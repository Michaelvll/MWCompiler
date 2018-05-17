package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Register extends Operand {
    private IntLiteral val;

    public IntLiteral getVal() {
        return val;
    }

    public void setVal(IntLiteral val) {
        this.val = val;
    }

    public abstract <T> T accept(IRVisitor<T> visitor);
}
