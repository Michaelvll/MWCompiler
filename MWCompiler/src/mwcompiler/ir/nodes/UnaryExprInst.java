package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

public class UnaryExprInst extends AssignInst {
    Operand src;
    ExprOps op;
    public UnaryExprInst(Register dst, ExprOps op, Operand src) {
        super(dst);
        this.op = op;
        this.src = src;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return null;
    }
}
