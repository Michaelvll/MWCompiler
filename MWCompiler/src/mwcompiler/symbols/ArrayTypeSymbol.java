package mwcompiler.symbols;


public class ArrayTypeSymbol extends TypeSymbol {
    private NonArrayTypeSymbol nonArrayTypeSymbol;
    private int dim;

    private static String combineName(String name, int dim) {
        return name + "~" + String.valueOf(dim);
    }

    public static ArrayTypeSymbol builder(String name, int dim) {
        NonArrayTypeSymbol nonArrayTypeSymbol = NonArrayTypeSymbol.builder(name);
        TypeSymbol search = typeSymbolMap.get(combineName(name, dim));
        if (search == null) {
            search = new ArrayTypeSymbol(nonArrayTypeSymbol, dim);
            typeSymbolMap.put(combineName(name, dim), search);
        }
        return (ArrayTypeSymbol) search;
    }

    private ArrayTypeSymbol(NonArrayTypeSymbol nonArrayTypeSymbol, int dim) {
        this.nonArrayTypeSymbol = nonArrayTypeSymbol;
        this.dim = dim;
    }

    public NonArrayTypeSymbol getNonArrayTypeSymbol() {
        return nonArrayTypeSymbol;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public SymbolInfo findIn(Instance instance) {
        if (instance == Instance.SIZE) {
            return new SymbolInfo(FunctionSymbol.SIZE);
        }
        throw new RuntimeException("Array type only has <size> function, <" + instance.getName() + ">");
    }


    @Override
    public String getName() {
        return combineName(this.nonArrayTypeSymbol.getName(), dim);
    }

    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getClassSymbolTable(this.nonArrayTypeSymbol);
        if (namedSymbolTable == null) {
            throw new RuntimeException(this.getName());
        }
    }

    @Override
    public boolean isPrimitiveTypeBase(){
        return nonArrayTypeSymbol.isPrimitiveType();
    }
}
