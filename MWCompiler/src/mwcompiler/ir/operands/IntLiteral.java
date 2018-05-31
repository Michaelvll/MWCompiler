package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public class IntLiteral extends Literal {
    private int val;

    public IntLiteral(int val) {
        this.val = val;
    }

    public int val() {
        return val;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IntLiteral && ((IntLiteral) obj).val() == val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public static final IntLiteral ONE_LITERAL = new IntLiteral(1);
    public static final IntLiteral ZERO_LITERAL = new IntLiteral(0);
}
