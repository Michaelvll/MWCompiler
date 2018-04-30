package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Register {
    public abstract <T> T accept(IRVisitor<T> visitor);
}
