package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Operand {
    public abstract <T>T accept(IRVisitor<T> visitor);
}
