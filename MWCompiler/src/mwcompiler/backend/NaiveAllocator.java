package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.PhysicalRegister;
import mwcompiler.ir.operands.Var;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.CompilerOptions;

import java.util.*;

import static mwcompiler.ir.operands.PhysicalRegister.*;

public class NaiveAllocator {
    private CompilerOptions options;

    public NaiveAllocator(CompilerOptions options) {
        this.options = options;
        PARAM_START = 2 * options.PTR_SIZE;
    }

    public void apply(ProgramIR programIR) {
        programIR.getFunctionMap().values().forEach(this::compileFunction);
    }

    private void compileFunction(Function function) {
        if (function.isLib()) return;

        Set<Var> vars = new HashSet<>();
        for (BasicBlock block : function.getBasicBlocks()) {
            for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                for (Var var : inst.usedVar()) {
                    if (!var.isCompareTmp()) {
                        var.addUseTime();
                        vars.add(var);
                    }
                }
                for (Var var : inst.dstVar()) {
                    if (!var.isCompareTmp()) {
                        var.addUseTime();
                        vars.add(var);
                    }
                }
            }
        }
        List<Var> usageRank = new ArrayList<>(vars);
        usageRank.sort(Comparator.comparingInt(Var::useTime).reversed());

        List<PhysicalRegister> allocateRegs = new ArrayList<>(Arrays.asList(RBX, R12, R13, R14, R15));
        int allocateIndex = 0;
        for (int index = 0; index < usageRank.size() && allocateIndex < allocateRegs.size(); ++index) {
            if (!usageRank.get(index).isGlobal() && !usageRank.get(index).isCompareTmp()) {
                usageRank.get(index).setPhysicalRegister(allocateRegs.get(allocateIndex));
                function.addUsedPReg(allocateRegs.get(allocateIndex));
                ++allocateIndex;
            }
        }
        function.addUsedPReg(RBP);


        // locate params
        int paramStackTop = PARAM_START;
        List<Var> params = function.getParamVReg();
        for (int index = 0; index < params.size(); ++index) {
            if (index >= PhysicalRegister.paramRegs.size()) {
                params.get(index).setStackPos(new Memory(RBP, null, 0, paramStackTop));
                paramStackTop += options.PTR_SIZE;
            }
        }

        // locate local variables
        int localVarStackTop = PhysicalRegister.calleeSaveRegs.size();
        localVarStackTop = locateLocalVariables(function.getSymbolTable(), localVarStackTop);

        // locate tmp variables
        int tmpVarStackTop = localVarStackTop;
        for (Var var : function.getVars()) {
            if (var.isTmp()) {
                var.setStackPos(new Memory(RBP, null, 0, tmpVarStackTop));
                tmpVarStackTop += options.PTR_SIZE;
            }
        }

        function.setVarStackSize(tmpVarStackTop);
    }

    private int locateLocalVariables(SymbolTable symbolTable, int parentStackOffset) {
        int offset = parentStackOffset;
        for (Var var : symbolTable.getVarList()) {
            if (var.stackPos() == null) {
                offset = alignStack(offset + var.getSize());
                var.setStackPos(new Memory(RBP, null, 0, -offset));
            }
        }
        int maxLen = offset;
        for (SymbolTable child : symbolTable.getChildren()) {
            int childLen = locateLocalVariables(child, offset);
            if (maxLen < childLen) maxLen = childLen;
        }
        return maxLen;
    }

    private int alignStack(int length) {
        return options.STACK_ALIGN_SIZE - length % options.STACK_ALIGN_SIZE + length;
    }

    private int PARAM_START;
}
