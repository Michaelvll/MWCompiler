package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.tools.IRVisitor;

public class ReturnInst extends JumpInst {
    private Operand retVal;

    public ReturnInst(Operand retVal) {
        this.retVal = retVal;
    }

    public Operand getRetVal() {
        return retVal;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
