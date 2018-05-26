package mwcompiler.symbols;

public class BaseTypeSymbol extends TypeSymbol {
    public final static BaseTypeSymbol INT_TYPE_SYMBOL = new BaseTypeSymbol("int");
    public final static BaseTypeSymbol STRING_TYPE_SYMBOL = new BaseTypeSymbol("string");
    public final static BaseTypeSymbol BOOL_TYPE_SYMBOL = new BaseTypeSymbol("bool");
    public final static BaseTypeSymbol VOID_TYPE_SYMBOL = new BaseTypeSymbol("void");
    public final static BaseTypeSymbol NULL_TYPE_SYMBOL = new BaseTypeSymbol("null");
    public final static BaseTypeSymbol GLOBAL = new BaseTypeSymbol("__global");

    static {
        typeSymbolMap.put("int", INT_TYPE_SYMBOL);
        typeSymbolMap.put("string", STRING_TYPE_SYMBOL);
        typeSymbolMap.put("bool", BOOL_TYPE_SYMBOL);
        typeSymbolMap.put("void", VOID_TYPE_SYMBOL);
        typeSymbolMap.put("null", NULL_TYPE_SYMBOL);
    }

    private String typename;
    private int size = 8;


    private BaseTypeSymbol(String typename) {
        this.typename = typename.intern();
    }


    public String getName() {
        return this.typename;
    }

    public static BaseTypeSymbol builder(String typename) {
        if (typename == null) {
            throw new RuntimeException("Get null typename when building BaseTypeSymbol");
        }
        TypeSymbol search = typeSymbolMap.get(typename);
        if (search == null) {
            search = new BaseTypeSymbol(typename);
            typeSymbolMap.put(typename, search);
        }

        return (BaseTypeSymbol) search;
    }


    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getClassSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException(this.typename);
        }
    }

    @Override
    public SymbolInfo findIn(Instance instance) {
        SymbolTable namedSymbolTable = SymbolTable.getClassSymbolTable(this);
        if (namedSymbolTable == null) {
            throw new RuntimeException("No declared class named " + this.typename);
        }
        return namedSymbolTable.findIn(instance);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isPrimitiveTypeBase() {
        return this == BaseTypeSymbol.INT_TYPE_SYMBOL || this == BaseTypeSymbol.STRING_TYPE_SYMBOL
                || this == BaseTypeSymbol.BOOL_TYPE_SYMBOL || this == BaseTypeSymbol.VOID_TYPE_SYMBOL;
    }
}
