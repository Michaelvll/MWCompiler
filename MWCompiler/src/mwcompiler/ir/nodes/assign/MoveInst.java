package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MoveInst extends AssignInst {
    private Operand val;

    public MoveInst(MutableOperand dst, Operand val) {
        super(dst);
        this.val = val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Operand val() {
        return val;
    }

    public void setVal(Operand val) {
        this.val = val;
    }

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        appendUsedVar(val, registers);
        if (super.dst() instanceof Memory) registers.addAll(((Memory) super.dst()).usedVar());
        return registers;
    }

    @Override
    public AssignInst copy(Map<Object, Object> replaceMap) {
        return new MoveInst((MutableOperand) dst().copy(replaceMap),
                val.copy(replaceMap));
    }
}
