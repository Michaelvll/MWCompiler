package mwcompiler.symbols.tools;

import mwcompiler.symbols.TypeSymbol;

// A Tool class for returning type and lvalue or rvalue of statement
class ReturnType {
    enum LvalOrRval {
        LVAL,
        RVAL
    }

    TypeSymbol typeSymbol;
    LvalOrRval lvalOrRval;

    ReturnType(TypeSymbol typeSymbol, LvalOrRval lvalOrRval) {
        this.typeSymbol = typeSymbol;
        this.lvalOrRval = lvalOrRval;
    }

}
