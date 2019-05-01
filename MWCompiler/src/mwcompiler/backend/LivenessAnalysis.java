package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
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
            if (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - oldTime) > 12) break;
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
                    if (inst instanceof FunctionCallInst && ((FunctionCallInst) inst).callee() == Function.MALLOC) {
                        Var memBase = (Var) ((FunctionCallInst) inst).dst();
                        if (!memBase.isGlobal() && inst.next instanceof MoveInst) {
                            MoveInst next = (MoveInst) inst.next;
                            if (next.dst() instanceof Memory && !next.liveOut().contains(memBase)) {
                                Memory dst = (Memory) next.dst();
                                if (dst.baseReg() == memBase && dst.indexReg() == null && next.val() instanceof IntLiteral) {
                                    liveOut.remove(memBase);
                                }
                            }
                        }
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
                        if (inst instanceof FunctionCallInst && ((FunctionCallInst) inst).callee() == Function.MALLOC) {
                            Var memBase = (Var) ((FunctionCallInst) inst).dst();
                            if (inst.liveOut().contains(memBase) || memBase.isGlobal()) continue;
                            blocks.get(index).delete(inst.next);
                            blocks.get(index).delete(inst);
                            eliminateChange = true;
                        }
                    }
                    for (Var var : inst.usedLocalVar()) {
                        if (var.isOnlyOnLeft()) {
                            MutableOperand dst = ((AssignInst) inst).dst();
                            if (dst != null) {
                                if (dst instanceof Var) ((Var) dst).setOnlyOnLeft();
                                else if (dst instanceof Memory) {
                                    Var base = (Var) ((Memory) dst).baseReg();
                                    base.setOnlyOnLeft();
                                }
                            }
                            System.err.println("Delete only on left inst: " + var.irName());
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
        List<Pair<Integer, Integer>> loopIndex = new ArrayList<>();
        Set<BasicBlock> visitedBlock = new HashSet<>();
        List<BasicBlock> functionBlocks = function.basicBlocks();
        for (BasicBlock block : functionBlocks) {
            visitedBlock.add(block);
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
                        loopIndex.add(new Pair<>(start, end));
                    }
                }
            }
        }
        Set<Register> out;
        Set<Integer> deleteIndex = new HashSet<>();
        for (Pair<Integer, Integer> aLoopIndex : loopIndex) {
            int start = aLoopIndex.first;
            int end = aLoopIndex.second;
            out = functionBlocks.get(end + 1).front().liveIn();
            boolean delete = true;
            for (int sub = start; sub <= end; ++sub) {
                for (Instruction inst = functionBlocks.get(sub).front(); inst != null; inst = inst.next) {
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
                System.err.println("Delete irrelevant loop: " + functionBlocks.get(start).name());
                BasicBlock currentBlock = functionBlocks.get(start);
                currentBlock.setFront(null);
                currentBlock.setEnd(null);
                BasicBlock nextBlock = functionBlocks.get(end + 1);
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
