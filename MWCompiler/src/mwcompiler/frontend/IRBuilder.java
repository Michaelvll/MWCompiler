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
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.*;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.LiteralProcess;
import mwcompiler.symbols.*;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.ExprOps;

import java.util.*;

import static mwcompiler.ir.operands.IntLiteral.ONE_LITERAL;
import static mwcompiler.ir.operands.IntLiteral.ZERO_LITERAL;
import static mwcompiler.symbols.BaseTypeSymbol.INT_TYPE_SYMBOL;
import static mwcompiler.utility.ExprOps.*;

public class IRBuilder implements AstVisitor<Operand> {
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;
    private ProgramIR programIR = new ProgramIR();

    private boolean isGetMutableOperand = false;
    private boolean isParamDecl = false;
    private boolean isGlobal = false;
    private LinkedList<BasicBlock> globalReverseInitBlock = new LinkedList<>();
    private CompilerOptions options;

    private int valTag = 0;

    public IRBuilder(CompilerOptions options) {
        this.options = options;
        PTR_SIZE = new IntLiteral(options.PTR_SIZE);
        LENGTH_SIZE = new IntLiteral(options.LENGTH_SIZE);
        LiteralProcess.programIR = programIR;
    }

    public ProgramIR build(Node node) {
        visit(node);
//        programIR.updateRecursiveCall();
        return programIR;
    }

    private void visitFunction(BlockNode node) {
        SymbolTable prevSymbolTable = currentSymbolTable;
        currentSymbolTable = node.getCurrentSymbolTable();
        for (Node decl : node.getStatements()) {
            if (decl instanceof FunctionDeclNode) {
                visit(decl);
            }
        }
        currentSymbolTable = prevSymbolTable;
    }

    private void visitGlobalClassVariable(BlockNode node) {
        SymbolTable prevSymbolTable = currentSymbolTable;
        currentSymbolTable = node.getCurrentSymbolTable();
        // Visit variable declaration of class and get class size
        for (Node decl : node.getStatements()) {
            if (decl instanceof ClassDeclNode) {
                getClassMemberVariableOffset((ClassDeclNode) decl, options.PTR_SIZE);
            }
        }
        for (Node decl : node.getStatements()) {
            if (decl instanceof ClassDeclNode) {
                ClassDeclNode classNode = (ClassDeclNode) decl;
                BlockNode block = classNode.getBody();
                classDeclSymbol = classNode.getClassSymbol();
                inClassFunc = true;
                visitFunction(block);
                inClassFunc = false;
                classDeclSymbol = null;
                classDeclThisReg = null;
            } else if (decl instanceof VariableDeclNode) {
                setCurrentEnv(globalReverseInitBlock.getFirst());
                isGlobal = true;
                visit(decl);
                isGlobal = false;
            }
        }
        currentSymbolTable = prevSymbolTable;
    }

    private void getClassMemberVariableOffset(ClassDeclNode node, int PTR_SIZE) {
        int size = 0;
        SymbolTable currentSymbolTable = node.getBody().getCurrentSymbolTable();
        for (Node decl : node.getBody().getStatements()) {
            if (decl instanceof VariableDeclNode) {
                SymbolInfo symbolInfo = currentSymbolTable.findIn(((VariableDeclNode) decl).getVarSymbol());
                symbolInfo.setOffset(size);
                size += PTR_SIZE;
            }
        }
        node.getClassSymbol().setSize(size);
    }

    @Override
    public Operand visit(ProgramNode node) {
        newValTag();
        // Add built in functions into programIR
        Function.builtinFunctions.forEach(programIR::putFunction);

        BlockNode block = node.getBlock();
        Function mainFunction = getFunction(FunctionSymbol.MAIN);
        BasicBlock global = new BasicBlock(null, node.getBlock().getCurrentSymbolTable(), "global", valTag);
        setCurrentEnv(global);
        visitGlobalClassVariable(block);
        visitFunction(block);
        globalReverseInitBlock.getFirst().pushBack(new DirectJumpInst(mainFunction.basicBlocks().get(0)));
        globalReverseInitBlock.forEach(mainFunction::pushFront);
        popValTag();

        return null;
    }

    @Override
    public Operand visit(BlockNode node) {
        currentSymbolTable = node.getCurrentSymbolTable();
        node.getStatements().forEach(this::visit);
        currentSymbolTable = currentSymbolTable.getParentSymbolTable();
        return null;
    }

    private BaseTypeSymbol classDeclSymbol;
    private Var classDeclThisReg;
    private boolean inClassFunc = false;

    @Override
    public Operand visit(ClassDeclNode node) {
        throw new RuntimeException("Compiler Bug: (IR building) Do not need to call visit(ClassDeclNode)");
    }

    @Override
    public Operand visit(VariableDeclNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getVarSymbol());
        TypeSymbol varSymbol = (TypeSymbol) symbolInfo.getSymbol();
        int varSize = options.PTR_SIZE;
        if (varSymbol.isPrimitiveType()) varSize = ((BaseTypeSymbol) varSymbol).getSize();
        Var reg = Var.builder(node.getVarSymbol(), currentSymbolTable, varSize);
        symbolInfo.setOperand(reg);
        if (isGlobal) {
            reg.setGlobal();
            programIR.addGlobal(reg, null);
        }
        if (node.init() != null) {
            Operand value = visit(node.init());
            if (isGlobal && value instanceof IntLiteral) programIR.addGlobal(reg, value);
            else resetCurrentBasicBlockBackDst(reg, value, node.getTypeSymbol());
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
        if (node.getInstance() == Instance.CONSTRUCTOR)
            functionSymbol.setReturnType(BaseTypeSymbol.VOID_TYPE_SYMBOL);
        Function function = getFunction(functionSymbol);
        function.setSymbolTable(node.getBody().getCurrentSymbolTable());
        setCurrentEnv(function, node.getBody().getCurrentSymbolTable());

        node.getParamList().forEach(param -> function.addParam((Var) visit(param)));
        if (inClassFunc) {
            currentSymbolTable.put(Instance.THIS, classDeclSymbol);
            SymbolInfo thisInfo = currentSymbolTable.findIn(Instance.THIS);
            classDeclThisReg = Var.builder(Instance.THIS, currentSymbolTable, options.PTR_SIZE);
            thisInfo.setOperand(classDeclThisReg);
            function.addParam(classDeclThisReg);
        }

        visit(node.getBody());
        if (!(currentBasicBlock.back() instanceof ReturnInst)) {
            if (function.needReturn()) {
                currentBasicBlock.pushBack(new ReturnInst(ZERO_LITERAL));
            } else {
                currentBasicBlock.pushBack(new ReturnInst(null));
            }
        }
        popValTag();
        function.cleanUp();
        return null;
    }


    private Operand visitAssign(BinaryExprNode node) {
        MutableOperand left = getMutableOperand(node.left());
        Operand right = visit(node.right());
//        System.err.println(node.location().irName());
        resetCurrentBasicBlockBackDst(left, right, node.left().type());
        return left;
    }

    private Operand visitArithComp(Operand left, ExprOps op, Operand right, TypeSymbol type) {
        Operand leftVal = getVal(left);
        Operand rightVal = getVal(right);
        if (leftVal instanceof Literal && rightVal instanceof Literal) {
            try {
                return LiteralProcess.calc(leftVal, op, rightVal);
            } catch (ArithmeticException e) {
                System.err.println("IR Building: get arithmetic exception: " + e.getMessage());
            }
        }
        Var dst;
        if (type != BaseTypeSymbol.STRING_TYPE_SYMBOL) {
            dst = Var.tmpBuilder(op.toString(), op.isCompare());
            return currentBasicBlock.pushBack(new BinaryExprInst(dst, leftVal, op, rightVal));
        }
        dst = Var.tmpBuilder(op.toString());
        List<Operand> args = new ArrayList<>(Arrays.asList(leftVal, rightVal));
        if (op == ADD) {
            if (left.equals(EMPTY_STRING)) return right;
            else if (right.equals(EMPTY_STRING)) return left;
            return currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_ADD, args, dst));
        }
        // String Compare
        currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_CMP, args, dst));
        Var cmp = Var.tmpBuilder("str_" + op.toString(), true);
        switch (op) {
            case GT: return currentBasicBlock.pushBack(new BinaryExprInst(cmp, dst, GT, ZERO_LITERAL));
            case LT: return currentBasicBlock.pushBack(new BinaryExprInst(cmp, dst, LT, ZERO_LITERAL));
            case GTE: return currentBasicBlock.pushBack(new BinaryExprInst(cmp, dst, GTE, ZERO_LITERAL));
            case LTE: return currentBasicBlock.pushBack(new BinaryExprInst(cmp, dst, LTE, ZERO_LITERAL));
            case EQ: return currentBasicBlock.pushBack(new BinaryExprInst(cmp, dst, EQ, ZERO_LITERAL));
            case NEQ: return currentBasicBlock.pushBack(new BinaryExprInst(cmp, dst, NEQ, ZERO_LITERAL));
            default: throw new RuntimeException("Compiler Bug: (IR building) Unsupported operation for string");
        }
    }

    private Stack<BasicBlock> successStack = new Stack<>();
    private Stack<BasicBlock> failStack = new Stack<>();

    private Operand visitLogic(BinaryExprNode node) {
        ExprOps op = node.op();
        switch (op) {
            case AND: return processLogic(node, successStack, failStack);
            case OR: return processLogic(node, failStack, successStack);
            default: throw new RuntimeException("Unknown logic op " + node.op());
        }
    }

    private Operand processLogic(BinaryExprNode node, Stack<BasicBlock> testMoreStack, Stack<BasicBlock> skipNextStack) {
        boolean outMost = testMoreStack.empty() && skipNextStack.empty();
        Function function = currentBasicBlock.parentFunction();
        ExprOps op = node.op();
        Var dst = null;
        if (outMost) dst = Var.tmpBuilder("cmp_res");
        IntLiteral initLiteral = op == AND ? ZERO_LITERAL : ONE_LITERAL;
        IntLiteral setLiteral = op == AND ? ONE_LITERAL : ZERO_LITERAL;
        if (outMost) {
            currentBasicBlock.pushBack(new MoveInst(dst, initLiteral));
            testMoreStack.add(new BasicBlock(function, currentSymbolTable, op.name() + "_set_val", valTag));
            skipNextStack.add(new BasicBlock(function, currentSymbolTable, "skip_next", valTag));
        }
        BasicBlock testMore = new BasicBlock(function, currentSymbolTable, op.name() + "_test_more", valTag);
        BasicBlock skipNext = skipNextStack.peek();
        testMoreStack.add(testMore);
        Operand left = visit(node.left());
        testMoreStack.pop();
        boolean setTestMore = true;
        if (left instanceof IntLiteral) {
            int val = ((IntLiteral) left).val();
            int valForTestMore = op == AND ? 1 : 0;
            if (val == valForTestMore) setTestMore = false;
            else {
                if (outMost) clearStack(testMoreStack, skipNextStack);
                return left;
            }
        } else if (left != null) {
            if (node.op() == AND) currentBasicBlock.pushBack(new CondJumpInst(left, testMore, skipNext));
            else currentBasicBlock.pushBack(new CondJumpInst(left, skipNext, testMore));
        }
        if (setTestMore) setCurrentEnv(testMore);
        Operand right = visit(node.right());
        if (right instanceof IntLiteral) {
            if (outMost) clearStack(testMoreStack, skipNextStack);
            return right;
        }
        if (right != null) {
            if (node.op() == AND)
                currentBasicBlock.pushBack(new CondJumpInst(right, testMoreStack.peek(), skipNextStack.peek()));
            else currentBasicBlock.pushBack(new CondJumpInst(right, skipNextStack.peek(), testMoreStack.peek()));
        }
        if (outMost) {
            setCurrentEnv(testMoreStack.pop());
            currentBasicBlock.pushBack(new MoveInst(dst, setLiteral));
            currentBasicBlock.pushBack(new DirectJumpInst(skipNextStack.peek()));
            setCurrentEnv(skipNextStack.pop());
            assert testMoreStack.empty() && skipNextStack.empty();
            return dst;
        }
        return null;
    }

    private void clearStack(Stack<BasicBlock> testMoreStack, Stack<BasicBlock> skipNextStack) {
        testMoreStack.pop();
        if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(skipNextStack.peek()));
        setCurrentEnv(skipNextStack.pop());
    }

    @Override
    public Operand visit(BinaryExprNode node) {
        ExprOps op = node.op();
        switch (op) {
            case ASSIGN: return visitAssign(node);
            case ADD: case SUB: case MUL: case DIV: case MOD: case EQ: case NEQ: case GT: case GTE: case LT:
            case LTE: case BITAND: case BITOR: case BITXOR: case LSFT: case RSFT:
                return visitArithComp(visit(node.left()), node.op(), visit(node.right()), node.left().type());
            case AND: case OR: return visitLogic(node);
            default:
                throw new RuntimeException("Compiler Bug: (IR building) Unprocessed Binary operation" + node.location().toString());
        }
    }

    @Override
    public Operand visit(UnaryExprNode node) {
        ExprOps op = node.getOp();
        Operand src;
        Var dst = Var.tmpBuilder(op.toString());
        switch (op) {
            case ADD: return visit(node.getExpr());
            case SUB:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(-((IntLiteral) src).val());
                return currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src));
            case BITNOT:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return new IntLiteral(~((IntLiteral) src).val());
                return currentBasicBlock.pushBack(new UnaryExprInst(dst, op, src));
            case NOT:
                src = visit(node.getExpr());
                if (src instanceof IntLiteral) return ((IntLiteral) src).val() == 1 ? ZERO_LITERAL : ONE_LITERAL;
                return currentBasicBlock.pushBack(new BinaryExprInst(dst, src, EQ, ZERO_LITERAL));
            case DEC: case INC: case DEC_SUFF: case INC_SUFF:
                src = getMutableOperand(node.getExpr());
                boolean isINC = op == INC || op == INC_SUFF;
                boolean isSUF = op == INC_SUFF || op == DEC_SUFF;
                if (src instanceof Register) {
                    dst = (Var) src;
                    IntLiteral val = (IntLiteral) ((Register) src).getVal(valTag);
                    if (val != null) {
                        IntLiteral newVal = new IntLiteral(isINC ? val.val() + 1 : val.val() - 1);
                        currentBasicBlock.pushBack(new MoveInst(dst, newVal));
                        return isSUF ? val : newVal;
                    }
                }
                MutableOperand sufTmpReg = Var.tmpBuilder("SUF");
                if (isSUF) currentBasicBlock.pushBack(new MoveInst(sufTmpReg, src));
                Operand ret = currentBasicBlock.pushBack(new BinaryExprInst((MutableOperand) src, src, isINC ? ADD : SUB, ONE_LITERAL));
                return isSUF ? sufTmpReg : ret;
            default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for unary expression");
        }

    }

    private Operand visitCreateClass(BaseTypeSymbol classType) {
        Var classReg = Var.tmpBuilder("class_" + classType.getName());
        IntLiteral sizeLiteral = new IntLiteral(classType.getSize());
        currentBasicBlock.pushBack(new FunctionCallInst(Function.MALLOC, new ArrayList<>(Collections.singleton(sizeLiteral)), classReg));
        SymbolInfo constructorSymbolInfo = SymbolTable.getClassSymbolTable(classType).findIn(Instance.CONSTRUCTOR);
        if (constructorSymbolInfo != null) {
            currentBasicBlock.pushBack(new FunctionCallInst(getFunction((FunctionSymbol) constructorSymbolInfo.getSymbol()),
                    new ArrayList<>(Collections.singleton(classReg)), classReg));
        }
        return classReg;
    }

    private Operand visitCreateArray(NewExprNode node, int index) {
        Var baseReg = Var.tmpBuilder("array_base");

        Operand size = visit(node.getDimArgs().get(index));
        Operand bitSize = visitArithComp(visitArithComp(size, MUL, PTR_SIZE, INT_TYPE_SYMBOL), ADD, LENGTH_SIZE, INT_TYPE_SYMBOL);
        currentBasicBlock.pushBack(new FunctionCallInst(Function.MALLOC, new ArrayList<>(Collections.singletonList(bitSize)), baseReg));
        currentBasicBlock.pushBack(new MoveInst(new Memory(baseReg, null, 0, 0), size));


        boolean terminal = index == node.getDimArgs().size() - 1;
//        boolean createClass = !node.getCreateType().isPrimitiveTypeBase() && node.getEmptyDim() == 0;
        // iterate for each subscript
//        if (!terminal || createClass) {
        if (!terminal) { // Not create class for array in new Instruction as the requirement of the M* changed
            Var indexReg = Var.tmpBuilder("index");
            currentBasicBlock.pushBack(new MoveInst(indexReg, ZERO_LITERAL));
            newValTag();
            BasicBlock creatorLoopBegin = new BasicBlock(currentBasicBlock.parentFunction(), SymbolTable.builder(currentSymbolTable), "creator_loop", valTag);
            popValTag();
            BasicBlock next = new BasicBlock(currentBasicBlock.parentFunction(), currentSymbolTable, "creator_loop_end", valTag);
            currentBasicBlock.pushBack(new DirectJumpInst(creatorLoopBegin));
            setCurrentEnv(creatorLoopBegin);
            Operand created;
            if (!terminal) created = visitCreateArray(node, index + 1);
            else created = visitCreateClass(node.getCreateType().getBaseType());
            currentBasicBlock.pushBack(new MoveInst(new Memory(baseReg, indexReg, options.PTR_SIZE, options.LENGTH_SIZE), created));
            Operand indexTmp = visitArithComp(indexReg, ADD, ONE_LITERAL, INT_TYPE_SYMBOL);
            resetCurrentBasicBlockBackDst(indexReg, indexTmp, INT_TYPE_SYMBOL);
            Operand ltTmp = visitArithComp(indexReg, LT, size, INT_TYPE_SYMBOL);
            currentBasicBlock.pushBack(new CondJumpInst(ltTmp, creatorLoopBegin, next));
            popValTag();
            setCurrentEnv(next);
        }
        return baseReg;
    }

    @Override
    public Operand visit(NewExprNode node) {
        if (node.getCreateType() instanceof ArrayTypeSymbol) return visitCreateArray(node, 0);
        else return visitCreateClass(node.getCreateType().getBaseType());

    }

    private void visitPrintCall(List<Operand> args, boolean newline) {
        Instruction lastInst = currentBasicBlock.back();
        StringLiteral formatStr = newline ? StringLiteral.stringlnFormat : StringLiteral.stringFormat;
        Operand outputReg;
        if (lastInst instanceof FunctionCallInst) {
            FunctionCallInst lastFunctionCall = (FunctionCallInst) lastInst;
            if (lastFunctionCall.function() == Function.TO_STRING && lastFunctionCall.dst() == args.get(0)) {
                currentBasicBlock.popBack();
                outputReg = lastFunctionCall.args().get(0);
                formatStr = newline ? StringLiteral.intlnFormat : StringLiteral.intFormat;
            } else outputReg = args.get(0);
        } else outputReg = args.get(0);
        List<Operand> printArgs = new ArrayList<>();

        printArgs.add(formatStr);
        printArgs.add(outputReg);
        currentBasicBlock.pushBack(new FunctionCallInst(Function.PRINTF, printArgs, null));
    }

    @Override
    public Operand visit(FunctionCallNode node) {
        Operand container = visit(node.getCaller());
        Function function = getFunction(currentFunctionSymbol);

        List<Operand> args = new ArrayList<>();
        for (ExprNode argNode : node.getArgs()) {
            Operand arg = visit(argNode);
            Var tmpArg = Var.tmpBuilder("arg_to_var");
            if (arg instanceof Memory) arg = currentBasicBlock.pushBack(new MoveInst(tmpArg, arg));
            args.add(arg);
        }
        if (container != null) args.add(container);
        else if (inClassFunc && currentFunctionSymbol.getClassSymbolTable() != null) args.add(classDeclThisReg);

        if (function == Function.SIZE) {
            Var dst = Var.tmpBuilder("array_size");
            if (container instanceof Memory) {
                Var ptr = Var.tmpBuilder("array_ptr");
                container = currentBasicBlock.pushBack(new MoveInst(ptr, container));
            }
            Memory size = new Memory((Register) container, null, 0, 0);
            currentBasicBlock.pushBack(new MoveInst(dst, size));
            return dst;
        }
        if (function == Function.PRINT) visitPrintCall(args, false);
        else if (function == Function.PRINTLN) visitPrintCall(args, true);
        else if (function == Function.GET_INT) {
            args.add(LiteralProcess.stringLiteralBuilder("%ld"));
            Var dst = Var.tmpBuilder("get_int");
            return currentBasicBlock.pushBack(new FunctionCallInst(Function.SCANF_INT, args, dst));
        } else if (function == Function.PARSE_INT) {
            args.add(LiteralProcess.stringLiteralBuilder("%ld"));
            Var dst = Var.tmpBuilder("parse_int");
            return currentBasicBlock.pushBack(new FunctionCallInst(Function.STR_PARSE_INT, args, dst));
        } else {
            Var dst = Var.tmpBuilder("call_ret");
            return currentBasicBlock.pushBack(new FunctionCallInst(function, args, dst));
        }
        return null;
    }

    @Override
    public Operand visit(ConstructorCallNode node) {
        return visitCreateClass(node.getClassTypeSymbol().getBaseType());
    }

    @Override
    public Operand visit(IfNode node) {
        Function function = currentBasicBlock.parentFunction();
        Operand cond = node.getCondition() == null ? ONE_LITERAL : visit(node.getCondition());
        boolean haveElse = node.getElseNode() != null;
        if (cond instanceof IntLiteral) {
            if (((IntLiteral) cond).val() == 1) visit(node.getBody());
            else if (haveElse) visit(node.getElseNode());
        } else {
            BasicBlock ifTrue = new BasicBlock(function, node.getBody().getCurrentSymbolTable(), prefix("if_then"), valTag); //TODO
            BasicBlock ifFalse = new BasicBlock(function,
                    haveElse ? node.getElseNode().getBody().getCurrentSymbolTable() : null, prefix("if_else"), valTag);
            BasicBlock next = new BasicBlock(function, currentSymbolTable, valTag);
            CondJumpInst condJump = new CondJumpInst(cond, ifTrue, haveElse ? ifFalse : next);
            currentBasicBlock.pushBack(condJump);
            if (haveElse) {
                setCurrentEnv(ifFalse);
                visit(node.getElseNode());
                if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(next));
            }
            setCurrentEnv(ifTrue);
            visit(node.getBody());
            if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(next));
            setCurrentEnv(next);
        }
        return null;
    }

    private Stack<BasicBlock> loopCondStack = new Stack<>();
    private Stack<BasicBlock> loopEndStack = new Stack<>();

    @Override
    public Operand visit(LoopNode node) {
        Function function = currentBasicBlock.parentFunction();
        BasicBlock loopEnd = new BasicBlock(function, currentSymbolTable, prefix("loop_end"), valTag);
        BasicBlock loopStartCond = new BasicBlock(function, currentSymbolTable, prefix("loop_start_cond"), valTag);
        newValTag();
        BasicBlock loopCond = new BasicBlock(function, SymbolTable.builder(currentSymbolTable), prefix("loop_cond"), valTag);
        BasicBlock loopBegin = new BasicBlock(function, node.getBody().getCurrentSymbolTable(), prefix("loop_begin"), valTag);
//        if (node.getBody().getStatements().isEmpty()) {
//            if (node.getVarInit() != null) visit(node.getVarInit());
//            currentBasicBlock.pushBack(new DirectJumpInst(loopCond));
//            setCurrentEnv(loopCond);
//            visitLoopCondition(node, loopBegin, loopEnd);
//            setCurrentEnv(loopEnd);
//            return null;
//        }
        popValTag();
        currentBasicBlock.pushBack(new DirectJumpInst(loopStartCond));
        setCurrentEnv(loopStartCond);
        if (node.getVarInit() != null) visit(node.getVarInit());
        if (visitLoopCondition(node, loopBegin, loopEnd)) return null; // Never go into the loop

        newValTag();
        loopCondStack.push(loopCond);
        loopEndStack.push(loopEnd);
        setCurrentEnv(loopBegin);
        visit(node.getBody());
        if (!currentBasicBlock.isEnded) currentBasicBlock.pushBack(new DirectJumpInst(loopCond));

        setCurrentEnv(loopCond);
        if (node.getStep() != null) visit(node.getStep());
        visitLoopCondition(node, loopBegin, loopEnd);
        loopCondStack.pop();
        loopEndStack.pop();

        setCurrentEnv(loopEnd);
        popValTag();
        return null;
    }

    private boolean visitLoopCondition(LoopNode node, BasicBlock loopBegin, BasicBlock loopEnd) {
        if (node.getCondition() != null) {
            Operand cond = visit(node.getCondition());
            if (cond instanceof IntLiteral) {
                if (((IntLiteral) cond).val() == 0) return true; // Never go into the loop
                currentBasicBlock.pushBack(new DirectJumpInst(loopBegin));
            } else currentBasicBlock.pushBack(new CondJumpInst(cond, loopBegin, loopEnd));
        } else currentBasicBlock.pushBack(new DirectJumpInst(loopBegin));
        return false;
    }

    private FunctionSymbol currentFunctionSymbol;

    @Override
    public Operand visit(DotMemberNode node) {
        //TODO
        if (node.getContainer() instanceof StringLiteralNode) {
            Operand container = visit(node.getContainer());
            SymbolInfo stringFuncInfo = SymbolTable.STRING_SYMBOL_TABLE.findIn(node.getMember().getInstance());
            currentFunctionSymbol = (FunctionSymbol) stringFuncInfo.getSymbol();
            return container;
        }

        Operand container = getMutableOperand(node.getContainer());
        TypeSymbol containerType = node.getContainer().type();
        if (containerType instanceof BaseTypeSymbol) {
            SymbolTable symbolTable = SymbolTable.getClassSymbolTable((BaseTypeSymbol) containerType);
            SymbolInfo memberInfo = symbolTable.findIn(node.getMember().getInstance());
            if (memberInfo.isClassMember()) {
                Register baseReg = Var.tmpBuilder("container_base");
                if (container instanceof Memory) currentBasicBlock.pushBack(new MoveInst(baseReg, container));
                else baseReg = (Var) container;
                return new Memory(baseReg, null, 0, memberInfo.getOffset());
            }
            currentFunctionSymbol = (FunctionSymbol) memberInfo.getSymbol();
            return container;
        }
        // For array (size function)
        currentFunctionSymbol = FunctionSymbol.SIZE;
        return container;
    }

    @Override
    public Operand visit(BrackMemberNode node) {
        Operand container = getParamForMem(visit(node.getContainer()), "array_base");
        Operand subscript = getParamForMem(visit(node.getSubscript()), "array_val");
        return new Memory((Register) container, subscript, options.PTR_SIZE, options.LENGTH_SIZE);
    }

    private Operand getParamForMem(Operand x, String name) {
        if (x instanceof Var || x instanceof Literal) return x;
        Var tmp = Var.tmpBuilder(name);
        currentBasicBlock.pushBack(new MoveInst(tmp, x));
        return tmp;
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
    public Operand visit(IdentifierExprNode node) {
        SymbolInfo symbolInfo = findReg(currentSymbolTable, node.getInstance());
        if (symbolInfo.getSymbol() instanceof FunctionSymbol) {
            currentFunctionSymbol = (FunctionSymbol) symbolInfo.getSymbol();
            return null;
        }
        if (symbolInfo.isClassMember())
            return new Memory(classDeclThisReg, null, 0, symbolInfo.getOffset());
        MutableOperand reg = symbolInfo.getOperand();
        return getVal(reg);
    }

    @Override
    public Operand visit(NullLiteralNode node) {
        return ZERO_LITERAL;
    }

    @Override
    public Operand visit(StringLiteralNode node) {
        return LiteralProcess.stringLiteralBuilder(node.getVal());
    }

    @Override
    public Operand visit(BoolLiteralNode node) {
        return node.getVal() ? ONE_LITERAL : ZERO_LITERAL;
    }

    @Override
    public Operand visit(IntLiteralNode node) {
        return new IntLiteral(node.getVal());
    }

    @Override
    public Operand visit(EmptyExprNode node) {
        return null;
    }

    private MutableOperand getMutableOperand(Node node) {
        isGetMutableOperand = true;
        Operand reg = visit(node);
        isGetMutableOperand = false;
        return (MutableOperand) reg;
    }

    private SymbolInfo findReg(SymbolTable symbolTable, Instance instance) {
        SymbolInfo symbolInfo = symbolTable.findIn(instance);
        if (symbolInfo != null) {
            if (symbolInfo.getSymbol() instanceof FunctionSymbol) return symbolInfo;
            if (symbolInfo.getOperand() != null) return symbolInfo;
            if (symbolInfo.isClassMember()) return symbolInfo;
        }
        return findReg(symbolTable.getParentSymbolTable(), instance);
    }

    private Operand getVal(Operand reg) {
        if (reg instanceof MutableOperand) {
            Literal val = null;
            if (reg instanceof Register) val = currentBasicBlock.getKnownReg((Register) reg);
            return (isGetMutableOperand || val == null) ? reg : val;
        }
        return reg;
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
        if (block.parentFunction() != null) block.parentFunction().pushBack(block);
        else if (globalReverseInitBlock.isEmpty() || globalReverseInitBlock.getFirst() != block)
            globalReverseInitBlock.addFirst(block);
        currentSymbolTable = block.getCurrentSymbolTable();
        currentBasicBlock = block;
    }

    private void setCurrentEnv(Function function, SymbolTable symbolTable) {
        currentSymbolTable = symbolTable;
        currentBasicBlock = new BasicBlock(function, currentSymbolTable, valTag);
        function.pushBack(currentBasicBlock);
    }

    private void resetCurrentBasicBlockBackDst(MutableOperand dst, Operand origin, TypeSymbol typeSymbol) {
        if (currentBasicBlock.empty()) currentBasicBlock.pushBack(new MoveInst(dst, origin));
        else if (currentBasicBlock.back() instanceof BinaryExprInst && ((BinaryExprInst) currentBasicBlock.back()).isCompare())
            currentBasicBlock.pushBack(new MoveInst(dst, origin));
        else if (typeSymbol.isPrimitiveType() && origin instanceof Var && ((Var) origin).isTmp()) {
            assert currentBasicBlock.back() instanceof AssignInst;
            AssignInst assignInst = (AssignInst) currentBasicBlock.popBack();
            assignInst.setDst(dst);
            currentBasicBlock.pushBack(assignInst);
        } else currentBasicBlock.pushBack(new MoveInst(dst, origin));
    }


    private String prefix(String name) {
        return currentBasicBlock.parentFunction().name() + "_" + name;
    }

    private IntLiteral PTR_SIZE;
    private IntLiteral LENGTH_SIZE;
    private final StringLiteral EMPTY_STRING = new StringLiteral("");
}
