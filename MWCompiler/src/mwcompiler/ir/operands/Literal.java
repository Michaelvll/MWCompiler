package mwcompiler.ir.operands;

import java.util.Map;

public abstract class Literal extends Operand {
    @Override
    public PhysicalRegister physicalRegister() {
        return null;
    }

    @Override
    public Operand copy(Map<Object, Object> replaceMap) {
        return this;
    }
}
