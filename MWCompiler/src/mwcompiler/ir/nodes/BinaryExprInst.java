package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

public class BinaryExprInst extends AssignInst {
    private ExprOps op;
    private Operand left;
    private Operand right;

    public BinaryExprInst(Register dst, Operand left, ExprOps op, Operand right) {
        super(dst);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Operand getLeft() {
        return left;
    }

    public Operand getRight() {
        return right;
    }

    public ExprOps getOp() {
        return op;
    }
}
