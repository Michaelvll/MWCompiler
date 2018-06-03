package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectJumpInst extends JumpInst {
    private BasicBlock target;

    public DirectJumpInst(BasicBlock target) {
        this.target = target;
    }

    public BasicBlock target() {
        return target;
    }

    public void setTarget(BasicBlock target) {
        this.target = target;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public List<Var> usedVar() {
        return new ArrayList<>();
    }

    @Override
    public JumpInst copy(Map<Object, Object> replaceMap) {
        return new DirectJumpInst(target.copy(replaceMap));
    }

    @Override
    public JumpInst sameCopy() {
        return new DirectJumpInst(target);
    }
}
