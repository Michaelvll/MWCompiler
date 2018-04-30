package mwcompiler.symbols.tools;

import mwcompiler.symbols.TypeSymbol;

// A Tool class for returning type and lvalue or rvalue of statement
public class ExprType {
    public enum LvalOrRval {
        LVAL,
        RVAL
    }

    public TypeSymbol typeSymbol;
    public LvalOrRval lvalOrRval;

    public ExprType(TypeSymbol typeSymbol, LvalOrRval lvalOrRval) {
        this.typeSymbol = typeSymbol;
        this.lvalOrRval = lvalOrRval;
    }

}
