package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Register extends MutableOperand {
    private Literal val;
    private Integer valTag;

    public Literal getVal(Integer valTag) {
        if (valTag.equals(this.valTag))
            return val;
        return null;
    }

    public void setVal(Literal val, Integer valTag) {
        this.val = val;
        if (valTag != null)
            this.valTag = valTag;
    }

    public abstract <T> T accept(IRVisitor<T> visitor);
}
