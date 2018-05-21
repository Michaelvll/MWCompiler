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
import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.*;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.symbols.*;
import mwcompiler.utility.ExprOps;

import java.util.*;

import static mwcompiler.utility.ExprOps.*;

public class IRBuilder implements AstVisitor<Operand> {
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;
    private ProgramIR programIR = new ProgramIR();

    private Boolean isGetReg = false;
    private Boolean isParamDecl = false;
    private final IntLiteral ONE_LITERAL = new IntLiteral(1);
    private final IntLiteral ZERO_LITERAL = new IntLiteral(0);
    private final IntLiteral PTR_SIZE = new IntLiteral(8);
    private final IntLiteral LENGTH_SIZE = new IntLiteral(8);
    private Integer valTag = 0;

    public ProgramIR build(Node node) {
        visit(node);
        return programIR;
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
            resetCurrentBasicBlockBackDst(reg, value);
        } else {
            // init un-init variable with 0
//            if (!isParamDecl) {
//                currentBasicBlock.pushBack(new MoveInst(reg, ZERO_LITERAL), valTag);
//            }
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
        setCurrentEnv(function, node.getBody().getCurrentSymbolTable());

        isParamDecl = true;
        node.getParamList().forEach(param -> function.AddParam((VirtualRegister) visit(param)));
        isParamDecl = false;

        visit(node.getBody());
        if (!(currentBasicBlock.back() instanceof ReturnInst)) {
            if (function.needReturn()) {
                currentBasicBlock.pushBack(new ReturnInst(ZERO_LITERAL));
            } else {
                currentBasicBlock.pushBack(new ReturnInst(null));
            }
        }
        popValTag();
        return null;
    }

    @Override
    public Operand visit(ClassDeclNode node) {
        return null;
    }

    private Literal literalCalc(Operand left, ExprOps op, Operand right) {
        if (left instanceof IntLiteral) {
            Integer x = ((IntLiteral) left).getVal();
            Integer y = ((IntLiteral) right).getVal();
            switch (op) {
                case ADD: return new IntLiteral(x + y);
                case SUB: return new IntLiteral(x - y);
                case MUL: return new IntLiteral(x * y);
                case DIV: return new IntLiteral(x / y);
                case MOD: return new IntLiteral(x % y);
                case EQ: return x.equals(y) ? ONE_LITERAL : ZERO_LITERAL;
                case NEQ: return !x.equals(y) ? ONE_LITERAL : ZERO_LITERAL;
                case GTE: return x >= y ? ONE_LITERAL : ZERO_LITERAL;
                case GT: return x > y ? ONE_LITERAL : ZERO_LITERAL;
                case LTE: return x <= y ? ONE_LITERAL : ZERO_LITERAL;
                case LT: return x < y ? ONE_LITERAL : ZERO_LITERAL;
                case OR: case BITOR: return new IntLiteral(x | y);
                case AND: case BITAND: return new IntLiteral(x & y);
                case BITXOR: return new IntLiteral(x ^ y);
                default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for IntLiteral");
            }
        } else {
            String x = ((StringLiteral) left).getVal();
            String y = ((StringLiteral) right).getVal();
            switch (op) {
                case ADD: return stringLiteralBuilder(x + y);
                case EQ: return x.equals(y) ? ONE_LITERAL : ZERO_LITERAL;
                case NEQ: return !x.equals(y) ? ONE_LITERAL : ZERO_LITERAL;
                case GTE: return x.compareTo(y) >= 0 ? ONE_LITERAL : ZERO_LITERAL;
                case GT: return x.compareTo(y) > 0 ? ONE_LITERAL : ZERO_LITERAL;
                case LTE: return x.compareTo(y) <= 0 ? ONE_LITERAL : ZERO_LITERAL;
                case LT: return x.compareTo(y) < 0 ? ONE_LITERAL : ZERO_LITERAL;
                default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for StringLiteral");
            }
        }
    }

    private Literal literalCalc(BinaryExprInst binaryExprInst) {
        return literalCalc(binaryExprInst.getLeft(), binaryExprInst.getOp(), binaryExprInst.getRight());
    }

    private Operand visitAssign(BinaryExprNode node) {
        Register left = getReg(node.getLeft());
        Operand right = visit(node.getRight());
        resetCurrentBasicBlockBackDst(left, right);
        return left;
    }

    private Operand visitArithComp(BinaryExprNode node) {
        ExprOps op = node.getOp();
        Operand left = visit(node.getLeft());
        Operand right = visit(node.getRight());
        if (left instanceof Literal && right instanceof Literal) {
            return literalCalc(left, op, right);
        }
        VirtualRegister dst = VirtualRegister.builder(op.toString());
        if (node.getType() != NonArrayTypeSymbol.STRING_TYPE_SYMBOL) {
            currentBasicBlock.pushBack(new BinaryExprInst(dst, left, op, right), valTag);
            return dst;
        }
        List<Operand> args = new ArrayList<>(Arrays.asList(left, right));
        switch (op) {
            case ADD: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_ADD, args, dst), valTag);
                break;
            case GT: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_GT, args, dst), valTag);
                break;
            case LT: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_LT, args, dst), valTag);
                break;
            case GTE: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_GTE, args, dst), valTag);
                break;
            case LTE: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_LTE, args, dst), valTag);
                break;
            case EQ: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_EQ, args, dst), valTag);
                break;
            case NEQ: currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_NEQ, args, dst), valTag);
                break;
            default: throw new RuntimeException("Compiler Bug: (IR building) Unsupported operation for string" + node.getStartLocation().toString());
        }
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
                setCurrentEnv(valTrue);
                break;
            case OR:
                if (left instanceof IntLiteral) {
                    if (((IntLiteral) left).getVal() == 1) return left;
                    return visit(node.getRight());
                }
                BasicBlock valFalse = new BasicBlock(function, null, "val_false");//TODO: is null good enough?
                currentBasicBlock.pushBack(new CondJumpInst(left, valFalse, next));
                setCurrentEnv(valFalse);
                break;
        }
        Operand right = visit(node.getRight());
        currentBasicBlock.pushBack(new MoveInst((VirtualRegister) left, right), valTag);
        currentBasicBlock.pushBack(new DirectJumpInst(next));
        setCurrentEnv(next);
        return left;
    }

    @Override
    public Operand visit(BinaryExprNode node) {
        ExprOps op = node.getOp();
        switch (op) {
            case ASSIGN:
                return visitAssign(node);
            case ADD: case SUB: case MUL: case DIV: case MOD: case EQ: case NEQ: case GT: case GTE: case LT:
            case LTE: case BITAND: case BITOR: case BITXOR:
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
                IntLiteral val = (IntLiteral) ((Register) src).getVal(valTag);
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

    private Register getReg(Node node) {
        isGetReg = true;
        Operand reg = visit(node);
        isGetReg = false;
        return (Register) reg;
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
        Literal val = currentBasicBlock.getKnownReg(reg, valTag);
        if (isGetReg || val == null) {
            return reg;
        }
        return val;
    }

    private void pushBackBinary(BinaryExprInst inst) {
        if (inst.getLeft() instanceof Literal && inst.getRight() instanceof Literal) {
            currentBasicBlock.pushBack(new MoveInst(inst.getDst(), literalCalc(inst)), valTag);
        } else currentBasicBlock.pushBack(inst, valTag);
    }

    private Operand visitCreatClass(NewExprNode node) {
        //TODO: create class
        return null;
    }

    private Operand visitCreateArray(NewExprNode node, Integer index) {
        VirtualRegister sizeReg = VirtualRegister.builder("size_tmp");
        VirtualRegister baseReg = VirtualRegister.builder("array_base_tmp");

        Operand size = visit(node.getDimArgs().get(index));
        resetCurrentBasicBlockBackDst(sizeReg, size);
        pushBackBinary(new BinaryExprInst(sizeReg, sizeReg, MUL, PTR_SIZE));
        pushBackBinary(new BinaryExprInst(sizeReg, sizeReg, ADD, LENGTH_SIZE));
        currentBasicBlock.pushBack(new FunctionCallInst(Function.MALLOC, new ArrayList<>(Collections.singletonList(sizeReg)), baseReg), valTag);
        currentBasicBlock.pushBack(new MoveInst(new Address(baseReg, null, 0, 0), size), valTag);


        if (index < node.getDimArgs().size() - 1 && node.getDimArgs().get(index + 1) == null) { //TODO: new for single
            return baseReg;
        }
        Boolean createClass = index == node.getDimArgs().size() - 1 && !node.getCreateType().isPrimitiveType();
        // iterate for each subscript
        VirtualRegister indexReg = VirtualRegister.builder("index_tmp");
        VirtualRegister cntReg = VirtualRegister.builder("counter");
        currentBasicBlock.pushBack(new MoveInst(indexReg, ZERO_LITERAL), valTag);
        currentBasicBlock.pushBack(new MoveInst(cntReg, size), valTag);
        BasicBlock creatorLoopBegin = new BasicBlock(currentBasicBlock.getParentFunction(), null, "creator_loop");
        BasicBlock next = new BasicBlock(currentBasicBlock.getParentFunction(), currentSymbolTable, "creator_loop_end");
        currentBasicBlock.pushBack(new DirectJumpInst(creatorLoopBegin));
        setCurrentEnv(creatorLoopBegin);
        newValTag();
        Operand created;
        if (createClass) created = visitCreatClass(node);
        else created = visitCreateArray(node, index + 1);
        currentBasicBlock.pushBack(new MoveInst(new Address(baseReg, indexReg, PTR_SIZE, LENGTH_SIZE), created), valTag);
        pushBackBinary(new BinaryExprInst(cntReg, cntReg, SUB, ONE_LITERAL));
        currentBasicBlock.pushBack(new CondJumpInst(cntReg, creatorLoopBegin, next));
        popValTag();
        setCurrentEnv(next);

        return baseReg;
    }

    @Override
    public Operand visit(NewExprNode node) {
        if (node.getCreateType() instanceof ArrayTypeSymbol) {

        } else {

        }
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
            setCurrentEnv(ifTrue);
            visit(node.getBody());
            if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(next));
            if (haveElse) {
                setCurrentEnv(ifFalse);
                visit(node.getElseNode());
                if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(next));
            }
            setCurrentEnv(next);
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
        setCurrentEnv(loopBegin);
        visit(node.getBody());
        currentBasicBlock.pushBack(new DirectJumpInst(loopCond));

        setCurrentEnv(loopCond);
        if (node.getStep() != null)
            visit(node.getStep());
        Operand cond2 = visit(node.getCondition());
        currentBasicBlock.pushBack(new CondJumpInst(cond2, loopBegin, loopEnd));
        loopCondStack.pop();
        loopEndStack.pop();

        setCurrentEnv(loopEnd);
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
        return stringLiteralBuilder(node.getVal());
    }

    @Override
    public Operand visit(BoolLiteralNode node) {
        return node.getVal() ? ONE_LITERAL : ZERO_LITERAL;
    }

    @Override
    public Operand visit(IntLiteralNode node) {
        return new IntLiteral(node.getVal());
    }


    private void newValTag() {
        ++valTag;
    }

    private void popValTag() {
        --valTag;
    }

    private Operand visit(Node node) {
        return node.accept(this);
    }

    private void setCurrentEnv(BasicBlock block) {
        currentBasicBlock.getParentFunction().pushBack(block);
        currentSymbolTable = block.getCurrentSymbolTable();
        currentBasicBlock = block;
    }

    private void setCurrentEnv(Function function, SymbolTable symbolTable) {
        currentSymbolTable = symbolTable;
        currentBasicBlock = new BasicBlock(function, currentSymbolTable);
        function.pushBack(currentBasicBlock);
    }

    private void resetCurrentBasicBlockBackDst(Register dst, Operand origin) {
        if (origin instanceof Literal) currentBasicBlock.pushBack(new MoveInst(dst, origin), valTag);
        else {
            assert currentBasicBlock.popBack() instanceof AssignInst;
            AssignInst assignInst = (AssignInst) currentBasicBlock.popBack();
            assignInst.setDst(dst);
            currentBasicBlock.pushBack(assignInst, valTag);
        }
    }

    private StringLiteral stringLiteralBuilder(String val) {
        StringLiteral search = programIR.getStringLiteral(val);
        if (search == null) {
            search = new StringLiteral(val);
            programIR.putStringLiteral(val, search);
        }
        return search;
    }


}
