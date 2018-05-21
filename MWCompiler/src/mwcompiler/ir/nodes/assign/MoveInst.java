package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.List;

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

    public Operand getVal() {
        return val;
    }

    @Override
    public List<Operand> getOperand() {
        return new ArrayList<Operand>() {{
            add(val);
        }};
    }
}
