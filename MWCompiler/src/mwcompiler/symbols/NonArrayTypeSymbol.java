package mwcompiler.symbols;

public class NonArrayTypeSymbol extends TypeSymbol {
    public final static NonArrayTypeSymbol intTypeSymbol = new NonArrayTypeSymbol("int");
    public final static NonArrayTypeSymbol stringTypeSymbol = new NonArrayTypeSymbol("string");
    public final static NonArrayTypeSymbol boolTypeSymbol = new NonArrayTypeSymbol("bool");
    public final static NonArrayTypeSymbol voidTypeSymbol = new NonArrayTypeSymbol("void");
    public final static NonArrayTypeSymbol constructorTypeSymbol = new NonArrayTypeSymbol("");
    public final static NonArrayTypeSymbol nullTypeSymbol = new NonArrayTypeSymbol("null");

    static {
        symbolMap.put("int", intTypeSymbol);
        symbolMap.put("string", stringTypeSymbol);
        symbolMap.put("bool", boolTypeSymbol);
        symbolMap.put("void", voidTypeSymbol);
        symbolMap.put("*Constructor", constructorTypeSymbol);
        symbolMap.put("null", nullTypeSymbol);
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

    public static NonArrayTypeSymbol getConstructorType() {
        return (NonArrayTypeSymbol) symbolMap.get("*Constructor");
    }

    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException(this.typename);
        }
    }

    @Override
    public TypeSymbol findIn(InstanceSymbol instanceSymbol) {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException("No declared class named " + this.typename);
        }
        return namedSymbolTable.findIn(instanceSymbol);
    }
}
