package mwcompiler.symbols;

public class InstanceSymbol extends Symbol {
    public static final InstanceSymbol sizeFunctionIS = new InstanceSymbol("size");
    public static final InstanceSymbol thisInstanceSybol = new InstanceSymbol("this");
    static {
        symbolMap.put("this", thisInstanceSybol);
        // inner functions
        symbolMap.put("print", new InstanceSymbol( "print"));
        symbolMap.put("println", new InstanceSymbol("println"));
        symbolMap.put("getString", new InstanceSymbol("getString"));
        symbolMap.put("getInt", new InstanceSymbol("getInt"));
        symbolMap.put("toString", new InstanceSymbol("toString"));
        // Array
        symbolMap.put("size", sizeFunctionIS);
        // String
        symbolMap.put("length", new InstanceSymbol("length"));
        symbolMap.put("substring",new InstanceSymbol("substring"));
        symbolMap.put("parseInt", new InstanceSymbol("parseInt"));
        symbolMap.put("ord", new InstanceSymbol("ord"));

    }
    private String name;

    private InstanceSymbol(String name) {
        this.name = name;
    }

    public static InstanceSymbol builder(String name) {
        String intern = name.intern();
        Symbol search = symbolMap.get(intern);
        if (search == null) {
            search = new InstanceSymbol(intern);
            symbolMap.put(intern, search);
        }
        return (InstanceSymbol) search;
    }

    @Override
    public String getName() {
        return name;
    }


}
