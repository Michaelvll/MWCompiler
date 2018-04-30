package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class SSA {
    public abstract <T>T accept(IRVisitor<T> visitor);
}
