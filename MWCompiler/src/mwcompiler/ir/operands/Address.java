package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

public class Address extends MutableOperand {
    private Register baseReg;
    private Register indexReg;
    private Integer scale;
    private Integer displacement;

    public Address(Register baseReg, Register indexReg, Integer scale, Integer displacement) {
        this.baseReg = baseReg;
        this.indexReg = indexReg;
        this.scale = scale;
        this.displacement = displacement;
    }
    public Address(Register baseReg, Register indexReg, IntLiteral scale, IntLiteral displacement) {
        this.baseReg = baseReg;
        this.indexReg = indexReg;
        this.scale = scale.getVal();
        this.displacement = displacement.getVal();
    }


    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
