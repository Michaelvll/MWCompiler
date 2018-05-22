package mwcompiler.symbols;

import mwcompiler.ir.operands.MutableOperand;

public class SymbolInfo {
    private Symbol symbol;
    private MutableOperand operand;

    public SymbolInfo(Symbol symbol) {
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public MutableOperand getOperand() {
        return operand;
    }

    public void setOperand(MutableOperand operand) {
        this.operand = operand;
    }
}
