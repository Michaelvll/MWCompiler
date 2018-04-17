package mwcompiler.symbols;

public abstract class TypeSymbol extends Symbol {
    public abstract void checkLegal();
    public abstract TypeSymbol findIn(InstanceSymbol instanceSymbol);
    public Boolean isPrimitiveType(){
        return this == NonArrayTypeSymbol.intTypeSymbol || this == NonArrayTypeSymbol.stringTypeSymbol
                || this == NonArrayTypeSymbol.boolTypeSymbol || this == NonArrayTypeSymbol.voidTypeSymbol;
    }


    public Boolean equals(TypeSymbol obj) {
        return this == obj;
    }
}
