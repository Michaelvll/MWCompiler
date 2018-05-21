package mwcompiler.symbols.tools;

import mwcompiler.symbols.TypeSymbol;

// A Tool class for returning type and lvalue or rvalue of statement
public class ExprType {
    public enum ValType {
        LVAL,
        RVAL
    }

    public TypeSymbol typeSymbol;
    public ValType valType;

    public ExprType(TypeSymbol typeSymbol, ValType valType) {
        this.typeSymbol = typeSymbol;
        this.valType = valType;
    }

}
