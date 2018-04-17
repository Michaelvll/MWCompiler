package mwcompiler.symbols.tools;

import mwcompiler.symbols.TypeSymbol;

public class ReturnType {
    enum LvalOrRval{
        LVAL,
        RVAL
    }
    public TypeSymbol typeSymbol;
    public LvalOrRval lvalOrRval;

    public ReturnType(TypeSymbol typeSymbol, LvalOrRval lvalOrRval) {
        this.typeSymbol = typeSymbol;
        this.lvalOrRval = lvalOrRval;
    }

    public Boolean typeEquals(ReturnType returnType) {
        return typeSymbol == returnType.typeSymbol;
    }

    public Boolean typeEquals(TypeSymbol typeSymbol) {
        return this.typeSymbol == typeSymbol;
    }

    public Boolean equals(ReturnType returnType) {
        return typeSymbol==returnType.typeSymbol && lvalOrRval == returnType.lvalOrRval;
    }
    public Boolean equals(TypeSymbol typeSymbol, LvalOrRval lvalOrRval) {
        return this.typeSymbol == typeSymbol && this.lvalOrRval == lvalOrRval;
    }
}
