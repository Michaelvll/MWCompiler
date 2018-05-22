package mwcompiler.symbols;

public class NonArrayTypeSymbol extends TypeSymbol {
    public final static NonArrayTypeSymbol INT_TYPE_SYMBOL = new NonArrayTypeSymbol("int");
    public final static NonArrayTypeSymbol STRING_TYPE_SYMBOL = new NonArrayTypeSymbol("string");
    public final static NonArrayTypeSymbol BOOL_TYPE_SYMBOL = new NonArrayTypeSymbol("bool");
    public final static NonArrayTypeSymbol VOID_TYPE_SYMBOL = new NonArrayTypeSymbol("void");
    public final static NonArrayTypeSymbol NULL_TYPE_SYMBOL = new NonArrayTypeSymbol("null");

    static {
        typeSymbolMap.put("int", INT_TYPE_SYMBOL);
        typeSymbolMap.put("string", STRING_TYPE_SYMBOL);
        typeSymbolMap.put("bool", BOOL_TYPE_SYMBOL);
        typeSymbolMap.put("void", VOID_TYPE_SYMBOL);
        typeSymbolMap.put("null", NULL_TYPE_SYMBOL);
    }

    private String typename;
    private Integer size = 8;


    private NonArrayTypeSymbol(String typename) {
        this.typename = typename.intern();
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
            search = new NonArrayTypeSymbol(typename);
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
    public SymbolInfo findIn(Instance instance) {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException("No declared class named " + this.typename);
        }
        return namedSymbolTable.findIn(instance);
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean isPrimitiveTypeBase() {
        return this == NonArrayTypeSymbol.INT_TYPE_SYMBOL || this == NonArrayTypeSymbol.STRING_TYPE_SYMBOL
                || this == NonArrayTypeSymbol.BOOL_TYPE_SYMBOL || this == NonArrayTypeSymbol.VOID_TYPE_SYMBOL;
    }
}
