package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.operands.MutableOperand;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

import java.util.LinkedList;
import java.util.List;

public class BinaryExprInst extends AssignInst {
    private ExprOps op;
    private Operand left;
    private Operand right;

    public BinaryExprInst(MutableOperand dst, Operand left, ExprOps op, Operand right) {
        super(dst);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Operand left() {
        return left;
    }

    public Operand right() {
        return right;
    }

    public ExprOps op() {
        return op;
    }

    public void notOp() {
        this.op = op.not();
    }

    public boolean isCompare() {
        return op.isCompare();
    }

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        appendUsedVar(left, registers);
        appendUsedVar(right, registers);
        return registers;
    }

}
