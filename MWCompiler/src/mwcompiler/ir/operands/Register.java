package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Register extends Operand {
    private IntLiteral val;
    private Integer valTag;

    public IntLiteral getVal(Integer valTag) {
        if (valTag.equals(this.valTag))
            return val;
        return null;
    }

    public void setVal(IntLiteral val, Integer valTag) {
        this.val = val;
        if (valTag != null)
            this.valTag = valTag;
    }

    public abstract <T> T accept(IRVisitor<T> visitor);
}
