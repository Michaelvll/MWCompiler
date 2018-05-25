package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.List;

public class Memory extends MutableOperand {
    private Register baseReg;
    private Register indexReg;
    private Integer scale;
    private Integer displacement;

    public Memory(Register baseReg, Operand indexReg, Integer scale, Integer displacement) {
        this.baseReg = baseReg;
        if (indexReg instanceof IntLiteral) {
            this.indexReg = null;
            this.scale = 0;
            this.displacement = ((IntLiteral) indexReg).getVal() * scale + displacement;
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

    public Register getBaseReg() {
        return baseReg;
    }

    public void setBaseReg(Register baseReg) {
        this.baseReg = baseReg;
    }

    public Register getIndexReg() {
        return indexReg;
    }

    public void setIndexReg(Register indexReg) {
        this.indexReg = indexReg;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public Integer getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Integer displacement) {
        this.displacement = displacement;
    }

    public List<Register> usedRegister() {
        List<Register> registers = new ArrayList<>();
        if (baseReg != null) registers.add(baseReg);
        if (indexReg != null) registers.add(baseReg);
        return registers;
    }

    public String toString(){
        StringBuilder s =new StringBuilder("[");
        if (baseReg != null) s.append(baseReg.toString());
        if (indexReg != null) s.append(" + ").append(indexReg.toString()).append(" * ").append(scale);
        if (displacement != 0) s.append(" + ").append(displacement);
        s.append("]");
        return s.toString();
    }

    @Override
    public Boolean isTmp() {
        return false;
    }
}
