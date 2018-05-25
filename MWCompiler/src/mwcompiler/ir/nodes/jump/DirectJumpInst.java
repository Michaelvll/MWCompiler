package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.tools.IRVisitor;

public class DirectJumpInst extends JumpInst {
    private BasicBlock target;

    public DirectJumpInst(BasicBlock target) {
        this.target = target;
    }

    public BasicBlock getTarget() {
        return target;
    }

    public void setTarget(BasicBlock target) {
        this.target = target;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
