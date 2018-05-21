package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Register extends Operand {
    private Literal val;
    private Integer valTag;
    private Boolean used = false;

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

    public void setUsed() {
        this.used = true;
    }

    public abstract <T> T accept(IRVisitor<T> visitor);
}
