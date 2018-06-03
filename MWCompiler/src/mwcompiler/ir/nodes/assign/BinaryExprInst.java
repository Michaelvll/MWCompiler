package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.ir.tools.LiteralProcess;
import mwcompiler.utility.ExprOps;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private static AssignInst builder(MutableOperand dst, Operand left, ExprOps op, Operand right) {
        if (left instanceof Literal && right instanceof Literal) {
            Literal literal = LiteralProcess.calc(left, op, right);
            return new MoveInst(dst, literal);
        }
        return new BinaryExprInst(dst, left, op, right);
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
        if (dst() instanceof Memory) appendUsedVar(dst(), registers);
        return registers;
    }

    @Override
    public AssignInst copy(Map<Object, Object> replaceMap) {
//        Operand left = left().copy(replaceMap);
//        Operand right = right().copy(replaceMap);
//        if (left instanceof Literal && right instanceof Literal) {
//            try {
//                replaceMap.put(dst(), LiteralProcess.calc(left, op, right));
//
//            } catch (ArithmeticException e) {
//                System.err.println("Warning: Get arithmetic exception when inlining: " + e.getMessage());
//            }
//            return null;
//        }
        return builder((MutableOperand) dst().dstCopy(replaceMap),
                left.copy(replaceMap),
                op,
                right.copy(replaceMap));
    }

    @Override
    public Instruction sameCopy() {
        return new BinaryExprInst(dst(), left, op, right);
    }

    @Override
    public AssignInst processKnownReg(BasicBlock basicBlock) {
        if (left instanceof Register) {
            Literal leftLiteral = basicBlock.getKnownReg((Register) left);
            if (leftLiteral != null) left = leftLiteral;
        }
        if (right instanceof Register) {
            Literal rightLiteral = basicBlock.getKnownReg((Register) right);
            if (rightLiteral != null) right = rightLiteral;
        }
        if (left instanceof Literal && right instanceof Literal) return builder(dst(), left, op, right);
        return this;
    }
}
