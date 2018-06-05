package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LivenessAnalysis {
    private ProgramIR programIR;
    private CompilerOptions options;

    public LivenessAnalysis(CompilerOptions options) {
        this.options = options;
    }

    private boolean eliminateChange = false;

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;
        long oldTime = System.nanoTime();
        int iterate = 0;
        programIR.functionMap().values().forEach(this::analysisFunction);
        do {
            eliminateChange = false;
            programIR.functionMap().values().forEach(func -> {
                if (!func.notUserFunc()) {
                    eliminate(func);
                    loopEliminate(func);
                    func.cleanUp();
                    analysisFunction(func);
                }
            });
            System.err.println("eliminate for " + String.valueOf(++iterate));
            if (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - oldTime) > 15) break;
        } while (eliminateChange);
    }

    private void analysisFunction(Function function) {
        resetInOut(function);
        List<BasicBlock> blocks = function.basicBlocks();
        boolean change = true;
        while (change) {
            change = false;
            Set<Register> liveIn = new HashSet<>();
            Set<Register> liveOut = new HashSet<>();
            for (int index = blocks.size() - 1; index >= 0; --index) {
                for (Instruction inst = blocks.get(index).back(); inst != null; inst = inst.prev) {
                    liveIn.clear();
                    liveOut.clear();

                    liveIn.addAll(inst.liveOut());
                    liveIn.removeAll(inst.dstLocalVar());
                    liveIn.addAll(inst.usedLocalVar());

                    if (inst instanceof CondJumpInst) {
                        liveOut.addAll(((CondJumpInst) inst).ifTrue().front().liveIn());
                        liveOut.addAll(((CondJumpInst) inst).ifFalse().front().liveIn());
                    } else if (inst instanceof DirectJumpInst) {
                        liveOut.addAll(((DirectJumpInst) inst).target().front().liveIn());
                    } else if (!(inst instanceof ReturnInst)) {
                        liveOut.addAll(inst.next.liveIn());
                    }

                    if (liveIn.equals(inst.liveIn()) && liveOut.equals(inst.liveOut())) continue;
                    change = true;
                    inst.liveIn().clear();
                    inst.liveIn().addAll(liveIn);
                    inst.liveOut().clear();
                    inst.liveOut().addAll(liveOut);
                }
            }
        }
    }

    private void resetInOut(Function function) {
        for (BasicBlock block : function.basicBlocks()) {
            for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                inst.liveIn().clear();
                inst.liveOut().clear();
            }
        }
    }

    private void eliminate(Function function) {
        List<BasicBlock> blocks = function.basicBlocks();
        for (int index = blocks.size() - 1; index >= 0; --index) {
            for (Instruction inst = blocks.get(index).back(); inst != null; inst = inst.prev) {
                if (inst instanceof AssignInst) {
                    for (Var dst : inst.dstLocalVar()) {
                        if (!inst.liveOut().contains(dst) && !(inst instanceof FunctionCallInst)) {
                            blocks.get(index).delete(inst);
                            eliminateChange = true;
                        }
                    }
                }
            }
        }
    }

    private void loopEliminate(Function function) {
        if (function.notUserFunc()) return;
        List<List<BasicBlock>> loopBlocks = new ArrayList<>();
        List<Pair<Integer, Integer>> loopIndex = new ArrayList<>();
        Set<BasicBlock> visitedBlock = new HashSet<>();
        List<BasicBlock> functionBlocks = function.basicBlocks();
        for (BasicBlock block : functionBlocks) {
            if (block.back() instanceof CondJumpInst) {
                CondJumpInst condJumpInst = (CondJumpInst) block.back();
                Integer start = null;
                if (visitedBlock.contains(condJumpInst.ifTrue()))
                    start = functionBlocks.indexOf(condJumpInst.ifTrue());
                else if (visitedBlock.contains(condJumpInst.ifFalse()))
                    start = functionBlocks.indexOf(condJumpInst.ifFalse());
                if (start != null && start != 0) {
                    int end = functionBlocks.indexOf(block);
                    if (end - start <= 1) {
                        loopBlocks.add(functionBlocks.subList(start, end));
                        loopIndex.add(new Pair<>(start, end));
                    }
                }
            }
            visitedBlock.add(block);
        }
        Set<Register> out;
        Set<Integer> deleteIndex = new HashSet<>();
        for (int index = 0; index < loopBlocks.size(); ++index) {
            int start = loopIndex.get(index).first;
            int end = loopIndex.get(index).second;
            out = functionBlocks.get(end + 1).front().liveIn();
            boolean delete = true;
            for (BasicBlock block : loopBlocks.get(index)) {
                for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                    if (inst instanceof ReturnInst || inst instanceof FunctionCallInst) {
                        delete = false;
                        break;
                    }
                    if (inst instanceof AssignInst) {
                        MutableOperand dst = ((AssignInst) inst).dst();
                        if (dst instanceof Memory || (dst instanceof Var && (((Var) dst).isGlobal() || out.contains(dst)))) {
                            delete = false;
                            break;
                        }
                    }
                }
                if (!delete) break;
            }
            if (delete) {
                System.err.println("Delete irrelevant loop: " + loopBlocks.get(index).get(0).name());
                BasicBlock currentBlock = loopBlocks.get(index).get(0);
                BasicBlock nextBlock = functionBlocks.get(end + 1);
                currentBlock.setFront(null);
                currentBlock.setEnd(null);
                currentBlock.pushBack(new DirectJumpInst(nextBlock));
                for (int i = start + 1; i <= end; ++i) deleteIndex.add(i);
            }
        }
        int index = 0;
        LinkedList<BasicBlock> newFuncBlocks = new LinkedList<>();
        if (deleteIndex.isEmpty()) return;
        for (BasicBlock block : functionBlocks) {
            if (!deleteIndex.contains(index)) newFuncBlocks.add(block);
            ++index;
        }
        function.setBasicBlocks(newFuncBlocks);
    }
}
