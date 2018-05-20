package mwcompiler.frontend;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;
import mwcompiler.symbols.tools.ExprType;
import mwcompiler.utility.*;

import java.util.List;

import static mwcompiler.symbols.NonArrayTypeSymbol.*;
import static mwcompiler.symbols.tools.ExprType.LvalOrRval.LVAL;
import static mwcompiler.symbols.tools.ExprType.LvalOrRval.RVAL;
import static mwcompiler.utility.ExprOps.*;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class TypeCheckAstVisitor implements AstVisitor<ExprType> {
    private SymbolTable currentSymbolTable = null;
    private Integer inLoop = 0;
    private Boolean inClass = false;
    private String stage = "Type Checking";
    private TypeSymbol expectedReturnType;

    private Boolean inNew = false;

    public void apply(Node node) {
        visit(node);
    }

    private ExprType visit(Node node) {
        return node.accept(this);
    }

    // For Error produce
    private void throwTypeMismatchErr(TypeSymbol lhsType, TypeSymbol rhsType, Location location) {
        throw new CompileError(stage,
                "Type Mismatch. In binary expression, " + StringProcess.getRefString(lhsType.getName()) + "mismatches "
                        + StringProcess.getRefString(rhsType.getName()),
                location);
    }

    private void throwNoSuchType(String typename, Location location) {
        throw new CompileError(stage, "Get an unknown type " + StringProcess.getRefString(typename), location);
    }

    private void throwNotSupport(ExprOps op, TypeSymbol typename, Location location) {
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
    public ExprType visit(ProgramNode node) {
        return visit(node.getBlock());
    }

    @Override
    public ExprType visit(BlockNode node) {
        ExprType currentReturn = null;
        getCurrentSymbolTable(node);
        List<Node> statements = node.getStatements();
        Integer statementNum = node.getStatements().size();
        for (Integer index = 0; index < statementNum; ++index) {
            Node statement = statements.get(index);
            ExprType stmtReturn = visit(statement);
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
    public ExprType visit(VariableDeclNode node) {
        try {
            node.getTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getTypePos());
        }
        TypeSymbol declType = node.getTypeSymbol();
        if (declType == VOID_TYPE_SYMBOL || declType == NULL_TYPE_SYMBOL) {
            throw new CompileError(stage,
                    "Can not declare a variable as type " + StringProcess.getRefString(declType.getName()),
                    node.getTypePos());
        }
        if (node.getInit() != null) {
            ExprType rhsType = visit(node.getInit());
            if (rhsType.typeSymbol != NULL_TYPE_SYMBOL) {
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
            throw new CompileError(stage, "Redeclare a variable " + StringProcess.getRefString(node.getVarSymbol().getName()) + "in the same scope",
                    node.getStartLocation());
        }
        currentSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());

        return null;
    }

    @Override
    public ExprType visit(FunctionDeclNode node) {
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        expectedReturnType = (node.getInstanceSymbol() == InstanceSymbol.CONSTRUCTOR) ?
                VOID_TYPE_SYMBOL : functionSymbol.getReturnType();
        try {
            functionSymbol.checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getStartLocation());
        }
        getCurrentSymbolTable(node.getBody());
        node.getParamList().forEach(this::visit);
        ExprType blockReturn = visit(node.getBody());
        if (blockReturn == null) {
            if (node.getFunctionSymbol().getReturnType() != VOID_TYPE_SYMBOL
                    && node.getInstanceSymbol() != InstanceSymbol.CONSTRUCTOR) {
                CompileWarining.add(stage, "Function " + StringProcess.getRefString(node.getInstanceSymbol().getName())
                        + "has no return statement ", node.getStartLocation());
            }
        }
        expectedReturnType = null;
        return null;
    }

    @Override
    public ExprType visit(ClassDeclNode node) {
        inClass = true;
        getCurrentSymbolTable(node.getBody());
        currentSymbolTable.put(InstanceSymbol.THIS, node.getClassSymbol());
        visit(node.getBody());
        inClass = false;
        return null;
    }

    // Expressions
    @Override
    public ExprType visit(BinaryExprNode node) {
        ExprType lhsType = visit(node.getLeft());
        ExprType rhsType = visit(node.getRight());

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
                if (node.getLeft() instanceof IdentifierExprNode) {
                    if (((IdentifierExprNode) node.getLeft()).getInstanceSymbol() == InstanceSymbol.THIS)
                        throw new CompileError(stage, "Can not assign to "
                                + StringProcess.getRefString("this"), node.getStartLocation());
                }
                if (((lhsType.typeSymbol instanceof ArrayTypeSymbol || !lhsType.typeSymbol.isPrimitiveType())
                        && rhsType.typeSymbol == NULL_TYPE_SYMBOL) || lhsType.typeSymbol == rhsType.typeSymbol) {
                    return new ExprType(VOID_TYPE_SYMBOL, RVAL);
                }
                throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
            case ADD:
                if (lhsType.typeSymbol != INT_TYPE_SYMBOL && lhsType.typeSymbol != STRING_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ExprType(lhsType.typeSymbol, RVAL);
            case SUB: case DIV: case MOD: case MUL: case LSFT: case RSFT: case BITOR: case BITAND:
            case BITXOR:
                if (lhsType.typeSymbol != INT_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ExprType(INT_TYPE_SYMBOL, RVAL);
            case LT: case GT: case LTE:
            case GTE:
                if (lhsType.typeSymbol != STRING_TYPE_SYMBOL && lhsType.typeSymbol != INT_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ExprType(BOOL_TYPE_SYMBOL, RVAL);
            case AND: case OR:
                if (lhsType.typeSymbol != BOOL_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ExprType(BOOL_TYPE_SYMBOL, RVAL);
            case EQ: case NEQ:
                if (rhsType.typeSymbol != NULL_TYPE_SYMBOL && lhsType.typeSymbol != rhsType.typeSymbol) {
                    throwTypeMismatchErr(lhsType.typeSymbol, rhsType.typeSymbol, node.getStartLocation());
                }
                if (lhsType.typeSymbol == VOID_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), lhsType.typeSymbol, node.getStartLocation());
                return new ExprType(BOOL_TYPE_SYMBOL, RVAL);
            default:
                throw new RuntimeException("Compiler Bug: Undefined binary op");
        }
    }

    @Override
    public ExprType visit(UnaryExprNode node) {
        ExprType exprType = visit(node.getExpr());
        switch (node.getOp()) {
            case INC: case DEC: case INC_SUFF: case DEC_SUFF:
                if (exprType.typeSymbol != INT_TYPE_SYMBOL) {
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                }
                if (exprType.lvalOrRval != LVAL) {
                    throw new CompileError(stage, "ERROR (Type Checking) Operator "
                            + StringProcess.getRefString(node.getOp().toString()) + "not support for rvalue",
                            node.getStartLocation());
                }
                return new ExprType(INT_TYPE_SYMBOL, RVAL);
            case NOT:
                if (exprType.typeSymbol != BOOL_TYPE_SYMBOL) {
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                }
                return new ExprType(BOOL_TYPE_SYMBOL, RVAL);
            case ADD: case SUB: case BITNOT:
                if (exprType.typeSymbol != INT_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), exprType.typeSymbol, node.getStartLocation());
                return new ExprType(INT_TYPE_SYMBOL, RVAL);
            default:
                throw new RuntimeException("Compiler Bug: Undefined unary op");
        }
    }

    @Override
    public ExprType visit(IdentifierExprNode node) {
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        SymbolInfo typeSymbol = currentSymbolTable.findAll(instanceSymbol);
        if (typeSymbol == null) {
            throwUndefined(instanceSymbol.getName(), node.getStartLocation());
        }
        return new ExprType(typeSymbol.getTypeSymbol(), LVAL);
    }

    @Override
    public ExprType visit(NewExprNode node) {
        return new ExprType(node.getCreateType(), LVAL);
    }

    @Override
    public ExprType visit(NullLiteralNode node) {
        return new ExprType(NULL_TYPE_SYMBOL, RVAL);

    }

    @Override
    public ExprType visit(StringLiteralNode node) {
        return new ExprType(STRING_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(BoolLiteralNode node) {
        return new ExprType(BOOL_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(IntLiteralNode node) {
        return new ExprType(INT_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(EmptyExprNode node) {
        return null;
    }

    // For arguments type checking
    private void checkArgs(List<ExprNode> args, List<TypeSymbol> params, Location location) {
        if (args.size() != params.size()) {
            throw new CompileError(stage,
                    "Number of arguments in function caller " + "does not match the declaration. ", location);
        }
        for (Integer index = 0; index < args.size(); ++index) {
            ExprType argType = visit(args.get(index));
            TypeSymbol paramType = params.get(index);
            if (argType.typeSymbol != paramType && argType.typeSymbol != NULL_TYPE_SYMBOL) {
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
    public ExprType visit(FunctionCallNode node) {
        ExprType callerType = visit(node.getCaller());
        if (!(callerType.typeSymbol instanceof FunctionSymbol)) {
            throw new CompileError(stage, "A non function name "
                    + StringProcess.getRefString(callerType.typeSymbol.getName()) + "is not callable ",
                    node.getStartLocation());
        }
        FunctionSymbol functionSymbol = (FunctionSymbol) callerType.typeSymbol;

        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionSymbol.getParams();
        checkArgs(args, params, node.getStartLocation());
        if (functionSymbol.getReturnType() instanceof ArrayTypeSymbol)
            return new ExprType(functionSymbol.getReturnType(), LVAL);
        return new ExprType(functionSymbol.getReturnType(), RVAL);

    }

    @Override
    public ExprType visit(ConstructorCallNode node) {
        NonArrayTypeSymbol classTypeSymbol = node.getClassTypeSymbol();
        FunctionSymbol functionSymbol = (FunctionSymbol) classTypeSymbol
                .findIn(InstanceSymbol.CONSTRUCTOR).getTypeSymbol();
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionSymbol.getParams();
        checkArgs(args, params, node.getStartLocation());
        return new ExprType(classTypeSymbol, LVAL);
    }

    // Member access
    @Override
    public ExprType visit(DotMemberNode node) {
        ExprType container = visit(node.getContainer());
        TypeSymbol memberType;
        memberType = container.typeSymbol.findIn(node.getMember().getInstanceSymbol()).getTypeSymbol();
        return new ExprType(memberType, LVAL);
    }

    @Override
    public ExprType visit(BrackMemberNode node) {
        ExprType container = visit(node.getContainer());
        ExprType subscript = visit(node.getSubscript());
        if (!(container.typeSymbol instanceof ArrayTypeSymbol)) {
            throw new CompileError(stage, "Non array type can not get member using subscript, "
                    + StringProcess.getRefString(container.typeSymbol.getName()), node.getStartLocation());
        }
        if (subscript.typeSymbol != INT_TYPE_SYMBOL) {
            throw new CompileError(stage, "The subscript must have int type ", node.getSubscript().getStartLocation());
        }
        ArrayTypeSymbol arrayTypeSymbol = (ArrayTypeSymbol) container.typeSymbol;
        if (arrayTypeSymbol.getDim() - 1 == 0) {
            return new ExprType(arrayTypeSymbol.getNonArrayTypeSymbol(), LVAL);
        }
        return new ExprType(ArrayTypeSymbol.builder(arrayTypeSymbol.getNonArrayTypeSymbol().getName(),
                arrayTypeSymbol.getDim() - 1), LVAL);
    }

    // Control Flow
    @Override
    public ExprType visit(IfNode node) {
        if (node.getCondition() != null) {
            ExprType cond = visit(node.getCondition());
            if (cond.typeSymbol != BOOL_TYPE_SYMBOL) {
                throw new CompileError(stage, "If condition must be a bool type, not " + cond.typeSymbol.getName(),
                        node.getStartLocation());
            }
        }
        visit(node.getBody());
        if (node.getElseNode() != null)
            visit(node.getElseNode());
        return null;
    }

    @Override
    public ExprType visit(LoopNode node) {
        ++inLoop;
        if (node.getVarInit() != null) {
            visit(node.getVarInit());
        }
        if (node.getCondition() != null) {
            if (visit(node.getCondition()).typeSymbol != BOOL_TYPE_SYMBOL) {
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
    public ExprType visit(BreakNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Break statement should not appear out of loop scope ",
                    node.getStartLocation());
        }
        return null;
    }

    @Override
    public ExprType visit(ReturnNode node) {
        if (expectedReturnType == null) {
            throw new CompileError(stage, "ReturnInst statement is unexpected in this scope", node.getStartLocation());
        }
        if (node.getReturnVal() != null) {
            TypeSymbol returnType = visit(node.getReturnVal()).typeSymbol;
            if (returnType != NULL_TYPE_SYMBOL && expectedReturnType != returnType) {
                throw new CompileError(stage, "ReturnInst type " + StringProcess.getRefString(expectedReturnType.getName())
                        + "expected, but " + StringProcess.getRefString(returnType.getName())
                        + "returned.", node.getStartLocation());
            }
            if (returnType == VOID_TYPE_SYMBOL) {
                throw new CompileError(stage, "Cannot return type " + StringProcess.getRefString("void"),
                        node.getStartLocation());
            }
            return new ExprType(visit(node.getReturnVal()).typeSymbol, RVAL);
        }
        if (expectedReturnType != VOID_TYPE_SYMBOL) {
            throw new CompileError(stage, "ReturnInst type " + StringProcess.getRefString(expectedReturnType.getName())
                    + "expected, but " + StringProcess.getRefString("void")
                    + "returned.", node.getStartLocation());
        }
        return new ExprType(VOID_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(ContinueNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Continue statement should not appear out of loop scope ",
                    node.getStartLocation());
        }
        return null;
    }

}
