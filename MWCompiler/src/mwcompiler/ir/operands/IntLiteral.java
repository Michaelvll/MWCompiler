package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public class IntLiteral extends Literal {
    private Integer val;

    public IntLiteral(Integer val) {
        this.val = val;
    }

    public Integer getVal() {
        return val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
