package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class DirectJumpInst extends JumpInst {
    private BasicBlock target;

    public DirectJumpInst(BasicBlock target) {
        this.target = target;
    }

    public BasicBlock getTarget() {
        return target;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
