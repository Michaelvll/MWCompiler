package mwcompiler.symbols;

import java.util.Dictionary;
import java.util.Hashtable;

public abstract class TypeSymbol extends Symbol {
    static Dictionary<String, TypeSymbol> typeSymbolMap = new Hashtable<>();

    public static TypeSymbol searchSymbol(String name) {
        return typeSymbolMap.get(name.intern());
    }

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
