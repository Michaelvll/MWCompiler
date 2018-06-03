package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Var;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.Pair;

import java.util.*;

public class FunctionInliner {
    private ProgramIR programIR;
    private CompilerOptions options;

    public FunctionInliner(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;
        updateRecursiveCalleeSet();

        int iter = 0;

        boolean change = true;
        Function mainFunc = programIR.functionMap().get(FunctionSymbol.MAIN);
        while (change) {
            LinkedList<BasicBlock> newBlocks = new LinkedList<>();
            change = processInline(iter, false, mainFunc, newBlocks);
            if (change) mainFunc.setBasicBlocks(newBlocks);
            ++iter;
        }
        updateRecursiveCalleeSet();
        iter = 0;
        change = true;
        while (change) {
            change = false;
            for (Function function : programIR.functionMap().values()) {
                if (function.notUserFunc() || function.isMain() || !mainFunc.recursiveCalleeSet().contains(function))
                    continue;
//            Function function = programIR.getFunction(FunctionSymbol.MAIN);
                LinkedList<BasicBlock> newBlocks = new LinkedList<>();
                change = processInline(iter, change, function, newBlocks);
                if (change) function.setBasicBlocks(newBlocks);
            }
            ++iter;
        }
        programIR.functionMap().values().forEach(func -> {
            if (!func.notUserFunc()) {
                func.cleanUp();
                func.reCalcCalleeSet();
            }
        });
    }

    private boolean processInline(int iterate, boolean change, Function function, LinkedList<BasicBlock> newBlocks) {
        for (BasicBlock block : function.basicBlocks()) {
            newBlocks.add(block);
            for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                if (inst instanceof FunctionCallInst) {
                    FunctionCallInst functionCallInst = (FunctionCallInst) inst;
                    Function callee = functionCallInst.function();
                    if (callee.notUserFunc() || callee.isMain() || callee == function ||
                            (iterate >= options.INLINE_RECURSIVE_LEVEL && callee.recursiveCalleeSet().contains(callee)))
                        continue;
                    if (callee.instNum <= options.INLINE_CALLEE_BOUND && function.instNum <= options.INLINE_CALLER_BOUND) {
                        newBlocks.addAll(inline(function, callee, newBlocks.getLast(), functionCallInst));
                        change = true;
                        function.instNum += callee.instNum;
                    }
                }
            }
        }
        return change;
    }

    private LinkedList<BasicBlock> inline(Function function, Function callee, BasicBlock currentBlock, FunctionCallInst inst) {
        BasicBlock afterInlineBlock = new BasicBlock(function, currentBlock.getCurrentSymbolTable(), function.name() + "_inline_next", currentBlock.valTag());
        currentBlock.delete(inst);
        Instruction next = inst.next;
        if (next != null) {
            next.prev = null;
            afterInlineBlock.setFront(next);
            afterInlineBlock.setEnd(currentBlock.back());
        }
        if (inst.prev != null) inst.prev.next = null;
        else currentBlock.setFront(null);
        currentBlock.setEnd(inst.prev);

        Map<Object, Object> replaceMap = new HashMap<>();
        LinkedList<BasicBlock> newBlocks = new LinkedList<>();
        replaceMap.put(callee, function);
        if (inst.dst() != null) replaceMap.put("retDst", inst.dst());
        replaceMap.put("inline_next", afterInlineBlock);

        List<Pair<Var, Operand>> argSet = new ArrayList<>();
        for (int index = 0; index < callee.paramVars().size(); ++index) {
//            replaceMap.put(callee.paramVars().get(index), inst.args().get(index));
            argSet.add(new Pair<>(callee.paramVars().get(index), inst.args().get(index)));
        }
//        currentBlock.pushBack(new DirectJumpInst(argSet));

        List<BasicBlock> calleeBlocks = callee.basicBlocks();

        for (int index = 0; index < calleeBlocks.size(); ++index) {
            if (index == 0) calleeBlocks.get(index).deepCopy(replaceMap, argSet, currentBlock);
            else newBlocks.addLast(calleeBlocks.get(index).deepCopy(replaceMap));
//            newBlocks.addLast(calleeBlocks.get(index).deepCopy(replaceMap));
        }
//        for (BasicBlock block : callee.basicBlocks()) {
//            newBlocks.addLast(block.deepCopy(replaceMap));
//        }
        newBlocks.addLast(afterInlineBlock);
//        argSet.pushBack(new DirectJumpInst(newBlocks.getFirst()));
//        newBlocks.addFirst(argSet);
        return newBlocks;
    }

    private void updateRecursiveCalleeSet() {
        programIR.functionMap().values().forEach(func -> {
            func.reCalcCalleeSet();
            func.recursiveCalleeSet().clear();
            func.recursiveCalleeSet().addAll(func.calleeSet());
        });
        boolean change = true;
        while (change) {
            change = false;
            Set<Function> recurseSet = new HashSet<>();
            for (Function function : programIR.functionMap().values()) {
                if (function.notUserFunc()) continue;
                recurseSet.clear();
                recurseSet.addAll(function.calleeSet());
                function.calleeSet().forEach(callee -> recurseSet.addAll(callee.recursiveCalleeSet()));
                if (function.recursiveCalleeSet().equals(recurseSet)) continue;
                change = true;
                function.recursiveCalleeSet().clear();
                function.recursiveCalleeSet().addAll(recurseSet);
            }
        }

    }
}
