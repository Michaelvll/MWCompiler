package mwcompiler.backend;

import mwcompiler.ir.nodes.ProgramIR;

public abstract class Allocator {
    public abstract void apply(ProgramIR programIR);

    public int alignStack(int length, int alignSize) {
        return (length + alignSize - 1) / alignSize * alignSize;
    }
}
