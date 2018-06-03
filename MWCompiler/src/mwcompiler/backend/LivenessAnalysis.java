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
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.utility.CompilerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LivenessAnalysis {
    private ProgramIR programIR;
    private CompilerOptions options;

    public LivenessAnalysis(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;

        programIR.functionMap().values().forEach(this::analysisFunction);
        programIR.functionMap().values().forEach(this::eliminate);
        programIR.functionMap().values().forEach(this::analysisFunction);
    }

    private void analysisFunction(Function function) {
        if (function.notUserFunc()) return;
        resetInOut(function);

        boolean change = true;
        List<BasicBlock> blocks = function.basicBlocks();
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
                        }
                    }
                }
            }
        }
    }
}
