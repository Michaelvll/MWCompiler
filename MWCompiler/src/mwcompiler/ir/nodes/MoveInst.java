package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class MoveInst extends AssignInst {
    private RegOrImm val;
    public MoveInst(Register dst, RegOrImm val) {
        super(dst);
        this.val = val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return  visitor.visit(this);
    }

    public RegOrImm getVal() {
        return val;
    }
}
