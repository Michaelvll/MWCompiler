package mwcompiler.symbols.tools;

import mwcompiler.symbols.Symbol;

// A Tool class for returning type and lvalue or rvalue of statement
public class ExprType {
    public enum ValType {
        LVAL,
        RVAL
    }

    public Symbol symbol;
    public ValType valType;

    public ExprType(Symbol symbol, ValType valType) {
        this.symbol = symbol;
        this.valType = valType;
    }

}
