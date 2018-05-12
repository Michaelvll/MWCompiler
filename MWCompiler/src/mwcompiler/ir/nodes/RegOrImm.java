package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class RegOrImm {
    public abstract <T>T accept(IRVisitor<T> visitor);
}
