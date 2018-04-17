package mwcompiler.symbols;

import java.util.Dictionary;
import java.util.Hashtable;

public abstract class Symbol {
    public abstract String getName();

//    public static TypeSymbol searchTypeSymbol(String name) {
//        return typeSymbolMap.get(name.intern());
//    }
//
//    public static InstanceSymbol searchInstanceSymbol(String name){
//        return instanceSymbolMap.get(name.intern());
//    }
}
