package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MoveInst extends AssignInst {
    private Operand src;

    public MoveInst(MutableOperand dst, Operand src) {
        super(dst);
        this.src = src;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Operand val() {
        return src;
    }

    public void setSrc(Operand src) {
        this.src = src;
    }

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        appendUsedVar(src, registers);
        if (super.dst() instanceof Memory) appendUsedVar(dst(), registers);
        return registers;
    }

    @Override
    public AssignInst copy(Map<Object, Object> replaceMap) {
//        Operand newDst = dst().copy(replaceMap);
//        Operand newVal = src.copy(replaceMap);
//        if (newDst instanceof Literal || newVal instanceof Literal) {
//            replaceMap.put(dst(), src.copy(replaceMap));
//            return null;
//        }
        return new MoveInst((MutableOperand) dst().dstCopy(replaceMap),
                src.copy(replaceMap));
    }

    @Override
    public AssignInst sameCopy() {
        return new MoveInst(dst(), src);
    }

    @Override
    public AssignInst processKnownReg(BasicBlock basicBlock) {
        if (src instanceof Register) {
            Literal valLiteral = basicBlock.getKnownReg((Register) src);
            if (valLiteral != null) src = valLiteral;
        }
        return this;
    }
}
