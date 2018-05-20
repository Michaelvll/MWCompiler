package mwcompiler.symbols;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InstanceSymbol extends Symbol {
    public static final InstanceSymbol THIS = new InstanceSymbol("this");
    public static final InstanceSymbol CONSTRUCTOR = new InstanceSymbol("~constructor");
    public static final InstanceSymbol PRINT = new InstanceSymbol("printf");
    public static final InstanceSymbol PRINTLN = new InstanceSymbol("printf");
    public static final InstanceSymbol GET_STRING = new InstanceSymbol("getString");
    public static final InstanceSymbol GET_INT = new InstanceSymbol("getInt");
    public static final InstanceSymbol TO_STRING = new InstanceSymbol("toString");
    public static final InstanceSymbol SIZE = new InstanceSymbol("size");
    public static final InstanceSymbol LENGTH = new InstanceSymbol("length");
    public static final InstanceSymbol SUBSTRING = new InstanceSymbol("substring");
    public static final InstanceSymbol PARSE_INT = new InstanceSymbol("parseInt");
    public static final InstanceSymbol ORD = new InstanceSymbol("ord");

    public static final Set<InstanceSymbol> builtinSet = new HashSet<>();


    private static Map<String, InstanceSymbol> instanceSymbolMap = new HashMap<>();

    static {
        instanceSymbolMap.put("this", THIS);
        // inner functions
        instanceSymbolMap.put("print", PRINT);
        instanceSymbolMap.put("println", PRINTLN);
        instanceSymbolMap.put("getString", GET_STRING);
        instanceSymbolMap.put("getInt", GET_INT);
        instanceSymbolMap.put("toString", TO_STRING);
        // Array
        instanceSymbolMap.put("size", SIZE);
        // String
        instanceSymbolMap.put("length", LENGTH);
        instanceSymbolMap.put("substring", SUBSTRING);
        instanceSymbolMap.put("parseInt", PARSE_INT);
        instanceSymbolMap.put("ord", ORD);
        builtinSet.add(PRINT);
        builtinSet.add(PRINTLN);
        builtinSet.add(GET_STRING);
        builtinSet.add(GET_INT);
        builtinSet.add(TO_STRING);
        builtinSet.add(SIZE);
        builtinSet.add(LENGTH);
        builtinSet.add(SUBSTRING);
        builtinSet.add(PARSE_INT);
        builtinSet.add(ORD);
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
