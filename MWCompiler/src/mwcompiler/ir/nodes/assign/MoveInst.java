package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MoveInst extends AssignInst {
    private Operand val;

    public MoveInst(MutableOperand dst, Operand val) {
        super(dst);
        this.val = val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Operand val() {
        return val;
    }

    public void setVal(Operand val) {
        this.val = val;
    }

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        appendUsedVar(val, registers);
        if (super.dst() instanceof Memory) registers.addAll(((Memory) super.dst()).usedVar());
        return registers;
    }

    @Override
    public AssignInst copy(Map<Object, Object> replaceMap) {
//        Operand newDst = dst().copy(replaceMap);
//        Operand newVal = val.copy(replaceMap);
//        if (newDst instanceof Literal || newVal instanceof Literal) {
//            replaceMap.put(dst(), val.copy(replaceMap));
//            return null;
//        }
        return new MoveInst((MutableOperand) dst().dstCopy(replaceMap),
                val.copy(replaceMap));
    }

    @Override
    public AssignInst sameCopy() {
        return new MoveInst(dst(), val);
    }

    @Override
    public AssignInst processKnownReg(BasicBlock basicBlock) {
        if (val instanceof Register) {
            Literal valLiteral = basicBlock.getKnownReg((Register) val);
            if (valLiteral != null) val = valLiteral;
        }
        return this;
    }
}
