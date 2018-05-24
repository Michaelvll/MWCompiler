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

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Boolean isClassMember() {
        return offset != null;
    }
}
