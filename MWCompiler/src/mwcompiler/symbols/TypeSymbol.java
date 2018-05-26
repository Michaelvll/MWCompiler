package mwcompiler.symbols;

import java.util.HashMap;
import java.util.Map;

public abstract class TypeSymbol extends Symbol {
    static Map<String, TypeSymbol> typeSymbolMap = new HashMap<>();

    public static TypeSymbol searchSymbol(String name) {
        return typeSymbolMap.get(name);
    }

    public BaseTypeSymbol getBaseType() {
        return this instanceof BaseTypeSymbol ? (BaseTypeSymbol) this : ((ArrayTypeSymbol) this).getBaseTypeSymbol();
    }

    public abstract boolean isPrimitiveTypeBase();

    public boolean isPrimitiveType() {
        return this == BaseTypeSymbol.INT_TYPE_SYMBOL || this == BaseTypeSymbol.STRING_TYPE_SYMBOL
                || this == BaseTypeSymbol.BOOL_TYPE_SYMBOL || this == BaseTypeSymbol.VOID_TYPE_SYMBOL;
    }
}
