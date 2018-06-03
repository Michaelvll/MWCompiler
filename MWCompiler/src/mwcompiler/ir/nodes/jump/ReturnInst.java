package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReturnInst extends JumpInst {
    private Operand retVal;

    public ReturnInst(Operand retVal) {
        this.retVal = retVal;
    }

    public Operand retVal() {
        return retVal;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<Var> usedVar() {
        List<Var> regs = new ArrayList<>();
        if (retVal instanceof Var) regs.add((Var) retVal);
        else if (retVal instanceof Memory) regs.addAll(((Memory) retVal).usedVar());
        return regs;
    }

    @Override
    public MoveInst copy(Map<Object, Object> replaceMap) {
        if (retVal == null) return null;
        return new MoveInst((MutableOperand) replaceMap.get("retDst"),
                retVal.copy(replaceMap));
    }

    @Override
    public Instruction sameCopy() {
        return new ReturnInst(retVal);
    }
}
