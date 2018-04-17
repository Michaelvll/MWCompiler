package mwcompiler.symbols;

import java.util.Dictionary;
import java.util.Hashtable;

public abstract class Symbol {
    static Dictionary<String, Symbol> symbolMap = new Hashtable<>();
    public abstract String getName();

    public static Symbol searchSymbol(String name) {
        return symbolMap.get(name.intern());
    }
}
