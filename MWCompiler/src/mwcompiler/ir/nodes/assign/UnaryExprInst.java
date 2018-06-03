package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.ExprOps;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UnaryExprInst extends AssignInst {
    Operand src;
    ExprOps op;

    public UnaryExprInst(MutableOperand dst, ExprOps op, Operand src) {
        super(dst);
        this.op = op;
        this.src = src;
    }

    public Operand src() {
        return src;
    }

    public ExprOps op() {
        return op;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        appendUsedVar(src, registers);
        return registers;
    }

    @Override
    public AssignInst copy(Map<Object, Object> replaceMap) {
        return new UnaryExprInst((MutableOperand) dst().dstCopy(replaceMap), op,
                src.copy(replaceMap));
    }

    @Override
    public AssignInst sameCopy() {
        return new UnaryExprInst(dst(), op, src);
    }

    @Override
    public AssignInst processKnownReg(BasicBlock basicBlock) {
        if (src instanceof Register) {
            Literal srcVal = basicBlock.getKnownReg((Register) src);
            if (srcVal != null) src = srcVal;
        }
        return this;
    }
}
