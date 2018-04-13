package mwcompiler.symbols;



public class ArrayTypeSymbol extends TypeSymbol {
    private NonArrayTypeSymbol nonArrayTypeSymbol;
    private Integer dim;

    private static String combineName(String name, Integer dim) {
        String combine = name + "~" + String.valueOf(dim);
        return combine.intern();
    }

    public static ArrayTypeSymbol builder(String name, Integer dim) {
        NonArrayTypeSymbol nonArrayTypeSymbol = NonArrayTypeSymbol.getSymbol(name);
        if (nonArrayTypeSymbol == null) {
            throw new RuntimeException("Declaring array for unknown typename");
        }
        ArrayTypeSymbol arrayTypeSymbol = new ArrayTypeSymbol(nonArrayTypeSymbol, dim);
        symbolMap.put(combineName(name, dim), arrayTypeSymbol);
        return arrayTypeSymbol;
    }

    private ArrayTypeSymbol(NonArrayTypeSymbol nonArrayTypeSymbol, Integer dim) {
        this.nonArrayTypeSymbol = nonArrayTypeSymbol;
        this.dim = dim;
    }

    public static ArrayTypeSymbol getSymbol(String name, Integer dim) {
        Symbol search = symbolMap.get(combineName(name, dim));
        if (search == null) {
            search = builder(name, dim);
        }
        return (ArrayTypeSymbol) search;
    }


    @Override
    public String getName() {
        return combineName(this.nonArrayTypeSymbol.getName(), dim);
    }
}
