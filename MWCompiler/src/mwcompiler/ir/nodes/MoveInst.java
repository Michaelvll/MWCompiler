package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class MoveInst extends Instruction {

    public MoveInst(RegisterSSA target, SSA value) {
        super();
        super.setTarget(target);
        super.setLeft(value);
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}