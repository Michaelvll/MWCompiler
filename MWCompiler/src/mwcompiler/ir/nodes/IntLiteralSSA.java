package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class IntLiteralSSA extends SSA {
    private Integer val;

    public IntLiteralSSA(Integer val) {
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
