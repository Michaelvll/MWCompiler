package mwcompiler.symbols;

import java.util.HashMap;
import java.util.Map;

public class InstanceSymbol extends Symbol {
    public static final InstanceSymbol SIZE_FUNCTION_IS = new InstanceSymbol("size");
    public static final InstanceSymbol THIS_IS = new InstanceSymbol("this");
    public static final InstanceSymbol CONSTRUCTOR_IS = new InstanceSymbol("~constructor");


    private static Map<String, InstanceSymbol> instanceSymbolMap = new HashMap<>();

    static {
        instanceSymbolMap.put("this", THIS_IS);
        // inner functions
        instanceSymbolMap.put("print", new InstanceSymbol("print"));
        instanceSymbolMap.put("println", new InstanceSymbol("println"));
        instanceSymbolMap.put("getString", new InstanceSymbol("getString"));
        instanceSymbolMap.put("getInt", new InstanceSymbol("getInt"));
        instanceSymbolMap.put("toString", new InstanceSymbol("toString"));
        // Array
        instanceSymbolMap.put("size", SIZE_FUNCTION_IS);
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
        InstanceSymbol search = instanceSymbolMap.get(name);
        if (search == null) {
            search = new InstanceSymbol(name);
            instanceSymbolMap.put(name, search);
        }
        return search;
    }

    @Override
    public String getName() {
        return name;
    }

}
