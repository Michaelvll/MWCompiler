package mwcompiler.ir.operands;

import java.util.Map;

public abstract class MutableOperand extends Operand {
    public abstract boolean isTmp();

    public abstract Operand dstCopy(Map<Object, Object> replaceMap);
}
