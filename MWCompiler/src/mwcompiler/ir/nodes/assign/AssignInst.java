package mwcompiler.ir.nodes.assign;


import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;

import java.util.LinkedList;
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

    public abstract List<Register> usedRegister();

    void appendUsedRegister(Operand operand, LinkedList<Register> registers) {
        if (operand instanceof Register) registers.addLast((Register) operand);
        else if (operand instanceof Memory) registers.addAll(((Memory) operand).usedRegister());
    }
}
