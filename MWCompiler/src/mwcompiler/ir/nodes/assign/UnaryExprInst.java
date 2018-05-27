package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        appendUsedVar(src, registers);
        return registers;
    }
}
