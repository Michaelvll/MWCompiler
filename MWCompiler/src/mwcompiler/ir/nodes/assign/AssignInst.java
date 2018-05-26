package mwcompiler.ir.nodes.assign;


import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.*;

import java.util.*;

public abstract class AssignInst extends Instruction {
    private MutableOperand dst;

    public AssignInst(MutableOperand dst) {
        this.dst = dst;
    }

    public void setDst(MutableOperand dst) {
        this.dst = dst;
    }

    public MutableOperand dst() {
        return dst;
    }

    public boolean isCompare() {
        return this instanceof BinaryExprInst && this.isCompare();
    }

    void appendUsedVar(Operand operand, LinkedList<Var> registers) {
        if (operand instanceof Var) registers.addLast((Var) operand);
        else if (operand instanceof Memory) registers.addAll(((Memory) operand).usedVar());
    }

    @Override
    public List<Var> dstVar() {
        if (dst instanceof Var) return new ArrayList<>(Collections.singleton((Var) dst));
        return new ArrayList<>();
    }
}
