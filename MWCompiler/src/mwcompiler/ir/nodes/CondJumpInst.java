package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.tools.IRVisitor;

public class CondJumpInst extends JumpInst {
    private Operand cond;
    private BasicBlock ifTrue;
    private BasicBlock ifFalse;

    public CondJumpInst(Operand cond, BasicBlock ifTrue, BasicBlock ifFalse) {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public BasicBlock getIfTrue() {
        return ifTrue;
    }

    public BasicBlock getIfFalse() {
        return ifFalse;
    }

    public Operand getCond() {
        return cond;
    }
}
