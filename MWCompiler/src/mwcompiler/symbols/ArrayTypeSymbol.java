package mwcompiler.symbols;


public class ArrayTypeSymbol extends TypeSymbol {
    private NonArrayTypeSymbol nonArrayTypeSymbol;
    private Integer dim;

    private static String combineName(String name, Integer dim) {
        String combine = name + "~" + String.valueOf(dim);
        return combine.intern();
    }

    public static ArrayTypeSymbol builder(String name, Integer dim) {
        NonArrayTypeSymbol nonArrayTypeSymbol = NonArrayTypeSymbol.builder(name);
        ArrayTypeSymbol arrayTypeSymbol = new ArrayTypeSymbol(nonArrayTypeSymbol, dim);
        symbolMap.put(combineName(name, dim), arrayTypeSymbol);
        return arrayTypeSymbol;
    }

    private ArrayTypeSymbol(NonArrayTypeSymbol nonArrayTypeSymbol, Integer dim) {
        this.nonArrayTypeSymbol = nonArrayTypeSymbol;
        this.dim = dim;
    }


    @Override
    public String getName() {
        return combineName(this.nonArrayTypeSymbol.getName(), dim);
    }
}
