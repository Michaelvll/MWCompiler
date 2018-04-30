package mwcompiler.symbols;

import mwcompiler.ir.nodes.RegisterSSA;

public class SymbolInfo {
    private TypeSymbol typeSymbol;
    private RegisterSSA reg;

    public SymbolInfo(TypeSymbol typeSymbol) {
        this.typeSymbol = typeSymbol;
    }

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    public RegisterSSA getReg() {
        return reg;
    }

    public void setReg(RegisterSSA reg) {
        this.reg = reg;
    }
}
