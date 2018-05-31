package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Memory extends MutableOperand {
    private Register baseReg;
    private Register indexReg;
    private int scale;
    private int displacement;

    public Memory(Register baseReg, Operand indexReg, int scale, int displacement) {
        this.baseReg = baseReg;
        if (indexReg instanceof IntLiteral) {
            this.indexReg = null;
            this.scale = 0;
            this.displacement = ((IntLiteral) indexReg).val() * scale + displacement;
        } else {
            this.indexReg = (Register) indexReg;
            this.scale = scale;
            this.displacement = displacement;
        }
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public PhysicalRegister physicalRegister() {
        return null;
    }

    @Override
    public Operand copy(Map<Object, Object> replaceMap) {
        return new Memory((Register) baseReg.copy(replaceMap), indexReg != null ? indexReg.copy(replaceMap) : null, scale, displacement);
    }

    public Register baseReg() {
        return baseReg;
    }

    public void setBaseReg(Register baseReg) {
        this.baseReg = baseReg;
    }

    public Register indexReg() {
        return indexReg;
    }

    public void setIndexReg(Register indexReg) {
        this.indexReg = indexReg;
    }

    public int scale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int disp() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public List<Var> usedVar() {
        List<Var> registers = new ArrayList<>();
        if (baseReg instanceof Var) registers.add((Var) baseReg);
        if (indexReg instanceof Var) registers.add((Var) baseReg);
        return registers;
    }

    public String irName() {
        StringBuilder s = new StringBuilder("[");
        if (baseReg != null) s.append(baseReg.irName());
        if (indexReg != null) s.append(" + ").append(indexReg.irName()).append(" * ").append(scale);
        if (displacement != 0) s.append(displacement > 0 ? " + " : " - ").append(Math.abs(displacement));
        s.append("]");
        return s.toString();
    }

    public String nasmName() {
        StringBuilder s = new StringBuilder("[");
        if (baseReg != null) s.append(baseReg.nasmName());
        if (indexReg != null) s.append(" + ").append(indexReg.nasmName()).append(" * ").append(scale);
        if (displacement != 0) s.append(displacement > 0 ? " + " : " - ").append(Math.abs(displacement));
        s.append("]");
        return s.toString();
    }

    @Override
    public boolean isTmp() {
        return false;
    }
}
