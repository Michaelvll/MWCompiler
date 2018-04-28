package mwcompiler.frontend;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.tools.ReturnType;
import mwcompiler.utility.Location;
import mwcompiler.symbols.*;
import mwcompiler.utility.StringProcess;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;

import java.util.List;

import static mwcompiler.ast.nodes.ExprNode.OPs.*;
import static mwcompiler.symbols.NonArrayTypeSymbol.*;
import static mwcompiler.symbols.tools.ReturnType.LvalOrRval.LVAL;
import static mwcompiler.symbols.tools.ReturnType.LvalOrRval.RVAL;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class TypeCheckAstVisitor implements AstVisitor {
    private SymbolTable currentSymbolTable = null;
    private ReturnType returnType;
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
    public void visit(ProgramNode node) {
        node.getBlock().accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        ReturnType currentReturn = null;
        getCurrentSymbolTable(node);
        List<Node> statements = node.getStatements();
        Integer statementNum = node.getStatements().size();
        for (Integer index = 0; index < statementNum; ++index) {
            Node statement = statements.get(index);
            statement.accept(this);
            if (statement instanceof ReturnNode) {
                currentReturn = returnType;
                if (index < statementNum - 1) {
                    CompileWarining.add(stage, "Statements after " + StringProcess.getRefString("return") +
                            "will never be executed", statement.getStartLocation());
                }
                break;
            } else if (statement instanceof BreakNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage, "Statements after " + StringProcess.getRefString("break") +
                            "will never be executed", statement.getStartLocation());
                }
                break;
            } else if (statement instanceof ContinueNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage, "Statements after " + StringProcess.getRefString("continue") +
                            "will never be executed", statement.getStartLocation());
                }
                break;
            }
        }

        getOuterSymbolTable();
        returnType = currentReturn;
    }

    // Declarations
    @Override
    public void visit(VariableDeclNode node) {
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
            node.getInit().accept(this);
            ReturnType rhsType = returnType;
            if (rhsType.typeSymbol != nullTypeSymbol) {
                if (declType != rhsType.typeSymbol) {
                    throwTypeMismatchErr(node.getTypeSymbol(), returnType.typeSymbol, node.getInitPos());
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

        returnType = null;
    }

    @Override
    public void visit(FunctionDeclNode node) {
        try {
            node.getFunctionTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getStartLocation());
        }
        getCurrentSymbolTable(node.getBody());
        for (VariableDeclNode param : node.getParamList()) {
            param.accept(this);
        }
        node.getBody().accept(this);
        ReturnType blockReturn = returnType;
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

        returnType = null;
    }

    @Override
    public void visit(ClassDeclNode node) {
        inClass = true;
        getCurrentSymbolTable(node.getBody());
        currentSymbolTable.put(InstanceSymbol.thisInstanceSymbol, node.getClassSymbol());
        node.getBody().accept(this);
        inClass = false;
    }


    // Expressions
    @Override
    public void visit(BinaryExprNode node) {
        ReturnType lhsType;
        ReturnType rhsType;
        node.getLeft().accept(this);
        lhsType = returnType;
        node.getRight().accept(this);
        rhsType = returnType;

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
                    returnType = new ReturnType(voidTypeSymbol, RVAL);
                } else {
                    throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
                }

                break;
            case ADD:
                if (lhsType.typeSymbol != intTypeSymbol && lhsType.typeSymbol != stringTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                returnType = new ReturnType(lhsType.typeSymbol, RVAL);
                break;
            case SUB: case DIV: case MOD: case MUL: case LSFT: case RSFT: case BITOR: case BITAND: case BITNOT:
            case BITXOR:
                if (lhsType.typeSymbol != intTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                returnType = new ReturnType(intTypeSymbol, RVAL);
                break;
            case LT: case GT: case LTE: case GTE:
                if (lhsType.typeSymbol != stringTypeSymbol && lhsType.typeSymbol != intTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                returnType = new ReturnType(boolTypeSymbol, RVAL);
                break;
            case AND: case OR:
                if (lhsType.typeSymbol != boolTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                returnType = new ReturnType(boolTypeSymbol, RVAL);
            case EQ: case NEQ:
                if (rhsType.typeSymbol != nullTypeSymbol && lhsType.typeSymbol != rhsType.typeSymbol) {
                    throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
                }
                if (lhsType.typeSymbol == voidTypeSymbol)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                returnType = new ReturnType(boolTypeSymbol, RVAL);
                break;

        }

    }

    @Override
    public void visit(UnaryExprNode node) {
        node.getExpr().accept(this);
        ReturnType exprType = returnType;
        switch (node.getOp()) {
            case INC: case DEC: case INC_SUFF: case DEC_SUFF: case BITNOT:
                if (returnType.typeSymbol != intTypeSymbol) {
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                }
                if (returnType.lvalOrRval != LVAL) {
                    throw new CompileError(stage, "ERROR (Type Checking) Operator " + StringProcess.getRefString(node.getOp().toString())
                            + "not support for rvalue", node.getStartLocation());
                }
                returnType = new ReturnType(intTypeSymbol, RVAL);
                break;
            case NOT:
                if (returnType.typeSymbol != boolTypeSymbol) {
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                }

                returnType = new ReturnType(boolTypeSymbol, RVAL);
                break;
        }

    }

    @Override
    public void visit(IdentifierExprNode node) {
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        TypeSymbol typeSymbol = currentSymbolTable.findAll(instanceSymbol);
        if (typeSymbol == null) {
            throwUndefined(instanceSymbol.getName(), node.getStartLocation());
        }
        returnType = new ReturnType(typeSymbol, LVAL);
    }

    @Override
    public void visit(NewExprNode node) {
        returnType = new ReturnType(node.getCreateType(), LVAL);
    }

    @Override
    public void visit(NullLiteralNode node) {
        returnType = new ReturnType(nullTypeSymbol, RVAL);

    }

    @Override
    public void visit(StringLiteralNode node) {
        returnType = new ReturnType(stringTypeSymbol, RVAL);
    }

    @Override
    public void visit(BoolLiteralNode node) {
        returnType = new ReturnType(boolTypeSymbol, RVAL);
    }

    @Override
    public void visit(IntLiteralNode node) {
        returnType = new ReturnType(intTypeSymbol, RVAL);
    }


    @Override
    public void visit(EmptyExprNode node) {
        returnType = null;
    }

    // For arguments type checking
    private void checkArgs(Node node, List<ExprNode> args, List<TypeSymbol> params, Location location) {
        if (args.size() != params.size()) {
            throw new CompileError(stage, "Number of arguments in function caller " +
                    "does not match the declaration. ", location);
        }
        for (Integer index = 0; index < args.size(); ++index) {
            args.get(index).accept(this);
            ReturnType argType = returnType;
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
    public void visit(FunctionCallNode node) {
        node.getCaller().accept(this);
        if (!(returnType.typeSymbol instanceof FunctionTypeSymbol)) {
            throw new CompileError(stage, "A non function name " + StringProcess.getRefString(returnType.typeSymbol.getName()) +
                    "is not callable ", node.getStartLocation());
        }
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) returnType.typeSymbol;
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionTypeSymbol.getParams();
        checkArgs(node, args, params, node.getStartLocation());
        if (functionTypeSymbol.getReturnType() instanceof ArrayTypeSymbol)
            returnType = new ReturnType(functionTypeSymbol.getReturnType(), LVAL);
        else
            returnType = new ReturnType(functionTypeSymbol.getReturnType(), RVAL);

    }

    @Override
    public void visit(ConstructorCallNode node) {
        NonArrayTypeSymbol classTypeSymbol = node.getClassTypeSymbol();
        FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) classTypeSymbol.findIn(InstanceSymbol.constructorSymbol);
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionTypeSymbol.getParams();
        checkArgs(node, args, params, node.getStartLocation());
        returnType = new ReturnType(classTypeSymbol, LVAL);
    }

    // Member access
    @Override
    public void visit(DotMemberNode node) {
        node.getContainer().accept(this);
        ReturnType container = returnType;
        TypeSymbol memberType;
        memberType = container.typeSymbol.findIn(node.getMember().getInstanceSymbol());
        returnType = new ReturnType(memberType, LVAL);

    }

    @Override
    public void visit(BrackMemberNode node) {
        node.getContainer().accept(this);
        ReturnType container = returnType;
        node.getSubscript().accept(this);
        ReturnType subscript = returnType;
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
            returnType = new ReturnType(arrayTypeSymbol.getNonArrayTypeSymbol(), LVAL);
        } else {
            returnType = new ReturnType(ArrayTypeSymbol.builder(arrayTypeSymbol.getNonArrayTypeSymbol().getName(), arrayTypeSymbol.getDim() - 1), LVAL);
        }
    }

    // Control Flow
    @Override
    public void visit(IfNode node) {
        if (node.getCondition() != null) {
            node.getCondition().accept(this);
            ReturnType cond = returnType;
            if (cond.typeSymbol != boolTypeSymbol) {
                throw new CompileError(stage, "If condition must be a bool type, not "
                        + cond.typeSymbol.getName(), node.getStartLocation());
            }
        }
        node.getBody().accept(this);
        if (node.getElseCondition() != null)
            node.getElseCondition().accept(this);
        returnType = null;
    }

    @Override
    public void visit(LoopNode node) {
        ++inLoop;
        getCurrentSymbolTable(node.getBody());
        if (node.getVarDecl() != null) {
            node.getVarDecl().accept(this);
        }
        if (node.getCondition() != null) {
            node.getCondition().accept(this);
            if (returnType.typeSymbol != boolTypeSymbol) {
                throw new CompileError(stage, "Condition statement in for loop should be bool type "
                        , node.getConditionPos());
            }
        }
        if (node.getStep() != null) {
            node.getStep().accept(this);
        }
        node.getBody().accept(this);

        --inLoop;
        returnType = null;
    }

    @Override
    public void visit(BreakNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Break statement should not appear out of loop scope "
                    , node.getStartLocation());
        }
        returnType = null;
    }

    @Override
    public void visit(ReturnNode node) {
        if (node.getReturnVal() != null) {
            node.getReturnVal().accept(this);
            returnType = new ReturnType(returnType.typeSymbol, RVAL);
        } else {
            returnType = new ReturnType(voidTypeSymbol, RVAL);
        }
    }

    @Override
    public void visit(ContinueNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Continue statement should not appear out of loop scope "
                    , node.getStartLocation());
        }
        returnType = null;
    }


}
