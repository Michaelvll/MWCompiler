package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.*;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.symbols.*;
import mwcompiler.utility.ExprOps;

import static mwcompiler.utility.ExprOps.*;

public class IRBuilder implements AstVisitor<Operand> {
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;
    private ProgramIR programIR = new ProgramIR();

    private Boolean isGetReg = false;
    private Boolean isParamDecl = false;
    private final IntLiteral ONE_LITERAL = new IntLiteral(1);


    public ProgramIR build(Node node) {
        visit(node);
        return programIR;
    }

    private Operand visit(Node node) {
        return node.accept(this);
    }

    private void getCurrentSymbolTable(BlockNode block) {
        currentSymbolTable = block.getCurrentSymbolTable();
    }

    private void getCurrentBasicBlock(Function function) {
        currentBasicBlock = function.getStartBasicBlock();
    }


    @Override
    public Operand visit(ProgramNode node) {
        visit(node.getBlock());
        return null;
    }

    @Override
    public Operand visit(BlockNode node) {
        currentSymbolTable = node.getCurrentSymbolTable();
        node.getStatements().forEach(this::visit);
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
        return null;
    }

    @Override
    public Operand visit(VariableDeclNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getVarSymbol());
        VirtualRegister reg = new VirtualRegister(node.getVarSymbol(), currentSymbolTable);
        symbolInfo.setReg(reg);
        if (node.getInit() != null) {
            Operand value = visit(node.getInit());
            if (value instanceof IntLiteral) {
                currentBasicBlock.pushBack(new MoveInst(reg, value));
            } else {
                assert currentBasicBlock.back() instanceof AssignInst;
                ((AssignInst) currentBasicBlock.back()).setDst(reg);
            }
        } else {
            if (!isParamDecl) {
                IntLiteral val = new IntLiteral(0);
                currentBasicBlock.pushBack(new MoveInst(reg, val));
            }
        }
        return reg;
    }

    private Function getFunction(InstanceSymbol functionSymbol, FunctionTypeSymbol functionTypeSymbol) {
        Function function = programIR.getFunction(functionSymbol);
        if (function == null) {
            function = new Function(functionSymbol, functionTypeSymbol);
            programIR.putFunction(functionSymbol, function);
        }
        return function;
    }

    @Override
    public Operand visit(FunctionDeclNode node) {
        InstanceSymbol functionSymbol = node.getInstanceSymbol();
        FunctionTypeSymbol functionTypeSymbol = node.getFunctionTypeSymbol();
        Function function = getFunction(functionSymbol, functionTypeSymbol);
        getCurrentSymbolTable(node.getBody());
        getCurrentBasicBlock(function);
        function.pushBack(currentBasicBlock);

        isParamDecl = true;
        node.getParamList().forEach(param -> function.AddParam((VirtualRegister) visit(param)));
        isParamDecl = false;

        visit(node.getBody());
        if (!(currentBasicBlock.back() instanceof ReturnInst)) {
            if (function.getFunctionTypeSymbol().getReturnType() == NonArrayTypeSymbol.VOID_TYPE_SYMBOL) {
                currentBasicBlock.pushBack(new ReturnInst(null));
            } else {
                currentBasicBlock.pushBack(new ReturnInst(new IntLiteral(0)));
            }
        }
        return null;
    }

    @Override
    public Operand visit(ClassDeclNode node) {
        return null;
    }

    private IntLiteral intLiteralCalc(Operand left, ExprOps op, Operand right) {
        Integer x = ((IntLiteral) left).getVal();
        Integer y = ((IntLiteral) right).getVal();
        switch (op) {
            case ADD: return new IntLiteral(x + y);
            case SUB: return new IntLiteral(x - y);
            case MUL: return new IntLiteral(x * y);
            case DIV: return new IntLiteral(x / y);
            case MOD: return new IntLiteral(x % y);
            case EQ: return new IntLiteral(x.equals(y) ? 1 : 0);
            case NEQ: return new IntLiteral(!x.equals(y) ? 1 : 0);
            case GTE: return new IntLiteral(x >= y ? 1 : 0);
            case GT: return new IntLiteral(x > y ? 1 : 0);
            case LTE: return new IntLiteral(x <= y ? 1 : 0);
            case LT: return new IntLiteral(x < y ? 1 : 0);
            case OR: case BITOR: return new IntLiteral(x | y);
            case AND: case BITAND: return new IntLiteral(x & y);
            case BITXOR: return new IntLiteral(x ^ y);
            default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for IntLiteral");
        }
    }

    private Operand visitAssign(BinaryExprNode node) {
        Operand left = getReg(node.getLeft());
        Operand right = visit(node.getRight());
        if (right instanceof IntLiteral) {
            currentBasicBlock.pushBack(new MoveInst((Register) left, right));
        } else {
            ((AssignInst) currentBasicBlock.back()).setDst((Register) left);
            ((Register) left).setVal(null);

        }
        return left;
    }

    private Operand visitArithComp(BinaryExprNode node) {
        ExprOps op = node.getOp();
        Operand left = visit(node.getLeft());
        Operand right = visit(node.getRight());
        if (left instanceof IntLiteral && right instanceof IntLiteral) {
            return intLiteralCalc(left, op, right);
        }
        VirtualRegister dst = VirtualRegister.builder(op.toString());
        currentBasicBlock.pushBack(new BinaryExprInst(dst, left, op, right));
        return dst;
    }

    private Operand visitLogic(BinaryExprNode node) {
        Function function = currentBasicBlock.getParentFunction();
        ExprOps op = node.getOp();
        Operand left = visit(node.getLeft());
        BasicBlock next = new BasicBlock(function, "next");
        switch (op) {
            case AND:
                if (left instanceof IntLiteral) {
                    if (((IntLiteral) left).getVal() == 0) return left;
                    return visit(node.getRight());
                }
                BasicBlock valTrue = new BasicBlock(function, "val_true");
                currentBasicBlock.pushBack(new CondJumpInst(left, valTrue, next));
                setCurrentBasicBlock(valTrue);
                break;
            case OR:
                if (left instanceof IntLiteral) {
                    if (((IntLiteral) left).getVal() == 1) return left;
                    return visit(node.getRight());
                }
                BasicBlock valFalse = new BasicBlock(function, "val_false");
                currentBasicBlock.pushBack(new CondJumpInst(left, valFalse, next));
                setCurrentBasicBlock(valFalse);
                break;
        }
        Operand right = visit(node.getRight());
        currentBasicBlock.pushBack(new MoveInst((VirtualRegister) left, right));
        currentBasicBlock.pushBack(new DirectJumpInst(next));
        setCurrentBasicBlock(next);
        return left;
    }

    @Override
    public Operand visit(BinaryExprNode node) {
        ExprOps op = node.getOp();
        switch (op) {
            case ASSIGN:
                return visitAssign(node);
            case ADD: case SUB: case MUL: case DIV: case MOD: case EQ: case NEQ: case GT: case GTE: case LT: case LTE:
            case BITAND: case BITOR: case BITXOR:
                return visitArithComp(node);
            case AND: case OR:
                return visitLogic(node);
            default:
                throw new RuntimeException("Compiler Bug: (IR building) Unprocessed Binary operation");
        }
    }


    @Override
    public Operand visit(UnaryExprNode node) {
        ExprOps op = node.getOp();
        Operand src;
        VirtualRegister dst = VirtualRegister.builder(op.toString());
        switch (op) {
            case ADD:
                return visit(node.getExpr());
            case SUB:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(-((IntLiteral) src).getVal());
                currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src));
                return dst;
            case BITNOT:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(~((IntLiteral) src).getVal());
                currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src));
                return dst;
            case NOT:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(((IntLiteral) src).getVal() == 1 ? 0 : 1);
                currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src));
                return dst;
            case DEC: case INC: case DEC_SUFF: case INC_SUFF:
                src = getReg(node.getExpr());
                dst = (VirtualRegister) src;
                Boolean isINC = op == INC || op == INC_SUFF;
                Boolean isSUF = op == INC_SUFF || op == DEC_SUFF;
                IntLiteral val = ((Register) src).getVal();
                if (val != null) {
                    IntLiteral newVal = new IntLiteral(isINC ? val.getVal() + 1 : val.getVal() - 1);
                    currentBasicBlock.pushBack(new MoveInst(dst, newVal));
                    return isSUF ? val : newVal;
                }
                VirtualRegister ret = (VirtualRegister) src;
                if (isSUF) {
                    ret = VirtualRegister.builder("SUF");
                    currentBasicBlock.pushBack(new MoveInst(ret, src));
                }
                currentBasicBlock.pushBack(new BinaryExprInst(dst, src, isINC ? ADD : SUB, ONE_LITERAL));
                return ret;
            default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for unary expression");
        }

    }

    private Operand getReg(Node node) {
        isGetReg = true;
        Operand reg = visit(node);
        isGetReg = false;
        return reg;
    }

    @Override
    public Operand visit(IdentifierExprNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findAll(node.getInstanceSymbol());
        Register reg = symbolInfo.getReg();
        IntLiteral val = reg.getVal();
        return (isGetReg || val == null) ? reg : val;
    }

    @Override
    public Operand visit(NewExprNode node) {
        return null;
    }

    @Override
    public Operand visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public Operand visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public Operand visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public Operand visit(IntLiteralNode node) {
        return new IntLiteral(node.getVal());
    }

    @Override
    public Operand visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public Operand visit(FunctionCallNode node) {
        ExprNode caller = node.getCaller();
        Function function;
        if (caller instanceof IdentifierExprNode) {
            InstanceSymbol functionSymbol = ((IdentifierExprNode) caller).getInstanceSymbol();
            SymbolInfo functionInfo = currentSymbolTable.findAll(functionSymbol);
            FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) functionInfo.getTypeSymbol();
            function = getFunction(functionSymbol, functionTypeSymbol);
        } else {
            //TODO
        }
        //TODO
        return null;
    }

    @Override
    public Operand visit(DotMemberNode node) {
        return null;
    }

    @Override
    public Operand visit(BrackMemberNode node) {
        return null;
    }

    private void setCurrentBasicBlock(BasicBlock block) {
        currentBasicBlock.getParentFunction().pushBack(block);
        currentBasicBlock = block;
    }

    @Override
    public Operand visit(IfNode node) {
        Function function = currentBasicBlock.getParentFunction();
        Operand cond = node.getCondition() == null ? ONE_LITERAL : visit(node.getCondition());
        if (cond instanceof IntLiteral) {
            if (((IntLiteral) cond).getVal() == 1) visit(node.getBody());
            else if (node.getElseNode() != null) visit(node.getElseNode());
        } else {
            BasicBlock ifTrue = new BasicBlock(function, "if_then");
            BasicBlock ifFalse = new BasicBlock(function, "if_else");
            BasicBlock next = new BasicBlock(function);
            Boolean haveElse = node.getElseNode() != null;
            CondJumpInst condJump = new CondJumpInst(cond, ifTrue, haveElse ? ifFalse : next);
            currentBasicBlock.pushBack(condJump);
            setCurrentBasicBlock(ifTrue);
            visit(node.getBody());
            if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(next));
            if (haveElse) {
                setCurrentBasicBlock(ifFalse);
                visit(node.getElseNode());
                if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(next));
            }
            setCurrentBasicBlock(next);
        }
        return null;
    }

    @Override
    public Operand visit(LoopNode node) {
        return null;
    }

    @Override
    public Operand visit(BreakNode node) {
        return null;
    }

    @Override
    public Operand visit(ReturnNode node) {
        Operand retVal = null;
        if (node.getReturnVal() != null) {
            retVal = visit(node.getReturnVal());
        }
        currentBasicBlock.pushBack(new ReturnInst(retVal));
        return null;
    }

    @Override
    public Operand visit(ContinueNode node) {
        return null;
    }

    @Override
    public Operand visit(ConstructorCallNode node) {
        return null;
    }

}
