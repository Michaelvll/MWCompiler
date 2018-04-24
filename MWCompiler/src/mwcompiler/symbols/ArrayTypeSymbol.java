package mwcompiler.symbols;


import java.util.ArrayList;
import java.util.List;

public class ArrayTypeSymbol extends TypeSymbol {
    private NonArrayTypeSymbol nonArrayTypeSymbol;
    private Integer dim;

    private static String combineName(String name, Integer dim) {
        String combine = name + "~" + String.valueOf(dim);
        return combine.intern();
    }

    public static ArrayTypeSymbol builder(String name, Integer dim) {
        NonArrayTypeSymbol nonArrayTypeSymbol = NonArrayTypeSymbol.builder(name);
        TypeSymbol search = typeSymbolMap.get(combineName(name, dim));
        if (search == null) {
            search = new ArrayTypeSymbol(nonArrayTypeSymbol, dim);
            typeSymbolMap.put(combineName(name,dim),search);
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
    public TypeSymbol findIn(InstanceSymbol instanceSymbol) {
        if (instanceSymbol == InstanceSymbol.sizeFunctionIS) {
            List<TypeSymbol> params = new ArrayList<>();
            return FunctionTypeSymbol.builder(NonArrayTypeSymbol.builder("int"),params);
        }
        throw new RuntimeException("(Type Checking) Array type only has <size> function, <"+instanceSymbol.getName()+">");
    }


    @Override
    public String getName() {
        return combineName(this.nonArrayTypeSymbol.getName(), dim);
    }

    @Override
    public void checkLegal() {
        SymbolTable namedSymbolTable = SymbolTable.getNamedSymbolTable(this.nonArrayTypeSymbol);
        if (namedSymbolTable == null){
            throw new RuntimeException(this.getName());
        }
    }
}
