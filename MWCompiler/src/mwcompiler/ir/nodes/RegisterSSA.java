package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class RegisterSSA extends SSA {
    public abstract <T> T accept(IRVisitor<T> visitor);
}
