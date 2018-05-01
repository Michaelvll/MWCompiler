package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class BinaryExpInst extends Instruction {

    public BinaryExpInst(RegisterSSA target, RegisterSSA left, RegisterSSA right) {
        super();
        super.setTarget(target);
        super.setLeft(left);
        super.setRight(right);
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
