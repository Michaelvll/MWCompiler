package mwcompiler.symbols;

public class NonArrayTypeSymbol extends TypeSymbol {
    static {
        symbolMap.put("int", new NonArrayTypeSymbol("int"));
        symbolMap.put("string", new NonArrayTypeSymbol("string"));
        symbolMap.put("bool", new NonArrayTypeSymbol("bool"));
        symbolMap.put("void", new NonArrayTypeSymbol("void"));
        symbolMap.put("*Constructor", new NonArrayTypeSymbol(""));
    }

    protected String typename;

    private NonArrayTypeSymbol(String typename) {
        this.typename = typename;
    }


    public String getName() {
        return this.typename;
    }

    public static NonArrayTypeSymbol builder(String typename) {
        if (typename == null) {
            throw new RuntimeException("Get null typename when building NonArrayTypeSymbol");
        }
        String intern = typename.intern();
        Symbol search = symbolMap.get(intern);
        if (search == null) {
            search = new NonArrayTypeSymbol(typename);
            symbolMap.put(typename.intern(), search);
        }
        return (NonArrayTypeSymbol) search;
    }
    public static NonArrayTypeSymbol getConstructorType(){
        return (NonArrayTypeSymbol) symbolMap.get("*Constructor");
    }
}
