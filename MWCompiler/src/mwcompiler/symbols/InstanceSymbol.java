package mwcompiler.symbols;

import java.util.Dictionary;
import java.util.Hashtable;

public class InstanceSymbol extends Symbol {
    static Dictionary<String, InstanceSymbol> instanceSymbolMap = new Hashtable<>();

    public static final InstanceSymbol sizeFunctionIS = new InstanceSymbol("size");
    public static final InstanceSymbol thisInstanceSymbol = new InstanceSymbol("this");
    public static final InstanceSymbol constructorSymbol = new InstanceSymbol("~constructor");
    static {
        instanceSymbolMap.put("this", thisInstanceSymbol);
        // inner functions
        instanceSymbolMap.put("print", new InstanceSymbol("print"));
        instanceSymbolMap.put("println", new InstanceSymbol("println"));
        instanceSymbolMap.put("getString", new InstanceSymbol("getString"));
        instanceSymbolMap.put("getInt", new InstanceSymbol("getInt"));
        instanceSymbolMap.put("toString", new InstanceSymbol("toString"));
        // Array
        instanceSymbolMap.put("size", sizeFunctionIS);
        // String
        instanceSymbolMap.put("length", new InstanceSymbol("length"));
        instanceSymbolMap.put("substring", new InstanceSymbol("substring"));
        instanceSymbolMap.put("parseInt", new InstanceSymbol("parseInt"));
        instanceSymbolMap.put("ord", new InstanceSymbol("ord"));

    }
    private String name;

    private InstanceSymbol(String name) {
        this.name = name;
    }

    public static InstanceSymbol builder(String name) {
        String intern = name.intern();
        InstanceSymbol search = instanceSymbolMap.get(intern);
        if (search == null) {
            search = new InstanceSymbol(intern);
            instanceSymbolMap.put(intern, search);
        }
        return search;
    }

    @Override
    public String getName() {
        return name;
    }

}
