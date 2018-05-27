package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Var> usedVar() {
        List<Var> regs = new ArrayList<>();
        if (retVal instanceof Var) regs.add((Var) retVal);
        return regs;
    }
}
