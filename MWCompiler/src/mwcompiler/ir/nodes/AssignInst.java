package mwcompiler.ir.nodes;


import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;

import java.util.List;

public abstract class AssignInst extends Instruction {
    private Register dst;

    public AssignInst(Register dst) {
        this.dst = dst;
    }

    public void setDst(Register dst) {
        this.dst = dst;
    }

    public Register getDst() {
        return dst;
    }

    public abstract List<Operand> getOperand();
}
