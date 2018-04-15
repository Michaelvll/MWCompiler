package mwcompiler.symbols;

public class InstanceSymbol extends Symbol {
    static {
        symbolMap.put("this", new InstanceSymbol("this"));
        // inner functions
        symbolMap.put("print", new InstanceSymbol( "print"));
        symbolMap.put("println", new InstanceSymbol("println"));
        symbolMap.put("getString", new InstanceSymbol("getString"));
        symbolMap.put("getInt", new InstanceSymbol("getInt"));
        symbolMap.put("toString", new InstanceSymbol("toString"));
        // Array
        symbolMap.put("size", new InstanceSymbol("size"));
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

    public  static InstanceSymbol builder(String name) {
        String intern = name.intern();
        Symbol search = symbolMap.get(intern);
        if (search == null) {
            search = new InstanceSymbol(intern);
            symbolMap.put(intern, search);
        }
        return (InstanceSymbol) search;
    }

    public static InstanceSymbol solveInstanceSymbol(String name){
        String intern = name.intern();
        Symbol search = symbolMap.get(intern);
        if (search == null) {
            throw new RuntimeException("Can not resolve "+name);
        }
        if (!(search instanceof InstanceSymbol)){
            throw new RuntimeException("Class name can not be used as instance name "+ name);
        }
        return (InstanceSymbol) search;
    }

    @Override
    public String getName() {
        return name;
    }
}
