package mwcompiler.ir.operands;

import java.util.List;
import java.util.Map;

public abstract class MutableOperand extends Operand {
    public abstract boolean isTmp();
    public abstract List<Var> usedVar();

    public abstract Operand dstCopy(Map<Object, Object> replaceMap);
    public abstract String irName();
}
