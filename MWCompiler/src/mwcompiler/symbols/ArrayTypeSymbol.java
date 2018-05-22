package mwcompiler.symbols;


public class ArrayTypeSymbol extends TypeSymbol {
    private NonArrayTypeSymbol nonArrayTypeSymbol;
    private Integer dim;

    private static String combineName(String name, Integer dim) {
        return name + "~" + String.valueOf(dim);
    }

    public static ArrayTypeSymbol builder(String name, Integer dim) {
        NonArrayTypeSymbol nonArrayTypeSymbol = NonArrayTypeSymbol.builder(name);
        TypeSymbol search = typeSymbolMap.get(combineName(name, dim));
        if (search == null) {
            search = new ArrayTypeSymbol(nonArrayTypeSymbol, dim);
            typeSymbolMap.put(combineName(name, dim), search);
        }
        return (ArrayTypeSymbol) search;
    }

    private ArrayTypeSymbol(NonArrayTypeSymbol nonArrayTypeSymbol, Integer dim) {
        this.nonArrayTypeSymbol = nonArrayTypeSymbol;
        this.dim = dim;
    }

    public NonArrayTypeSymbol getNonArrayTypeSymbol() {
        return nonArrayTypeSymbol;
    }

    public Integer getDim() {
        return dim;
    }

    @Override
    public SymbolInfo findIn(Instance instance) {
        if (instance == Instance.SIZE) {
            return new SymbolInfo(FunctionSymbol.SIZE);
        }
        throw new RuntimeException("(Type Checking) Array type only has <size> function, <" + instance.getName() + ">");
    }


    @Override
    public String getName() {
        return combineName(this.nonArrayTypeSymbol.getName(), dim);
    }

    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this.nonArrayTypeSymbol);
        if (namedSymbolTable == null) {
            throw new RuntimeException(this.getName());
        }
    }

    @Override
    public Boolean isPrimitiveTypeBase(){
        return nonArrayTypeSymbol.isPrimitiveType();
    }
}
