package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.*;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.SymbolInfo;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.ExprOps;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static mwcompiler.utility.ExprOps.*;

public class IRBuilder implements AstVisitor<Operand> {
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;
    private ProgramIR programIR = new ProgramIR();

    private Boolean isGetReg = false;
    private Boolean isParamDecl = false;
    private final IntLiteral ONE_LITERAL = new IntLiteral(1);
    private final IntLiteral ZERO_LITERAL = new IntLiteral(0);

    private Integer valTag = 0;

    private void newValTag() {
        ++valTag;
    }

    private void popValTag() {
        --valTag;
    }


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
        currentBasicBlock = new BasicBlock(function, currentSymbolTable);
        function.pushBack(currentBasicBlock);
    }

    private void setCurrentBasicBlock(BasicBlock block) {
        currentBasicBlock.getParentFunction().pushBack(block);
        currentBasicBlock = block;
    }


    @Override
    public Operand visit(ProgramNode node) {
        Function.builtinFunctions.forEach(programIR::putFunction);
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
                currentBasicBlock.pushBack(new MoveInst(reg, value), valTag);
            } else {
                assert currentBasicBlock.back() instanceof AssignInst;
                ((AssignInst) currentBasicBlock.back()).setDst(reg);
            }
        } else {
            if (!isParamDecl) {
                currentBasicBlock.pushBack(new MoveInst(reg, ZERO_LITERAL), valTag);
            }
        }
        return reg;
    }

    private Function getFunction(FunctionSymbol functionSymbol) {
        Function function = programIR.getFunction(functionSymbol);
        if (function == null) {
            function = new Function(functionSymbol);
            programIR.putFunction(function);
        }
        return function;
    }

    @Override
    public Operand visit(FunctionDeclNode node) {
        newValTag();
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        Function function = getFunction(functionSymbol);
        getCurrentSymbolTable(node.getBody());
        getCurrentBasicBlock(function);

        isParamDecl = true;
        node.getParamList().forEach(param -> function.AddParam((VirtualRegister) visit(param)));
        isParamDecl = false;

        visit(node.getBody());
        if (!(currentBasicBlock.back() instanceof ReturnInst)) {
            if (function.needReturn()) {
                currentBasicBlock.pushBack(new ReturnInst(null));
            } else {
                currentBasicBlock.pushBack(new ReturnInst(ZERO_LITERAL));
            }
        }
        popValTag();
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
            currentBasicBlock.pushBack(new MoveInst((Register) left, right), valTag);
        } else {
            ((AssignInst) currentBasicBlock.back()).setDst((Register) left);
            ((Register) left).setVal(null, valTag);
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
        currentBasicBlock.pushBack(new BinaryExprInst(dst, left, op, right), valTag);
        return dst;
    }

    private Operand visitLogic(BinaryExprNode node) {
        Function function = currentBasicBlock.getParentFunction();
        ExprOps op = node.getOp();
        Operand left = visit(node.getLeft());
        BasicBlock next = new BasicBlock(function, currentSymbolTable, "next");
        switch (op) {
            case AND:
                if (left instanceof IntLiteral) {
                    if (((IntLiteral) left).getVal() == 0) return left;
                    return visit(node.getRight());
                }
                BasicBlock valTrue = new BasicBlock(function, null, "val_true");//TODO: is null good enough?
                currentBasicBlock.pushBack(new CondJumpInst(left, valTrue, next));
                setCurrentBasicBlock(valTrue);
                break;
            case OR:
                if (left instanceof IntLiteral) {
                    if (((IntLiteral) left).getVal() == 1) return left;
                    return visit(node.getRight());
                }
                BasicBlock valFalse = new BasicBlock(function, null, "val_false");//TODO: is null good enough?
                currentBasicBlock.pushBack(new CondJumpInst(left, valFalse, next));
                setCurrentBasicBlock(valFalse);
                break;
        }
        Operand right = visit(node.getRight());
        currentBasicBlock.pushBack(new MoveInst((VirtualRegister) left, right), valTag);
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
                currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src), valTag);
                return dst;
            case BITNOT:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(~((IntLiteral) src).getVal());
                currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src), valTag);
                return dst;
            case NOT:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(((IntLiteral) src).getVal() == 1 ? 0 : 1);
                currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src), valTag);
                return dst;
            case DEC: case INC: case DEC_SUFF: case INC_SUFF:
                src = getReg(node.getExpr());
                dst = (VirtualRegister) src;
                Boolean isINC = op == INC || op == INC_SUFF;
                Boolean isSUF = op == INC_SUFF || op == DEC_SUFF;
                IntLiteral val = ((Register) src).getVal(valTag);
                if (val != null) {
                    IntLiteral newVal = new IntLiteral(isINC ? val.getVal() + 1 : val.getVal() - 1);
                    currentBasicBlock.pushBack(new MoveInst(dst, newVal), valTag);
                    return isSUF ? val : newVal;
                }
                VirtualRegister ret = (VirtualRegister) src;
                if (isSUF) {
                    ret = VirtualRegister.builder("SUF");
                    currentBasicBlock.pushBack(new MoveInst(ret, src), valTag);
                }
                currentBasicBlock.pushBack(new BinaryExprInst(dst, src, isINC ? ADD : SUB, ONE_LITERAL), valTag);
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

    private SymbolInfo findReg(SymbolTable symbolTable, InstanceSymbol instanceSymbol) {
        SymbolInfo symbolInfo = symbolTable.findIn(instanceSymbol);
        if (symbolInfo != null && symbolInfo.getReg() != null) return symbolInfo;
        return findReg(symbolTable.getOuterSymbolTable(), instanceSymbol);
    }

    @Override
    public Operand visit(IdentifierExprNode node) {
        SymbolInfo symbolInfo = findReg(currentSymbolTable, node.getInstanceSymbol());
        Register reg = symbolInfo.getReg();
        IntLiteral val = reg.getVal(valTag);
        return (isGetReg || val == null) ? reg : val;
    }

    @Override
    public Operand visit(NewExprNode node) {
        return null;
    }


    @Override
    public Operand visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public Operand visit(FunctionCallNode node) {
        ExprNode caller = node.getCaller();
        Function function = null;
        if (caller instanceof IdentifierExprNode) {
            // TODO built in function
            InstanceSymbol instanceSymbol = ((IdentifierExprNode) caller).getInstanceSymbol();
            SymbolInfo functionInfo = currentSymbolTable.findAll(instanceSymbol);
            FunctionSymbol functionSymbol = (FunctionSymbol) functionInfo.getTypeSymbol();
            function = getFunction(functionSymbol);
        } else {
            //TODO for class function
        }
        List<Operand> args = new ArrayList<>();
        node.getArgs().forEach(arg -> args.add(visit(arg)));
        VirtualRegister dst = VirtualRegister.builder("call_ret_tmp");
        currentBasicBlock.pushBack(new FunctionCallInst(function, args, dst), valTag);
        return dst;
    }

    @Override
    public Operand visit(ConstructorCallNode node) {
        return null;
    }


    @Override
    public Operand visit(IfNode node) {
        Function function = currentBasicBlock.getParentFunction();
        Operand cond = node.getCondition() == null ? ONE_LITERAL : visit(node.getCondition());
        if (cond instanceof IntLiteral) {
            if (((IntLiteral) cond).getVal() == 1) visit(node.getBody());
            else if (node.getElseNode() != null) visit(node.getElseNode());
        } else {
            Boolean haveElse = node.getElseNode() != null;
            BasicBlock ifTrue = new BasicBlock(function, node.getBody().getCurrentSymbolTable(), "if_then"); //TODO
            BasicBlock ifFalse = new BasicBlock(function, haveElse ? node.getElseNode().getBody().getCurrentSymbolTable() : null, "if_else");
            BasicBlock next = new BasicBlock(function, currentSymbolTable);
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

    private Stack<BasicBlock> loopCondStack = new Stack<>();
    private Stack<BasicBlock> loopEndStack = new Stack<>();

    @Override
    public Operand visit(LoopNode node) {
        Function function = currentBasicBlock.getParentFunction();
        BasicBlock loopEnd = new BasicBlock(function, currentSymbolTable, "loop_end");
        BasicBlock loopCond = new BasicBlock(function, new SymbolTable(currentSymbolTable), "loop_cond");
        BasicBlock loopBegin = new BasicBlock(function, node.getBody().getCurrentSymbolTable(), "loop_begin");
        if (node.getVarInit() != null) {
            visit(node.getVarInit());
            if (node.getCondition() != null) {
                Operand cond1 = visit(node.getCondition());
                if (cond1 instanceof IntLiteral) {
                    if (((IntLiteral) cond1).getVal() == 0) {
                        System.err.println("Never go into the loop " + node.getStartLocation().toString());
                        return null; // Never go into the loop
                    }
                    currentBasicBlock.pushBack(new DirectJumpInst(loopBegin));
                } else {
                    currentBasicBlock.pushBack(new CondJumpInst(cond1, loopBegin, loopEnd));
                }
            }
        }
        newValTag();

        loopCondStack.push(loopCond);
        loopEndStack.push(loopEnd);
        setCurrentBasicBlock(loopBegin);
        visit(node.getBody());
        currentBasicBlock.pushBack(new DirectJumpInst(loopCond));

        setCurrentBasicBlock(loopCond);
        if (node.getStep() != null)
            visit(node.getStep());
        Operand cond2 = visit(node.getCondition());
        currentBasicBlock.pushBack(new CondJumpInst(cond2, loopBegin, loopEnd));
        loopCondStack.pop();
        loopEndStack.pop();

        setCurrentBasicBlock(loopEnd);
        popValTag();
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


    @Override
    public Operand visit(BreakNode node) {
        currentBasicBlock.pushBack(new DirectJumpInst(loopEndStack.peek()));
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
        currentBasicBlock.pushBack(new DirectJumpInst(loopCondStack.peek()));
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
        return node.getVal() ? ONE_LITERAL : ZERO_LITERAL;
    }

    @Override
    public Operand visit(IntLiteralNode node) {
        return new IntLiteral(node.getVal());
    }


}
