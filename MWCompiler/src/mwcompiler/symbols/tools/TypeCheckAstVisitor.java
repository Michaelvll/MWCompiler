package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import mwcompiler.utility.Location;
import mwcompiler.utility.StringProcess;

import java.util.List;

import static mwcompiler.ast.nodes.ExprNode.OPs.*;
import static mwcompiler.symbols.NonArrayTypeSymbol.*;
import static mwcompiler.symbols.tools.ReturnType.LvalOrRval.LVAL;
import static mwcompiler.symbols.tools.ReturnType.LvalOrRval.RVAL;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class TypeCheckAstVisitor implements AstVisitor<ReturnType> {
    private SymbolTable currentSymbolTable = null;
    private Integer inLoop = 0;
    private Boolean inClass = false;
    private String stage = "Type Checking";


    // For Error produce
    private void throwTypeMismatchErr(TypeSymbol lhsType, TypeSymbol rhsType, Location location) {
        throw new CompileError(stage, "Type Mismatch. In binary expression,  " + StringProcess.getRefString(lhsType.getName()) +
                "mismatches " + StringProcess.getRefString(rhsType.getName()), location);
    }

    private void throwNoSuchType(String typename, Location location) {
        throw new CompileError(stage, "Get an unknown type " + StringProcess.getRefString(typename), location);
    }

    private void throwNotSupport(ExprNode.OPs op, TypeSymbol typename, Location location) {
        throw new CompileError(stage, "Operator " + StringProcess.getRefString(op.toString()) + "not support for the type "
                + StringProcess.getRefString(typename.getName()), location);
    }

    private void throwUndefined(String name, Location location) {
        throw new CompileError(stage, "Can not resolve name " + StringProcess.getRefString(name), location);
    }

    // For Symbol Table
    private void getCurrentSymbolTable(BlockNode node) {
        if (node.getCurrentSymbolTable() == null) {
            node.setCurrentSymbolTable(new SymbolTable(currentSymbolTable));
        }
        currentSymbolTable = node.getCurrentSymbolTable();
    }

    private void getOuterSymbolTable() {
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
    }

    @Override
    public ReturnType visit(ProgramNode node) {
        return node.getBlock().accept(this);
    }

    @Override
    public ReturnType visit(BlockNode node) {
        ReturnType currentReturn = null;
        getCurrentSymbolTable(node);
        List<Node> statements = node.getStatements();
        Integer statementNum = node.getStatements().size();
        for (Integer index = 0; index < statementNum; ++index) {
            Node statement = statements.get(index);
            ReturnType stmtReturn = statement.accept(this);
            if (statement instanceof ReturnNode) {
                currentReturn = stmtReturn;
                if (index < statementNum - 1) {
                    CompileWarining.add(stage, "Statements after " + StringProcess.getRefString("return") +
                            "will never be executed", statement.getStartLocation());
                }
                break;
            }
            if (statement instanceof BreakNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage, "Statements after " + StringProcess.getRefString("break") +
                            "will never be executed", statement.getStartLocation());
                }
                break;
            }
            if (statement instanceof ContinueNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage, "Statements after " + StringProcess.getRefString("continue") +
                            "will never be executed", statement.getStartLocation());
                }
                break;
            }
        }

        getOuterSymbolTable();
        return currentReturn;
    }

    // Declarations
    @Override
    public ReturnType visit(VariableDeclNode node) {
        try {
            node.getTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getTypePos());
        }
        TypeSymbol declType = node.getTypeSymbol();
        if (declType == voidTypeSymbol || declType == nullTypeSymbol) {
            throw new CompileError(stage, "Can not declare a variable as type " + StringProcess.getRefString(declType.getName()),
                    node.getTypePos());
        }
        if (node.getInit() != null) {
            ReturnType rhsType = node.getInit().accept(this);
            if (rhsType.typeSymbol != nullTypeSymbol) {
                if (declType != rhsType.typeSymbol) {
                    throwTypeMismatchErr(node.getTypeSymbol(), rhsType.typeSymbol, node.getInitPos());
                }

            } else {
                if (declType.isPrimitiveType()) {
                    throw new CompileError(stage, "Assigning null to primitive type is not allowed ",
                            node.getStartLocation());
                }
            }

        }
        if (currentSymbolTable.findIn(node.getVarSymbol()) != null && !inClass) {
            throw new CompileError(stage, "Redeclare a variable " + node.getVarSymbol().getName()
                    + "in the same scope", node.getStartLocation());
        }
        currentSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());

        return null;
    }

    @Override
    public ReturnType visit(FunctionDeclNode node) {
        try {
            node.getFunctionTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getStartLocation());
        }
        getCurrentSymbolTable(node.getBody());
        for (VariableDeclNode param : node.getParamList()) {
            param.accept(this);
        }
        ReturnType blockReturn = node.getBody().accept(this);
        if (blockReturn == null) {
            if (node.getFunctionTypeSymbol().getReturnType() != voidTypeSymbol && node.getInstanceSymbol() != InstanceSymbol.constructorSymbol) {
                CompileWarining.add(stage, "Function " + StringProcess.getRefString(node.getInstanceSymbol().getName())
                        + "has no return statement ", node.getStartLocation());
            }
        } else {
            if (node.getInstanceSymbol() == InstanceSymbol.constructorSymbol) {
                if (blockReturn.typeSymbol != voidTypeSymbol)
                    throw new CompileError(stage, "Constructor can not return any value "
                            , node.getStartLocation());
            } else if (blockReturn.typeSymbol != node.getFunctionTypeSymbol().getReturnType())
                throw new CompileError(stage, "Function declared return " +
                        StringProcess.getRefString(node.getFunctionTypeSymbol().getReturnType().getName()) + "but return " +
                        StringProcess.getRefString(blockReturn.typeSymbol.getName()), node.getStartLocation());
        }
        return null;
    }

    @Override
    public ReturnType visit(ClassDeclNode node) {
        inClass = true;
        getCurrentSymbolTable(node.getBody());
        currentSymbolTable.put(InstanceSymbol.thisInstanceSymbol, node.getClassSymbol());
        node.getBody().accept(this);
        inClass = false;
        return null;
    }


    // Expressions
    @Override
    public ReturnType visit(BinaryExprNode node) {
        ReturnType lhsType = node.getLeft().accept(this);
        ReturnType rhsType = node.getRight().accept(this);

        if (node.getOp() != ASSIGN && node.getOp() != EQ && node.getOp() != NEQ && lhsType.typeSymbol != rhsType.typeSymbol) {
            throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
        }

        if (lhsType == null) {
            throw new RuntimeException("Compiler Bug: (Type Checking) Binary Expression get null type");
        }

        switch (node.getOp()) {
            case ASSIGN:
                if (lhsType.lvalOrRval == RVAL || node.getLeft() instanceof FunctionCallNode) {
                    throw new CompileError(stage, "Can not assign to a Rvalue "
                            , node.getStartLocation());
                }
                if (((lhsType.typeSymbol instanceof ArrayTypeSymbol || !lhsType.typeSymbol.isPrimitiveType()) && rhsType.typeSymbol == nullTypeSymbol)
                        || lhsType.typeSymbol == rhsType.typeSymbol) {
                    return new ReturnType(voidTypeSymbol, RVAL);
                }
                throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
            case ADD:
                if (lhsType.typeSymbol != intTypeSymbol && lhsType.typeSymbol != stringTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ReturnType(lhsType.typeSymbol, RVAL);
            case SUB: case DIV: case MOD: case MUL: case LSFT: case RSFT: case BITOR: case BITAND: case BITNOT:
            case BITXOR:
                if (lhsType.typeSymbol != intTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ReturnType(intTypeSymbol, RVAL);
            case LT: case GT: case LTE: case GTE:
                if (lhsType.typeSymbol != stringTypeSymbol && lhsType.typeSymbol != intTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ReturnType(boolTypeSymbol, RVAL);
            case AND: case OR:
                if (lhsType.typeSymbol != boolTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ReturnType(boolTypeSymbol, RVAL);
            case EQ: case NEQ:
                if (rhsType.typeSymbol != nullTypeSymbol && lhsType.typeSymbol != rhsType.typeSymbol) {
                    throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
                }
                if (lhsType.typeSymbol == voidTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ReturnType(boolTypeSymbol, RVAL);
            default:
                throw new RuntimeException("Compiler Bug: Undefined binary op");
        }
    }

    @Override
    public ReturnType visit(UnaryExprNode node) {
        ReturnType exprType = node.getExpr().accept(this);
        switch (node.getOp()) {
            case INC: case DEC: case INC_SUFF: case DEC_SUFF: case BITNOT:
                if (exprType.typeSymbol != intTypeSymbol) {
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                }
                if (exprType.lvalOrRval != LVAL) {
                    throw new CompileError(stage, "ERROR (Type Checking) Operator " + StringProcess.getRefString(node.getOp().toString())
                            + "not support for rvalue", node.getStartLocation());
                }
                return new ReturnType(intTypeSymbol, RVAL);
            case NOT:
                if (exprType.typeSymbol != boolTypeSymbol) {
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                }
                return new ReturnType(boolTypeSymbol, RVAL);
            default:
                throw new RuntimeException("Compiler Bug: Undefined unary op");
        }
    }

    @Override
    public ReturnType visit(IdentifierExprNode node) {
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        TypeSymbol typeSymbol = currentSymbolTable.findAll(instanceSymbol);
        if (typeSymbol == null) {
            throwUndefined(instanceSymbol.getName(), node.getStartLocation());
        }
        return new ReturnType(typeSymbol, LVAL);
    }

    @Override
    public ReturnType visit(NewExprNode node) {
        return new ReturnType(node.getCreateType(), LVAL);
    }

    @Override
    public ReturnType visit(NullLiteralNode node) {
        return new ReturnType(nullTypeSymbol, RVAL);

    }

    @Override
    public ReturnType visit(StringLiteralNode node) {
        return new ReturnType(stringTypeSymbol, RVAL);
    }

    @Override
    public ReturnType visit(BoolLiteralNode node) {
        return new ReturnType(boolTypeSymbol, RVAL);
    }

    @Override
    public ReturnType visit(IntLiteralNode node) {
        return new ReturnType(intTypeSymbol, RVAL);
    }


    @Override
    public ReturnType visit(EmptyExprNode node) {
        return null;
    }

    // For arguments type checking
    private void checkArgs(List<ExprNode> args, List<TypeSymbol> params, Location location) {
        if (args.size() != params.size()) {
            throw new CompileError(stage, "Number of arguments in function caller " +
                    "does not match the declaration. ", location);
        }
        for (Integer index = 0; index < args.size(); ++index) {
            ReturnType argType = args.get(index).accept(this);
            TypeSymbol paramType = params.get(index);
            if (argType.typeSymbol != paramType && argType.typeSymbol != nullTypeSymbol) {
                throw new CompileError(stage, "Type Mismatch. Argument (" + String.valueOf(index + 1)
                        + "), type " + StringProcess.getRefString(argType.typeSymbol.getName()) + "mismatches "
                        + StringProcess.getRefString(paramType.getName()), location);
            }
        }
    }

    // Function calls
    @Override
    public ReturnType visit(FunctionCallNode node) {
        ReturnType callerType = node.getCaller().accept(this);
        if (!(callerType.typeSymbol instanceof FunctionTypeSymbol)) {
            throw new CompileError(stage, "A non function name " + StringProcess.getRefString(callerType.typeSymbol.getName()) +
                    "is not callable ", node.getStartLocation());
        }
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) callerType.typeSymbol;
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionTypeSymbol.getParams();
        checkArgs(args, params, node.getStartLocation());
        if (functionTypeSymbol.getReturnType() instanceof ArrayTypeSymbol)
            return new ReturnType(functionTypeSymbol.getReturnType(), LVAL);
        return new ReturnType(functionTypeSymbol.getReturnType(), RVAL);

    }

    @Override
    public ReturnType visit(ConstructorCallNode node) {
        NonArrayTypeSymbol classTypeSymbol = node.getClassTypeSymbol();
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) classTypeSymbol.findIn(InstanceSymbol.constructorSymbol);
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionTypeSymbol.getParams();
        checkArgs(args, params, node.getStartLocation());
        return new ReturnType(classTypeSymbol, LVAL);
    }

    // Member access
    @Override
    public ReturnType visit(DotMemberNode node) {
        ReturnType container = node.getContainer().accept(this);
        TypeSymbol memberType;
        memberType = container.typeSymbol.findIn(node.getMember().getInstanceSymbol());
        return new ReturnType(memberType, LVAL);
    }

    @Override
    public ReturnType visit(BrackMemberNode node) {
        ReturnType container = node.getContainer().accept(this);
        ReturnType subscript = node.getSubscript().accept(this);
        if (!(container.typeSymbol instanceof ArrayTypeSymbol)) {
            throw new CompileError(stage, "Non array type can not get member using subscript, " +
                    StringProcess.getRefString(container.typeSymbol.getName()), node.getStartLocation());
        }
        if (subscript.typeSymbol != intTypeSymbol) {
            throw new CompileError(stage, "The subscript must have int type "
                    , node.getSubscript().getStartLocation());
        }
        ArrayTypeSymbol arrayTypeSymbol = (ArrayTypeSymbol) container.typeSymbol;
        if (arrayTypeSymbol.getDim() - 1 == 0) {
            return new ReturnType(arrayTypeSymbol.getNonArrayTypeSymbol(), LVAL);
        }
        return new ReturnType(ArrayTypeSymbol.builder(arrayTypeSymbol.getNonArrayTypeSymbol().getName(), arrayTypeSymbol.getDim() - 1), LVAL);
    }

    // Control Flow
    @Override
    public ReturnType visit(IfNode node) {
        if (node.getCondition() != null) {
            ReturnType cond = node.getCondition().accept(this);
            if (cond.typeSymbol != boolTypeSymbol) {
                throw new CompileError(stage, "If condition must be a bool type, not "
                        + cond.typeSymbol.getName(), node.getStartLocation());
            }
        }
        node.getBody().accept(this);
        if (node.getElseCondition() != null)
            node.getElseCondition().accept(this);
        return null;
    }

    @Override
    public ReturnType visit(LoopNode node) {
        ++inLoop;
        getCurrentSymbolTable(node.getBody());
        if (node.getVarDecl() != null) {
            node.getVarDecl().accept(this);
        }
        if (node.getCondition() != null) {
            if (node.getCondition().accept(this).typeSymbol != boolTypeSymbol) {
                throw new CompileError(stage, "Condition statement in for loop should be bool type "
                        , node.getConditionPos());
            }
        }
        if (node.getStep() != null) {
            node.getStep().accept(this);
        }
        node.getBody().accept(this);

        --inLoop;
        return null;
    }

    @Override
    public ReturnType visit(BreakNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Break statement should not appear out of loop scope "
                    , node.getStartLocation());
        }
        return null;
    }

    @Override
    public ReturnType visit(ReturnNode node) {
        if (node.getReturnVal() != null) {
            return new ReturnType(node.getReturnVal().accept(this).typeSymbol, RVAL);
        }
        return new ReturnType(voidTypeSymbol, RVAL);
    }

    @Override
    public ReturnType visit(ContinueNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Continue statement should not appear out of loop scope "
                    , node.getStartLocation());
        }
        return null;
    }


}
