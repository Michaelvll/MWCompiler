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

public class NaiveAllocator extends Allocator{
    private CompilerOptions options;

    public NaiveAllocator(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        programIR.functionMap().values().forEach(this::compileFunction);
    }

    private void compileFunction(Function function) {
        if (function.notUserFunc()) return;

        Set<Var> vars = function.vars();
        for (BasicBlock block : function.basicBlocks()) {
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
        function.addUsedPReg(RBP);
        for (int index = 0, allocateIndex = 0; index < usageRank.size() && allocateIndex < allocateRegs.size(); ++index) {
            if (!usageRank.get(index).isGlobal() && !usageRank.get(index).isCompareTmp()) {
//                System.err.println("Var: " + usageRank.get(index).irName() + " -> " + allocateRegs.get(allocateIndex));
                usageRank.get(index).setPhysicalRegister(allocateRegs.get(allocateIndex));
                function.addUsedPReg(allocateRegs.get(allocateIndex));
                ++allocateIndex;
            }
        }


        // locate params
        int paramStackTop =  function.usedCalleeRegs().size() * options.PTR_SIZE + options.PTR_SIZE; // callee-save + return address
        List<Var> params = function.paramVars();
        for (int index = 0; index < params.size(); ++index) {
            if (index >= PhysicalRegister.paramRegs.size()) {
                params.get(index).setStackPos(new Memory(RBP, null, 0, paramStackTop));
                paramStackTop += options.PTR_SIZE;// We rule that each arg must be size of 8
            }
        }

        // locate local variables
        int localVarStackTop = options.PTR_SIZE;
        localVarStackTop = locateLocalVariables(function.getSymbolTable(), localVarStackTop);

        // locate tmp variables
        int tmpVarStackTop = localVarStackTop;
        for (Var var : function.vars()) {
            if (var.isTmp()) {
                tmpVarStackTop += options.PTR_SIZE;
                var.setStackPos(new Memory(RBP, null, 0, -tmpVarStackTop));
            }
        }
//        if (callee.isMain()) tmpVarStackTop = alignStack(tmpVarStackTop, options.FUNC_CALL_STACK_ALIGN_SIZE);
//        else if (tmpVarStackTop % 16 == 0) tmpVarStackTop += options.FUNC_CALL_STACK_ALIGN_SIZE - options.PTR_SIZE;
//        if (tmpVarStackTop % 16 == 0) tmpVarStackTop += options.FUNC_CALL_STACK_ALIGN_SIZE - options.PTR_SIZE;
        // TODO : rsp and -0x10 may be helpful
        // align stack to 16 for callee call (align size - address point)
        function.setVarStackSize(tmpVarStackTop);
    }

    private int locateLocalVariables(SymbolTable symbolTable, int parentStackOffset) {
        int offset = parentStackOffset;
        for (Var var : symbolTable.getVarList()) {
            if (var.stackPos() == null) {
                offset = alignStack(offset + var.getSize(), options.PTR_SIZE);
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

}
