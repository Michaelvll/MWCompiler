package mwcompiler.symbols;

import mwcompiler.ir.operands.Register;

public class SymbolInfo {
    private TypeSymbol typeSymbol;
    private Register reg;

    public SymbolInfo(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    public Register getReg() {
        return reg;
    }

    public void setReg(Register reg) {
        this.reg = reg;
    }
}
