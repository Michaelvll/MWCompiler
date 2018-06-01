package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.utility.CompilerOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class FunctionInliner {
    private ProgramIR programIR;
    private CompilerOptions options;

    public FunctionInliner(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;

        boolean change = true;
        while (change) {
            change = false;
            for (Function function : programIR.functionMap().values()) {
                if (function.notUserFunc()) continue;
                LinkedList<BasicBlock> newBlocks = new LinkedList<>();
                for (BasicBlock block : function.basicBlocks()) {
                    newBlocks.add(block);
                    for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                        if (inst instanceof FunctionCallInst) {
                            FunctionCallInst functionCallInst = (FunctionCallInst) inst;
                            Function callee = functionCallInst.function();
                            if (callee.notUserFunc() || callee.isMain() || callee == function) continue;
                            if (callee.instNum <= options.INLINE_CALLEE_BOUND && function.instNum <= options.INLINE_CALLER_BOUND) {
                                newBlocks.addAll(inline(function, callee, newBlocks.getLast(), functionCallInst));
                                change = true;
                                function.instNum += callee.instNum;
                            }
                        }
                    }
                }
                if (change) function.setBasicBlocks(newBlocks);
            }
        }
        programIR.functionMap().values().forEach(func->{
            func.cleanUp();
            func.recalcCalleSet();
        });
    }

    private LinkedList<BasicBlock> inline(Function function, Function callee, BasicBlock currentBlock, FunctionCallInst inst) {
        BasicBlock afterInlineBlock = new BasicBlock(function, currentBlock.getCurrentSymbolTable(), function.name() + "_inline_next");
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
        for (int index = 0; index < callee.paramVars().size(); ++index) {
            replaceMap.put(callee.paramVars().get(index), inst.args().get(index));
        }
        replaceMap.put(callee, function);
        if (inst.dst() != null) replaceMap.put("retDst", inst.dst());
        replaceMap.put("inline_next", afterInlineBlock);

        LinkedList<BasicBlock> newBlocks = new LinkedList<>();
        for (BasicBlock block : callee.basicBlocks()) {
            newBlocks.addLast(block.deepCopy(replaceMap));
        }
        currentBlock.pushBack(new DirectJumpInst(newBlocks.getFirst()));
        newBlocks.addLast(afterInlineBlock);
        return newBlocks;
    }
}
