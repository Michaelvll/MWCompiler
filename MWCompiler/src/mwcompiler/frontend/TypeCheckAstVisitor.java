package mwcompiler.frontend;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;
import mwcompiler.symbols.tools.ExprReturnType;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import mwcompiler.utility.Location;
import mwcompiler.utility.StringProcess;

import java.util.List;

import static mwcompiler.ast.nodes.ExprNode.OPs.*;
import static mwcompiler.symbols.NonArrayTypeSymbol.*;
import static mwcompiler.symbols.tools.ExprReturnType.LvalOrRval.LVAL;
import static mwcompiler.symbols.tools.ExprReturnType.LvalOrRval.RVAL;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class TypeCheckAstVisitor implements AstVisitor<ExprReturnType> {
    private SymbolTable currentSymbolTable = null;
    private Integer inLoop = 0;
    private Boolean inClass = false;
    private String stage = "Type Checking";

    public void apply(Node node){
        visit(node);
    }

    private ExprReturnType visit(Node node) {
        return node.accept(this);
    }

    // For Error produce
    private void throwTypeMismatchErr(TypeSymbol lhsType, TypeSymbol rhsType, Location location) {
        throw new CompileError(stage,
                "Type Mismatch. In binary expression,  " + StringProcess.getRefString(lhsType.getName()) + "mismatches "
                        + StringProcess.getRefString(rhsType.getName()),
                location);
    }

    private void throwNoSuchType(String typename, Location location) {
        throw new CompileError(stage, "Get an unknown type " + StringProcess.getRefString(typename), location);
    }

    private void throwNotSupport(ExprNode.OPs op, TypeSymbol typename, Location location) {
        throw new CompileError(stage, "Operator " + StringProcess.getRefString(op.toString())
                + "not support for the type " + StringProcess.getRefString(typename.getName()), location);
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
    public ExprReturnType visit(ProgramNode node) {
        return visit(node.getBlock());
    }

    @Override
    public ExprReturnType visit(BlockNode node) {
        ExprReturnType currentReturn = null;
        getCurrentSymbolTable(node);
        List<Node> statements = node.getStatements();
        Integer statementNum = node.getStatements().size();
        for (Integer index = 0; index < statementNum; ++index) {
            Node statement = statements.get(index);
            ExprReturnType stmtReturn = visit(statement);
            if (statement instanceof ReturnNode) {
                currentReturn = stmtReturn;
                if (index < statementNum - 1) {
                    CompileWarining.add(stage,
                            "Statements after " + StringProcess.getRefString("return") + "will never be executed",
                            statement.getStartLocation());
                }
                break;
            }
            if (statement instanceof BreakNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage,
                            "Statements after " + StringProcess.getRefString("break") + "will never be executed",
                            statement.getStartLocation());
                }
                break;
            }
            if (statement instanceof ContinueNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage,
                            "Statements after " + StringProcess.getRefString("continue") + "will never be executed",
                            statement.getStartLocation());
                }
                break;
            }
        }

        getOuterSymbolTable();
        return currentReturn;
    }

    // Declarations
    @Override
    public ExprReturnType visit(VariableDeclNode node) {
        try {
            node.getTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getTypePos());
        }
        TypeSymbol declType = node.getTypeSymbol();
        if (declType == voidTypeSymbol || declType == nullTypeSymbol) {
            throw new CompileError(stage,
                    "Can not declare a variable as type " + StringProcess.getRefString(declType.getName()),
                    node.getTypePos());
        }
        if (node.getInit() != null) {
            ExprReturnType rhsType = visit(node.getInit());
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
            throw new CompileError(stage, "Redeclare a variable " + node.getVarSymbol().getName() + "in the same scope",
                    node.getStartLocation());
        }
        currentSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());

        return null;
    }

    @Override
    public ExprReturnType visit(FunctionDeclNode node) {
        try {
            node.getFunctionTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getStartLocation());
        }
        getCurrentSymbolTable(node.getBody());
        for (VariableDeclNode param : node.getParamList()) {
            visit(param);
        }
        ExprReturnType blockReturn = visit(node.getBody());
        if (blockReturn == null) {
            if (node.getFunctionTypeSymbol().getReturnType() != voidTypeSymbol
                    && node.getInstanceSymbol() != InstanceSymbol.constructorSymbol) {
                CompileWarining.add(stage, "Function " + StringProcess.getRefString(node.getInstanceSymbol().getName())
                        + "has no return statement ", node.getStartLocation());
            }
        } else {
            if (node.getInstanceSymbol() == InstanceSymbol.constructorSymbol) {
                if (blockReturn.typeSymbol != voidTypeSymbol)
                    throw new CompileError(stage, "Constructor can not return any value ", node.getStartLocation());
            } else if (blockReturn.typeSymbol != node.getFunctionTypeSymbol().getReturnType())
                throw new CompileError(stage,
                        "Function declared return "
                                + StringProcess.getRefString(node.getFunctionTypeSymbol().getReturnType().getName())
                                + "but return " + StringProcess.getRefString(blockReturn.typeSymbol.getName()),
                        node.getStartLocation());
        }
        return null;
    }

    @Override
    public ExprReturnType visit(ClassDeclNode node) {
        inClass = true;
        getCurrentSymbolTable(node.getBody());
        currentSymbolTable.put(InstanceSymbol.thisInstanceSymbol, node.getClassSymbol());
        visit(node.getBody());
        inClass = false;
        return null;
    }

    // Expressions
    @Override
    public ExprReturnType visit(BinaryExprNode node) {
        ExprReturnType lhsType = visit(node.getLeft());
        ExprReturnType rhsType = visit(node.getRight());

        if (node.getOp() != ASSIGN && node.getOp() != EQ && node.getOp() != NEQ
                && lhsType.typeSymbol != rhsType.typeSymbol) {
            throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
        }

        if (lhsType == null) {
            throw new RuntimeException("Compiler Bug: (Type Checking) Binary Expression get null type");
        }

        switch (node.getOp()) {
        case ASSIGN:
            if (lhsType.lvalOrRval == RVAL || node.getLeft() instanceof FunctionCallNode) {
                throw new CompileError(stage, "Can not assign to a Rvalue ", node.getStartLocation());
            }
            if (((lhsType.typeSymbol instanceof ArrayTypeSymbol || !lhsType.typeSymbol.isPrimitiveType())
                    && rhsType.typeSymbol == nullTypeSymbol) || lhsType.typeSymbol == rhsType.typeSymbol) {
                return new ExprReturnType(voidTypeSymbol, RVAL);
            }
            throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
        case ADD:
            if (lhsType.typeSymbol != intTypeSymbol && lhsType.typeSymbol != stringTypeSymbol)
                throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
            return new ExprReturnType(lhsType.typeSymbol, RVAL);
        case SUB:
        case DIV:
        case MOD:
        case MUL:
        case LSFT:
        case RSFT:
        case BITOR:
        case BITAND:
        case BITNOT:
        case BITXOR:
            if (lhsType.typeSymbol != intTypeSymbol)
                throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
            return new ExprReturnType(intTypeSymbol, RVAL);
        case LT:
        case GT:
        case LTE:
        case GTE:
            if (lhsType.typeSymbol != stringTypeSymbol && lhsType.typeSymbol != intTypeSymbol)
                throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
            return new ExprReturnType(boolTypeSymbol, RVAL);
        case AND:
        case OR:
            if (lhsType.typeSymbol != boolTypeSymbol)
                throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
            return new ExprReturnType(boolTypeSymbol, RVAL);
        case EQ:
        case NEQ:
            if (rhsType.typeSymbol != nullTypeSymbol && lhsType.typeSymbol != rhsType.typeSymbol) {
                throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
            }
            if (lhsType.typeSymbol == voidTypeSymbol)
                throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
            return new ExprReturnType(boolTypeSymbol, RVAL);
        default:
            throw new RuntimeException("Compiler Bug: Undefined binary op");
        }
    }

    @Override
    public ExprReturnType visit(UnaryExprNode node) {
        ExprReturnType exprType = visit(node.getExpr());
        switch (node.getOp()) {
        case INC:
        case DEC:
        case INC_SUFF:
        case DEC_SUFF:
        case BITNOT:
            if (exprType.typeSymbol != intTypeSymbol) {
                throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
            }
            if (exprType.lvalOrRval != LVAL) {
                throw new CompileError(stage, "ERROR (Type Checking) Operator "
                        + StringProcess.getRefString(node.getOp().toString()) + "not support for rvalue",
                        node.getStartLocation());
            }
            return new ExprReturnType(intTypeSymbol, RVAL);
        case NOT:
            if (exprType.typeSymbol != boolTypeSymbol) {
                throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
            }
            return new ExprReturnType(boolTypeSymbol, RVAL);
        case ADD:
        case SUB:
            if (exprType.typeSymbol != intTypeSymbol)
                throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
            return new ExprReturnType(intTypeSymbol, RVAL);
        default:
            throw new RuntimeException("Compiler Bug: Undefined unary op");
        }
    }

    @Override
    public ExprReturnType visit(IdentifierExprNode node) {
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        TypeSymbol typeSymbol = currentSymbolTable.findAll(instanceSymbol);
        if (typeSymbol == null) {
            throwUndefined(instanceSymbol.getName(), node.getStartLocation());
        }
        return new ExprReturnType(typeSymbol, LVAL);
    }

    @Override
    public ExprReturnType visit(NewExprNode node) {
        return new ExprReturnType(node.getCreateType(), LVAL);
    }

    @Override
    public ExprReturnType visit(NullLiteralNode node) {
        return new ExprReturnType(nullTypeSymbol, RVAL);

    }

    @Override
    public ExprReturnType visit(StringLiteralNode node) {
        return new ExprReturnType(stringTypeSymbol, RVAL);
    }

    @Override
    public ExprReturnType visit(BoolLiteralNode node) {
        return new ExprReturnType(boolTypeSymbol, RVAL);
    }

    @Override
    public ExprReturnType visit(IntLiteralNode node) {
        return new ExprReturnType(intTypeSymbol, RVAL);
    }

    @Override
    public ExprReturnType visit(EmptyExprNode node) {
        return null;
    }

    // For arguments type checking
    private void checkArgs(List<ExprNode> args, List<TypeSymbol> params, Location location) {
        if (args.size() != params.size()) {
            throw new CompileError(stage,
                    "Number of arguments in function caller " + "does not match the declaration. ", location);
        }
        for (Integer index = 0; index < args.size(); ++index) {
            ExprReturnType argType = visit(args.get(index));
            TypeSymbol paramType = params.get(index);
            if (argType.typeSymbol != paramType && argType.typeSymbol != nullTypeSymbol) {
                throw new CompileError(stage,
                        "Type Mismatch. Argument (" + String.valueOf(index + 1) + "), type "
                                + StringProcess.getRefString(argType.typeSymbol.getName()) + "mismatches "
                                + StringProcess.getRefString(paramType.getName()),
                        location);
            }
        }
    }

    // Function calls
    @Override
    public ExprReturnType visit(FunctionCallNode node) {
        ExprReturnType callerType = visit(node.getCaller());
        if (!(callerType.typeSymbol instanceof FunctionTypeSymbol)) {
            throw new CompileError(stage, "A non function name "
                    + StringProcess.getRefString(callerType.typeSymbol.getName()) + "is not callable ",
                    node.getStartLocation());
        }
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) callerType.typeSymbol;
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionTypeSymbol.getParams();
        checkArgs(args, params, node.getStartLocation());
        if (functionTypeSymbol.getReturnType() instanceof ArrayTypeSymbol)
            return new ExprReturnType(functionTypeSymbol.getReturnType(), LVAL);
        return new ExprReturnType(functionTypeSymbol.getReturnType(), RVAL);

    }

    @Override
    public ExprReturnType visit(ConstructorCallNode node) {
        NonArrayTypeSymbol classTypeSymbol = node.getClassTypeSymbol();
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) classTypeSymbol
                .findIn(InstanceSymbol.constructorSymbol);
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionTypeSymbol.getParams();
        checkArgs(args, params, node.getStartLocation());
        return new ExprReturnType(classTypeSymbol, LVAL);
    }

    // Member access
    @Override
    public ExprReturnType visit(DotMemberNode node) {
        ExprReturnType container = visit(node.getContainer());
        TypeSymbol memberType;
        memberType = container.typeSymbol.findIn(node.getMember().getInstanceSymbol());
        return new ExprReturnType(memberType, LVAL);
    }

    @Override
    public ExprReturnType visit(BrackMemberNode node) {
        ExprReturnType container = visit(node.getContainer());
        ExprReturnType subscript = visit(node.getSubscript());
        if (!(container.typeSymbol instanceof ArrayTypeSymbol)) {
            throw new CompileError(stage, "Non array type can not get member using subscript, "
                    + StringProcess.getRefString(container.typeSymbol.getName()), node.getStartLocation());
        }
        if (subscript.typeSymbol != intTypeSymbol) {
            throw new CompileError(stage, "The subscript must have int type ", node.getSubscript().getStartLocation());
        }
        ArrayTypeSymbol arrayTypeSymbol = (ArrayTypeSymbol) container.typeSymbol;
        if (arrayTypeSymbol.getDim() - 1 == 0) {
            return new ExprReturnType(arrayTypeSymbol.getNonArrayTypeSymbol(), LVAL);
        }
        return new ExprReturnType(ArrayTypeSymbol.builder(arrayTypeSymbol.getNonArrayTypeSymbol().getName(),
                arrayTypeSymbol.getDim() - 1), LVAL);
    }

    // Control Flow
    @Override
    public ExprReturnType visit(IfNode node) {
        if (node.getCondition() != null) {
            ExprReturnType cond = visit(node.getCondition());
            if (cond.typeSymbol != boolTypeSymbol) {
                throw new CompileError(stage, "If condition must be a bool type, not " + cond.typeSymbol.getName(),
                        node.getStartLocation());
            }
        }
        visit(node.getBody());
        if (node.getElseCondition() != null)
            visit(node.getElseCondition());
        return null;
    }

    @Override
    public ExprReturnType visit(LoopNode node) {
        ++inLoop;
        getCurrentSymbolTable(node.getBody());
        if (node.getVarDecl() != null) {
            visit(node.getVarDecl());
        }
        if (node.getCondition() != null) {
            if (visit(node.getCondition()).typeSymbol != boolTypeSymbol) {
                throw new CompileError(stage, "Condition statement in for loop should be bool type ",
                        node.getConditionPos());
            }
        }
        if (node.getStep() != null) {
            visit(node.getStep());
        }
        visit(node.getBody());

        --inLoop;
        return null;
    }

    @Override
    public ExprReturnType visit(BreakNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Break statement should not appear out of loop scope ",
                    node.getStartLocation());
        }
        return null;
    }

    @Override
    public ExprReturnType visit(ReturnNode node) {
        if (node.getReturnVal() != null) {
            return new ExprReturnType(visit(node.getReturnVal()).typeSymbol, RVAL);
        }
        return new ExprReturnType(voidTypeSymbol, RVAL);
    }

    @Override
    public ExprReturnType visit(ContinueNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Continue statement should not appear out of loop scope ",
                    node.getStartLocation());
        }
        return null;
    }

}
