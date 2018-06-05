package mwcompiler.frontend;

import mwcompiler.ast.nodes.BlockNode;
import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.nodes.ProgramNode;
import mwcompiler.ast.nodes.declarations.ClassDeclNode;
import mwcompiler.ast.nodes.declarations.FunctionDeclNode;
import mwcompiler.ast.nodes.declarations.VariableDeclNode;
import mwcompiler.ast.nodes.expressions.*;
import mwcompiler.ast.nodes.literals.BoolLiteralNode;
import mwcompiler.ast.nodes.literals.IntLiteralNode;
import mwcompiler.ast.nodes.literals.NullLiteralNode;
import mwcompiler.ast.nodes.literals.StringLiteralNode;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;
import mwcompiler.symbols.tools.ExprType;
import mwcompiler.utility.*;

import java.util.List;

import static mwcompiler.symbols.BaseTypeSymbol.*;
import static mwcompiler.symbols.tools.ExprType.ValType.LVAL;
import static mwcompiler.symbols.tools.ExprType.ValType.RVAL;
import static mwcompiler.utility.ExprOps.*;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class TypeCheckAstVisitor implements AstVisitor<ExprType> {
    private SymbolTable currentSymbolTable = null;
    private int inLoop = 0;
    private boolean inClass = false;
    private String stage = "Type Checking";
    private TypeSymbol expectedReturnType;

    private boolean inNew = false;

    public void apply(Node node) {
        visit(node);
    }

    private ExprType visit(Node node) {
        return node.accept(this);
    }

    // For Error produce
    private void throwTypeMismatchErr(Symbol lhsType, Symbol rhsType, Location location) {
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
            node.setCurrentSymbolTable(SymbolTable.builder(currentSymbolTable));
        }
        currentSymbolTable = node.getCurrentSymbolTable();
    }

    private void getOuterSymbolTable() {
        currentSymbolTable = currentSymbolTable.getParentSymbolTable();
    }

    private ExprType setType(ExprNode node, Symbol type, ExprType.ValType valType) {
        if (type instanceof TypeSymbol)
            node.setType((TypeSymbol) type);
        return new ExprType(type, valType);
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
        int statementNum = node.getStatements().size();
        for (int index = 0; index < statementNum; ++index) {
            Node statement = statements.get(index);
            ExprType stmtReturn = visit(statement);
            if (statement instanceof ReturnNode) {
                currentReturn = stmtReturn;
                if (index < statementNum - 1) {
                    CompileWarining.add(stage,
                            "Statements after " + StringProcess.getRefString("return") + "will never be executed",
                            statement.location());
                }
                node.clearStatement(index + 1);
                break;
            }
            if (statement instanceof BreakNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage,
                            "Statements after " + StringProcess.getRefString("break") + "will never be executed",
                            statement.location());
                }
                for (int removeInt = statementNum - 1; removeInt > index; --removeInt) statements.remove(removeInt);
                break;
            }
            if (statement instanceof ContinueNode) {
                if (index < statementNum - 1) {
                    CompileWarining.add(stage,
                            "Statements after " + StringProcess.getRefString("continue") + "will never be executed",
                            statement.location());
                }
                for (int removeInt = statementNum - 1; removeInt > index; --removeInt) statements.remove(removeInt);
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
        if (node.init() != null) {
            ExprType rhsType = visit(node.init());
            if (rhsType.symbol != NULL_TYPE_SYMBOL) {
                if (declType != rhsType.symbol) {
                    throwTypeMismatchErr(node.getTypeSymbol(), rhsType.symbol, node.getInitPos());
                }

            } else {
                if (declType.isPrimitiveTypeBase()) {
                    throw new CompileError(stage, "Assigning null to primitive type is not allowed ",
                            node.location());
                }
            }

        }
        if (currentSymbolTable.findIn(node.getVarSymbol()) != null && !inClass) {
            throw new CompileError(stage, "Redeclare a variable " + StringProcess.getRefString(node.getVarSymbol().getName()) + "in the same scope",
                    node.location());
        }
        currentSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());

        return null;
    }

    @Override
    public ExprType visit(FunctionDeclNode node) {
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        expectedReturnType = (node.getInstance() == Instance.CONSTRUCTOR) ?
                VOID_TYPE_SYMBOL : functionSymbol.getReturnType();
        try {
            functionSymbol.checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.location());
        }
        getCurrentSymbolTable(node.getBody());
        node.getParamList().forEach(this::visit);
        ExprType blockReturn = visit(node.getBody());
        if (blockReturn == null) {
            if (node.getFunctionSymbol().getReturnType() != VOID_TYPE_SYMBOL
                    && node.getInstance() != Instance.CONSTRUCTOR) {
                CompileWarining.add(stage, "Function " + StringProcess.getRefString(node.getInstance().getName())
                        + "has no return statement ", node.location());
            }
        }
        expectedReturnType = null;
        return null;
    }

    @Override
    public ExprType visit(ClassDeclNode node) {
        inClass = true;
        getCurrentSymbolTable(node.getBody());
        currentSymbolTable.put(Instance.THIS, node.getClassSymbol());
        visit(node.getBody());
        inClass = false;
        return null;
    }

    // Expressions
    @Override
    public ExprType visit(BinaryExprNode node) {
        ExprType lhsType = visit(node.left());
        ExprType rhsType = visit(node.right());
        TypeSymbol lhsTypeSymbol = (TypeSymbol) lhsType.symbol;
        TypeSymbol rhsTypeSymbol = (TypeSymbol) rhsType.symbol;

        if (node.op() != ASSIGN && node.op() != EQ && node.op() != NEQ
                && lhsTypeSymbol != rhsTypeSymbol) {
            throwTypeMismatchErr(lhsTypeSymbol, rhsTypeSymbol, node.location());
        }


        switch (node.op()) {
            case ASSIGN:
                if (lhsType.valType == RVAL || node.left() instanceof FunctionCallNode) {
                    throw new CompileError(stage, "Can not assign to a Rvalue ", node.location());
                }
                if (node.left() instanceof IdentifierExprNode) {
                    if (((IdentifierExprNode) node.left()).getInstance() == Instance.THIS)
                        throw new CompileError(stage, "Can not assign to "
                                + StringProcess.getRefString("this"), node.location());
                }
                if (((!lhsTypeSymbol.isPrimitiveType()) && rhsTypeSymbol == NULL_TYPE_SYMBOL) || lhsTypeSymbol == rhsTypeSymbol) {
                    return setType(node, VOID_TYPE_SYMBOL, RVAL);
                }
                throwTypeMismatchErr(lhsTypeSymbol, rhsTypeSymbol, node.location());
            case ADD:
                if (lhsTypeSymbol != INT_TYPE_SYMBOL && lhsTypeSymbol != STRING_TYPE_SYMBOL)
                    throwNotSupport(node.op(), lhsTypeSymbol, node.location());
                return setType(node, lhsTypeSymbol, RVAL);
            case SUB: case DIV: case MOD: case MUL: case LSFT: case RSFT: case BITOR: case BITAND:
            case BITXOR:
                if (lhsTypeSymbol != INT_TYPE_SYMBOL)
                    throwNotSupport(node.op(), lhsTypeSymbol, node.location());
                return setType(node, INT_TYPE_SYMBOL, RVAL);
            case LT: case GT: case LTE:
            case GTE:
                if (lhsTypeSymbol != STRING_TYPE_SYMBOL && lhsTypeSymbol != INT_TYPE_SYMBOL)
                    throwNotSupport(node.op(), lhsTypeSymbol, node.location());
                return setType(node, BOOL_TYPE_SYMBOL, RVAL);
            case AND: case OR:
                if (lhsTypeSymbol != BOOL_TYPE_SYMBOL)
                    throwNotSupport(node.op(), lhsTypeSymbol, node.location());
                return setType(node, BOOL_TYPE_SYMBOL, RVAL);
            case EQ: case NEQ:
                if (rhsTypeSymbol != NULL_TYPE_SYMBOL && lhsTypeSymbol != rhsTypeSymbol) {
                    throwTypeMismatchErr(lhsTypeSymbol, rhsTypeSymbol, node.location());
                }
                if (lhsTypeSymbol == VOID_TYPE_SYMBOL)
                    throwNotSupport(node.op(), lhsTypeSymbol, node.location());
                return setType(node, BOOL_TYPE_SYMBOL, RVAL);
            default:
                throw new RuntimeException("Compiler Bug: Undefined binary op");
        }
    }

    @Override
    public ExprType visit(UnaryExprNode node) {
        ExprType exprType = visit(node.getExpr());
        TypeSymbol exprTypeSymbol = (TypeSymbol) exprType.symbol;
        switch (node.getOp()) {
            case INC: case DEC: case INC_SUFF: case DEC_SUFF:
                if (exprTypeSymbol != INT_TYPE_SYMBOL) {
                    throwNotSupport(node.getOp(), exprTypeSymbol, node.location());
                }
                if (exprType.valType != LVAL) {
                    throw new CompileError(stage, "ERROR (Type Checking) Operator "
                            + StringProcess.getRefString(node.getOp().toString()) + "not support for rvalue",
                            node.location());
                }
                return setType(node, INT_TYPE_SYMBOL, RVAL);
            case NOT:
                if (exprTypeSymbol != BOOL_TYPE_SYMBOL) {
                    throwNotSupport(node.getOp(), exprTypeSymbol, node.location());
                }
                return setType(node, BOOL_TYPE_SYMBOL, RVAL);
            case ADD: case SUB: case BITNOT:
                if (exprTypeSymbol != INT_TYPE_SYMBOL)
                    throwNotSupport(node.getOp(), exprTypeSymbol, node.location());
                return setType(node, INT_TYPE_SYMBOL, RVAL);
            default:
                throw new RuntimeException("Compiler Bug: Undefined unary op");
        }
    }

    @Override
    public ExprType visit(IdentifierExprNode node) {
        Instance instance = node.getInstance();
        SymbolInfo symbolInfo = currentSymbolTable.findAll(instance);
        if (symbolInfo == null) {
            throwUndefined(instance.getName(), node.location());
        }
        return setType(node, symbolInfo.getSymbol(), LVAL);
    }

    @Override
    public ExprType visit(NewExprNode node) {
        for (ExprNode dimArg : node.getDimArgs()) {
            ExprType dimArgType = visit(dimArg);
            if (dimArgType.symbol != BaseTypeSymbol.INT_TYPE_SYMBOL)
                throw new CompileError(stage, "New Array dim should be int type but " + StringProcess.getRefString(dimArgType.symbol.getName()), node.location());
        }
        return setType(node, node.getCreateType(), LVAL);
    }

    @Override
    public ExprType visit(NullLiteralNode node) {
        return setType(node, NULL_TYPE_SYMBOL, RVAL);

    }

    @Override
    public ExprType visit(StringLiteralNode node) {
        return setType(node, STRING_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(BoolLiteralNode node) {
        return setType(node, BOOL_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(IntLiteralNode node) {
        return setType(node, INT_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(EmptyExprNode node) {
        return null;
    }

    // For arguments type checking
    private void checkArgs(List<ExprNode> args, List<TypeSymbol> params, Location location) {
        if (args.size() != params.size()) {
            throw new CompileError(stage,
                    "Number of arguments in callee caller " + "does not match the declaration. ", location);
        }
        for (int index = 0; index < args.size(); ++index) {
            ExprType argType = visit(args.get(index));
            TypeSymbol paramType = params.get(index);
            if (argType.symbol != paramType && argType.symbol != NULL_TYPE_SYMBOL) {
                throw new CompileError(stage,
                        "Type Mismatch. Argument (" + String.valueOf(index + 1) + "), type "
                                + StringProcess.getRefString(argType.symbol.getName()) + "mismatches "
                                + StringProcess.getRefString(paramType.getName()),
                        location);
            }
        }
    }

    // Function calls
    @Override
    public ExprType visit(FunctionCallNode node) {
        ExprType callerType = visit(node.getCaller());
        if (!(callerType.symbol instanceof FunctionSymbol)) {
            throw new CompileError(stage, "A non callee name "
                    + StringProcess.getRefString(callerType.symbol.getName()) + "is not callable ",
                    node.location());
        }
        FunctionSymbol functionSymbol = (FunctionSymbol) callerType.symbol;

        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionSymbol.getParams();
        checkArgs(args, params, node.location());
        if (functionSymbol.getReturnType() instanceof ArrayTypeSymbol)
            return setType(node, functionSymbol.getReturnType(), LVAL);
        return setType(node, functionSymbol.getReturnType(), RVAL);

    }

    @Override
    public ExprType visit(ConstructorCallNode node) {
        BaseTypeSymbol classTypeSymbol = node.getClassTypeSymbol();
        FunctionSymbol functionSymbol = (FunctionSymbol) classTypeSymbol
                .findIn(Instance.CONSTRUCTOR).getSymbol();
        List<ExprNode> args = node.getArgs();
        List<TypeSymbol> params = functionSymbol.getParams();
        checkArgs(args, params, node.location());
        return setType(node, classTypeSymbol, LVAL);
    }

    // Member access
    @Override
    public ExprType visit(DotMemberNode node) {
//        System.err.println(node.location());
        
        ExprType container = visit(node.getContainer());
        TypeSymbol containerTypeSymbol = (TypeSymbol) container.symbol;
        Symbol memberType;
        try {
            memberType = containerTypeSymbol.findIn(node.getMember().getInstance()).getSymbol();
        } catch (RuntimeException e) {
            throw new CompileError(stage, e.getMessage(), node.location());
        }
        return setType(node, memberType, LVAL);
    }

    @Override
    public ExprType visit(BrackMemberNode node) {
        ExprType container = visit(node.getContainer());
        ExprType subscript = visit(node.getSubscript());
        if (!(container.symbol instanceof ArrayTypeSymbol)) {
            throw new CompileError(stage, "Non array type can not get member using subscript, "
                    + StringProcess.getRefString(container.symbol.getName()), node.location());
        }
        if (subscript.symbol != INT_TYPE_SYMBOL) {
            throw new CompileError(stage, "The subscript must have int type ", node.getSubscript().location());
        }
        ArrayTypeSymbol arrayTypeSymbol = (ArrayTypeSymbol) container.symbol;
        if (arrayTypeSymbol.getDim() - 1 == 0) {
            return setType(node, arrayTypeSymbol.getBaseTypeSymbol(), LVAL);
        }
        return setType(node, ArrayTypeSymbol.builder(arrayTypeSymbol.getBaseTypeSymbol().getName(),
                arrayTypeSymbol.getDim() - 1), LVAL);
    }

    // Control Flow
    @Override
    public ExprType visit(IfNode node) {
        if (node.getCondition() != null) {
            ExprType cond = visit(node.getCondition());
            if (cond.symbol != BOOL_TYPE_SYMBOL) {
                throw new CompileError(stage, "If condition must be a bool type, not " + cond.symbol.getName(),
                        node.location());
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
            if (visit(node.getCondition()).symbol != BOOL_TYPE_SYMBOL) {
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
                    node.location());
        }
        return null;
    }

    @Override
    public ExprType visit(ReturnNode node) {
        if (expectedReturnType == null) {
            throw new CompileError(stage, "ReturnInst statement is unexpected in this scope", node.location());
        }
        if (node.getReturnVal() != null) {
            TypeSymbol returnType = (TypeSymbol) visit(node.getReturnVal()).symbol;
            if (returnType != NULL_TYPE_SYMBOL && expectedReturnType != returnType) {
                throw new CompileError(stage, "ReturnInst type " + StringProcess.getRefString(expectedReturnType.getName())
                        + "expected, but " + StringProcess.getRefString(returnType.getName())
                        + "returned.", node.location());
            }
            if (returnType == VOID_TYPE_SYMBOL) {
                throw new CompileError(stage, "Cannot return type " + StringProcess.getRefString("void"),
                        node.location());
            }
            return setType(node, visit(node.getReturnVal()).symbol, RVAL);
        }
        if (expectedReturnType != VOID_TYPE_SYMBOL) {
            throw new CompileError(stage, "ReturnInst type " + StringProcess.getRefString(expectedReturnType.getName())
                    + "expected, but " + StringProcess.getRefString("void")
                    + "returned.", node.location());
        }
        return setType(node, VOID_TYPE_SYMBOL, RVAL);
    }

    @Override
    public ExprType visit(ContinueNode node) {
        if (inLoop == 0) {
            throw new CompileError(stage, "Continue statement should not appear out of loop scope ",
                    node.location());
        }
        return null;
    }

}
