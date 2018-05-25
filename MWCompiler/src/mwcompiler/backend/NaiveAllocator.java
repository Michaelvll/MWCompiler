package mwcompiler.backend;

import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.utility.CompilerOptions;

public class NaiveAllocator {
    private CompilerOptions options;

    public NaiveAllocator(CompilerOptions options) {
        this.options = options;
    }

    public void allocate(ProgramIR programIR) {
        programIR.getFunctionMap().values().forEach(this::compileFunction);
    }

    public void compileFunction(Function function){

    }


}
