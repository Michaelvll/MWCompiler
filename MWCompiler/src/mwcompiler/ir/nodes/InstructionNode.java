package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class InstructionNode {
    public abstract void accept(IRVisitor visitor);
}
