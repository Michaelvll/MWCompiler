package mwcompiler.ir.nodes.assign;


import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Operand;

import java.util.List;

public abstract class AssignInst extends Instruction {
    private MutableOperand dst;

    public AssignInst(MutableOperand dst) {
        this.dst = dst;
    }

    public void setDst(MutableOperand dst) {
        this.dst = dst;
    }

    public MutableOperand getDst() {
        return dst;
    }

    public abstract List<Operand> getOperand();
}
