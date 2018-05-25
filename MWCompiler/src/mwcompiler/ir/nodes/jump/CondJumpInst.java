package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.tools.IRVisitor;

public class CondJumpInst extends JumpInst {
    private Operand cond; // if cond != 0 -> ifTrue, else ->ifFalse
    private BasicBlock ifTrue;
    private BasicBlock ifFalse;
    private BinaryExprInst cmp;


    public CondJumpInst(Operand cond, BasicBlock ifTrue, BasicBlock ifFalse) {
        this.cond = cond;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public BasicBlock getIfTrue() {
        return ifTrue;
    }

    public BasicBlock getIfFalse() {
        return ifFalse;
    }

    public Operand getCond() {
        return cond;
    }

    public BinaryExprInst getCmp() {
        return cmp;
    }

    public void setCmp(BinaryExprInst cmp) {
        this.cmp = cmp;
    }

    public void setIfTrue(BasicBlock ifTrue) {
        this.ifTrue = ifTrue;
    }

    public void setIfFalse(BasicBlock ifFalse) {
        this.ifFalse = ifFalse;
    }

    public String getOp() {
        switch (cmp.getOp()) {
            case EQ: return "je";
            case NEQ: return "jne";
            case GT: return "jg";
            case GTE: return "jge";
            case LT: return "jl";
            case LTE: return "jle";
            default: throw new RuntimeException("Compiler Bug: Unknown ExprOp for condition jump");
        }
    }

}
