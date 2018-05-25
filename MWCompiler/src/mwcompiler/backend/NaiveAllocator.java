package mwcompiler.backend;

import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.Var;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.CompilerOptions;

import static mwcompiler.ir.operands.PhysicalRegister.RBP;

public class NaiveAllocator {
    private CompilerOptions options;

    public NaiveAllocator(CompilerOptions options) {
        this.options = options;
        PARAM_START_SIZE = 2 * options.PTR_SIZE;
    }

    public void allocate(ProgramIR programIR) {
        programIR.getFunctionMap().values().forEach(this::compileFunction);
    }

    private void compileFunction(Function function) {
        // locate params
        int paramStackTop = PARAM_START_SIZE;
        for (Var param : function.getParamVReg()) {
            param.setStackPos(new Memory(RBP, null, 0, -paramStackTop));
            paramStackTop += options.PTR_SIZE;
        }

        // locate local variables
        int localVarStackTop = paramStackTop;
        localVarStackTop = locateLocalVariables(function.getSymbolTable(), localVarStackTop);

        // locate tmp variables
        int tmpVarStackTop = localVarStackTop;
        for (Var var : function.getTmpVars()) {
            if (var.isTmp()) {
                var.setStackPos(new Memory(RBP, null, 0, tmpVarStackTop));
                tmpVarStackTop += options.PTR_SIZE;
            }
        }
    }

    private int locateLocalVariables(SymbolTable symbolTable, int parentStackOffset) {
        int offset = parentStackOffset;
        for (Var var : symbolTable.getVarList()) {
            offset = alignStack(offset + var.getSize());
            var.setStackPos(new Memory(RBP, null, 0, -offset));
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


    private int PARAM_START_SIZE;
}
