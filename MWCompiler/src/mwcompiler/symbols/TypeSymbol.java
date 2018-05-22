package mwcompiler.symbols;

import java.util.HashMap;
import java.util.Map;

public abstract class TypeSymbol extends Symbol {
    static Map<String, TypeSymbol> typeSymbolMap = new HashMap<>();

    public static TypeSymbol searchSymbol(String name) {
        return typeSymbolMap.get(name);
    }



    public Boolean isPrimitiveTypeBase() {
        return false;
    }

    public Boolean isPrimitiveType() {
        return this == NonArrayTypeSymbol.INT_TYPE_SYMBOL || this == NonArrayTypeSymbol.STRING_TYPE_SYMBOL
                || this == NonArrayTypeSymbol.BOOL_TYPE_SYMBOL || this == NonArrayTypeSymbol.VOID_TYPE_SYMBOL;
    }
}
