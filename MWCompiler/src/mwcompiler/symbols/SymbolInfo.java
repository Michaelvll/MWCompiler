package mwcompiler.symbols;

import mwcompiler.ir.operands.MutableOperand;

public class SymbolInfo {
    private Symbol symbol;
    private MutableOperand operand;
    private Integer offset;

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
        operand.setSymbolInfo(this);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isClassMember() {
        return offset != null;
    }
}
