package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.*;

import static mwcompiler.ast.nodes.ExprNode.OPs.*;
import static mwcompiler.symbols.NonArrayTypeSymbol.*;
import static mwcompiler.symbols.tools.ReturnType.LvalOrRval.LVAL;
import static mwcompiler.symbols.tools.ReturnType.LvalOrRval.RVAL;

// TODO there may be some problem with construct function
public class TypeCheckAstVisitor implements AstVisitor {
    private SymbolTable currentSymbolTable = null;
    private ReturnType returnType;

    private void throwTypeMismatchErr(TypeSymbol lhsType, TypeSymbol rhsType, Location location) {
        throw new RuntimeException("ERROR: (Type Checking) Type Mismatch. In binary expression, <"
                + lhsType.getName() + "> mismatches <" + rhsType.getName() + ">" + location.getLocation());
    }

    private void throwNoSuchType(String typename, Location location) {
        throw new RuntimeException("ERROR: (Type Checking) Get an unknown type <" + typename + ">" + location.getLocation());
    }

    private void throwNotSupport(ExprNode.OPs op, TypeSymbol typename, Location location) {
        throw new RuntimeException("ERROR: (Type Checking) Operator <" + op.toString()
                + "> not support for the type <" + typename.getName() + ">" + location.getLocation());
    }

    private void throwUndefined(String name, Location location) {
        throw new RuntimeException("ERROR: (Type Checking) Can not resolve name <" + name + ">" + location.getLocation());
    }

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
        Integer statementNum = node.getStatements().size();
        for (Integer index = 0; index < statementNum; ++index) {
            Node statement = node.getStatements().get(index);
            statement.accept(this);
            if (statement instanceof ReturnNode) {
                currentReturn = returnType;
                if (index < statementNum - 1) {
                    System.err.println("WARNING: (Type Checking) statements after <return> will never be executed"+ statement.getStartLocation().getLocation());
                }
                break;
            } else if (statement instanceof BreakNode) {
                if (index < statementNum - 1) {
                    System.err.println("WARNING: (Type Checking) statements after <break> will never be executed"+ statement.getStartLocation().getLocation());
                }
                break;
            } else if (statement instanceof ContinueNode) {
                if (index < statementNum - 1) {
                    System.err.println("WARNING: (Type Checking) statements after <continue> will never be executed"+ statement.getStartLocation().getLocation());
                }
                break;
            }
        }

        getOuterSymbolTable();
        returnType = currentReturn;
    }

    @Override
    public void visit(VariableDeclNode node) {
        try {
            node.getTypeSymbol().checkLegal();
        } catch (RuntimeException e) {
            throwNoSuchType(e.getMessage(), node.getTypePos());
        }
        if (node.getInit() != null) {
            node.getInit().accept(this);
            if (!returnType.equals(node.getTypeSymbol(), LVAL)) {
                throwTypeMismatchErr(node.getTypeSymbol(), returnType.typeSymbol, node.getInitPos());
            }
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
            if (node.getFunctionTypeSymbol().getReturnType() != voidTypeSymbol) {
                System.err.println("WARNING: (Type Checking) Function <"+ node.getInstanceSymbol().getName() + "> has no return statement " + node.getStartLocation().getLocation());
            }
        } else if (blockReturn.typeSymbol != node.getFunctionTypeSymbol().getReturnType()) {
            throw new RuntimeException("ERROR: (Type Checking) Function does not return a value as declared "
                    + node.getStartLocation().getLocation());
        }

        returnType = null;
    }

    @Override
    public void visit(ClassDeclNode node) {
        getCurrentSymbolTable(node.getBody());
        currentSymbolTable.put(InstanceSymbol.thisInstanceSybol, node.getClassSymbol());
        node.getBody().accept(this);
    }


    @Override
    public void visit(BinaryExprNode node) {
        ReturnType lhsType = null;
        ReturnType rhsType = null;
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
                if (lhsType.lvalOrRval == RVAL) {
                    throw new RuntimeException("ERROR: (Type Checking) Can not assign to a Rvalue "
                            + node.getStartLocation().getLocation());
                }
                if ((lhsType.typeSymbol instanceof ArrayTypeSymbol && rhsType.typeSymbol == nullTypeSymbol)
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
                if (lhsType.typeSymbol == voidTypeSymbol || lhsType.typeSymbol == constructorTypeSymbol)
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
        returnType = new ReturnType(stringTypeSymbol, LVAL);
    }

    @Override
    public void visit(BoolLiteralNode node) {
        returnType = new ReturnType(boolTypeSymbol, LVAL);
    }

    @Override
    public void visit(IntLiteralNode node) {
        returnType = new ReturnType(intTypeSymbol, LVAL);
    }


    @Override
    public void visit(EmptyExprNode node) {
        returnType = null;
    }

    @Override
    public void visit(FunctionCallNode node) {
        node.getCaller().accept(this);
        if (!(returnType.typeSymbol instanceof FunctionTypeSymbol)) {
            throw new RuntimeException("ERROR: (Type Checking) A non function name <"
                    + returnType.typeSymbol.getName() + "> is not callable " +
                    node.getStartLocation().getLocation());
        }
        returnType = new ReturnType(((FunctionTypeSymbol) returnType.typeSymbol).getReturnType(), RVAL);

    }

    @Override
    public void visit(DotMemberNode node) {
        node.getContainer().accept(this);
        ReturnType container = returnType;
        TypeSymbol memberType;
        try {
            memberType = container.typeSymbol.findIn(node.getMember().getInstanceSymbol());
        } catch (RuntimeException e) {
            throw new RuntimeException("ERROR: (Type Checking) " + e.getMessage() + node.getStartLocation().getLocation());
        }
        returnType = new ReturnType(memberType, RVAL);

    }

    @Override
    public void visit(BrackMemberNode node) {
        node.getContainer().accept(this);
        ReturnType container = returnType;
        node.getSubscript().accept(this);
        ReturnType subscript = returnType;
        if (!(container.typeSymbol instanceof ArrayTypeSymbol)) {
            throw new RuntimeException("ERROR: (Type Checking) Non array type can not get member using subscript, <"
                    + container.typeSymbol.getName() + "> " + node.getStartLocation().getLocation());
        }
        if (subscript.typeSymbol != intTypeSymbol) {
            throw new RuntimeException("ERROR: (Type Checking) The subscript must have int type "
                    + node.getSubscript().getStartLocation().getLocation());
        }
        ArrayTypeSymbol arrayTypeSymbol = (ArrayTypeSymbol) container.typeSymbol;
        if (arrayTypeSymbol.getDim() - 1 == 0) {
            returnType = new ReturnType(arrayTypeSymbol.getNonArrayTypeSymbol(), LVAL);
        } else {
            returnType = new ReturnType(ArrayTypeSymbol.builder(arrayTypeSymbol.getNonArrayTypeSymbol().getName(), arrayTypeSymbol.getDim() - 1), LVAL);
        }
    }

    @Override
    public void visit(IfNode node) {
        node.getCondition().accept(this);
        ReturnType cond = returnType;
        if (cond.typeSymbol != boolTypeSymbol) {
            throw new RuntimeException("ERROR: (Type Checking) If condition must be a bool type, not "
                    + cond.typeSymbol.getName() + node.getLocation().getLocation());
        }
        node.getBody().accept(this);
        returnType = null;
    }

    @Override
    public void visit(LoopNode node) {
        getCurrentSymbolTable(node.getBody());
        if (node.getVarDecl() != null) {
            node.getVarDecl().accept(this);
        }
        if (node.getCondition() != null) {
            node.getCondition().accept(this);
        }
        if (node.getStep() != null) {
            node.getStep().accept(this);
        }
        node.getBody().accept(this);
        returnType = null;
    }

    @Override
    public void visit(BreakNode node) {
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
        returnType = null;
    }
}
