package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.SymbolInfo;

public abstract class Operand {
    private SymbolInfo symbolInfo;

    public void setSymbolInfo(SymbolInfo symbolInfo) {
        this.symbolInfo = symbolInfo;
    }

    public SymbolInfo getSymbolInfo() {
        return symbolInfo;
    }

    public abstract <T>T accept(IRVisitor<T> visitor);

    public abstract PhysicalRegister physicalRegister();
}
