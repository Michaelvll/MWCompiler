package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

public class BinaryExprInst extends AssignInst {
    private ExprOps op;
    private RegOrImm left;
    private RegOrImm right;

    public BinaryExprInst(Register dst, RegOrImm left, ExprOps op, RegOrImm right) {
        super(dst);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public RegOrImm getLeft() {
        return left;
    }

    public RegOrImm getRight() {
        return right;
    }

    public ExprOps getOp() {
        return op;
    }
}
