package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.*;

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

    private CondJumpInst(Operand cond, BinaryExprInst cmp, BasicBlock ifTrue, BasicBlock ifFalse) {
        this.cond = cond;
        this.cmp = cmp;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<Var> usedVar() {
        return cmp.usedVar();
    }

    public BasicBlock ifTrue() {
        return ifTrue;
    }

    public BasicBlock ifFalse() {
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

    public void not() {
        cmp.notOp();
        BasicBlock tmp = ifTrue;
        ifTrue = ifFalse;
        ifFalse = tmp;
    }

    @Override
    public JumpInst copy(Map<Object, Object> replaceMap) {
        AssignInst cmpInst = cmp.copy(replaceMap);
        if (cmpInst instanceof MoveInst) {
            IntLiteral val = (IntLiteral) ((MoveInst) cmpInst).val();
            if (val.val() == 1) return new DirectJumpInst(ifTrue.copy(replaceMap));
            return new DirectJumpInst(ifFalse.copy(replaceMap));
        }
        return new CondJumpInst((Operand) replaceMap.getOrDefault(cond, cond),
                (BinaryExprInst) cmpInst,
                ifTrue.copy(replaceMap),
                ifFalse.copy(replaceMap));

    }
}
