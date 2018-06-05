package mwcompiler.symbols;


public class ArrayTypeSymbol extends TypeSymbol {
    private BaseTypeSymbol baseTypeSymbol;
    private int dim;

    private static String combineName(String name, int dim) {
        return name + "~" + String.valueOf(dim);
    }

    public static ArrayTypeSymbol builder(String name, int dim) {
        BaseTypeSymbol baseTypeSymbol = BaseTypeSymbol.builder(name);
        TypeSymbol search = typeSymbolMap.get(combineName(name, dim));
        if (search == null) {
            search = new ArrayTypeSymbol(baseTypeSymbol, dim);
            typeSymbolMap.put(combineName(name, dim), search);
        }
        return (ArrayTypeSymbol) search;
    }

    private ArrayTypeSymbol(BaseTypeSymbol baseTypeSymbol, int dim) {
        this.baseTypeSymbol = baseTypeSymbol;
        this.dim = dim;
    }

    public BaseTypeSymbol getBaseTypeSymbol() {
        return baseTypeSymbol;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public SymbolInfo findIn(Instance instance) {
        if (instance == Instance.SIZE) {
            return new SymbolInfo(FunctionSymbol.SIZE);
        }
        throw new RuntimeException("Array type only has <size> callee, <" + instance.getName() + ">");
    }


    @Override
    public String getName() {
        return combineName(this.baseTypeSymbol.getName(), dim);
    }

    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getClassSymbolTable(this.baseTypeSymbol);
        if (namedSymbolTable == null) {
            throw new RuntimeException(this.getName());
        }
    }

    @Override
    public boolean isPrimitiveTypeBase(){
        return baseTypeSymbol.isPrimitiveType();
    }
}
