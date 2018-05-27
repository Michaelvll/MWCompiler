package mwcompiler.ir.operands;

public abstract class Literal extends Operand {
    @Override
    public PhysicalRegister physicalRegister() {
        return null;
    }
}
