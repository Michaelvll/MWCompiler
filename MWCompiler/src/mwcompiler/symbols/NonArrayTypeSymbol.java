package mwcompiler.symbols;

public class NonArrayTypeSymbol extends TypeSymbol {
    public final static NonArrayTypeSymbol INT_TYPE_SYMBOL = new NonArrayTypeSymbol("int", true);
    public final static NonArrayTypeSymbol STRING_TYPE_SYMBOL = new NonArrayTypeSymbol("string", true);
    public final static NonArrayTypeSymbol BOOL_TYPE_SYMBOL = new NonArrayTypeSymbol("bool", true);
    public final static NonArrayTypeSymbol VOID_TYPE_SYMBOL = new NonArrayTypeSymbol("void", true);
    public final static NonArrayTypeSymbol NULL_TYPE_SYMBOL = new NonArrayTypeSymbol("null", true);

    static {
        typeSymbolMap.put("int", INT_TYPE_SYMBOL);
        typeSymbolMap.put("string", STRING_TYPE_SYMBOL);
        typeSymbolMap.put("bool", BOOL_TYPE_SYMBOL);
        typeSymbolMap.put("void", VOID_TYPE_SYMBOL);
        typeSymbolMap.put("null", NULL_TYPE_SYMBOL);
    }

    private String typename;
    private Boolean isPrimaryType;


    private NonArrayTypeSymbol(String typename, Boolean isPrimaryType) {
        this.typename = typename.intern();
        this.isPrimaryType = isPrimaryType;
    }


    public String getName() {
        return this.typename;
    }

    public static NonArrayTypeSymbol builder(String typename) {
        if (typename == null) {
            throw new RuntimeException("Get null typename when building NonArrayTypeSymbol");
        }
        TypeSymbol search = typeSymbolMap.get(typename);
        if (search == null) {
            search = new NonArrayTypeSymbol(typename, false);
            typeSymbolMap.put(typename, search);
        }

        return (NonArrayTypeSymbol) search;
    }


    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException(this.typename);
        }
    }

    @Override
    public SymbolInfo findIn(InstanceSymbol instanceSymbol) {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException("No declared class named " + this.typename);
        }
        return namedSymbolTable.findIn(instanceSymbol);
    }

    public Boolean isPrimaryType() {
        return isPrimaryType;
    }
}
