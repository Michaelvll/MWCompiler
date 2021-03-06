package mwcompiler.symbols;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Instance {
    public static final Instance MAIN = new Instance("main");
    public static final Instance THIS = new Instance("this");
    public static final Instance CONSTRUCTOR = new Instance("_constructor");
    public static final Instance PRINTF = new Instance("printf");
    public static final Instance PRINT = new Instance("print");
    public static final Instance PRINTLN = new Instance("println");
    public static final Instance GET_STRING = new Instance("getString");
    public static final Instance GET_INT = new Instance("getInt");
    public static final Instance TO_STRING = new Instance("toString");
    public static final Instance SIZE = new Instance("size");
    public static final Instance LENGTH = new Instance("strlen");
    public static final Instance SUBSTRING = new Instance("str_substring");
    public static final Instance PARSE_INT = new Instance("str_parseInt");
    public static final Instance ORD = new Instance("str_ord");

    public static final Set<Instance> builtinSet = new HashSet<>();


    private static Map<String, Instance> instanceSymbolMap = new HashMap<>();

    static {
        instanceSymbolMap.put("main", MAIN);
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

    private Instance(String name) {
        this.name = name;
    }

    public static Instance builder(String name) {
        Instance search = instanceSymbolMap.get(name);
        if (search == null) {
            search = new Instance(name);
            instanceSymbolMap.put(name, search);
        }
        return search;
    }

    public String getName() {
        return name;
    }

}
