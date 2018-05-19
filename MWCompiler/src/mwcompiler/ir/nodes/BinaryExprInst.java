package mwcompiler.ir.nodes;

import edu.emory.mathcs.backport.java.util.Arrays;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Operand> getOperand() {
        return new ArrayList<Operand>(){{
            add(left);
            add(right);
        }};
    }
}
