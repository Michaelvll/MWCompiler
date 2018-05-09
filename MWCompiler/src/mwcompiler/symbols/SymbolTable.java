package mwcompiler.symbols;


import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private static Map<NonArrayTypeSymbol, SymbolTable> namedSymbolTableMap = new HashMap<>();

    static {
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("int"), new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("string"), new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("bool"), new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("void"), new SymbolTable(null));
    }

    private Map<InstanceSymbol, SymbolInfo> currentMap = new HashMap<>();
    private SymbolTable outerSymbolTable;

    public SymbolTable(SymbolTable outerSymbolTable) {
        this.outerSymbolTable = outerSymbolTable;
    }

    /**
     * Put entries into symbol table
     *
     * @param instanceSymbol
     * @param typeSymbol
     */
    public void put(InstanceSymbol instanceSymbol, TypeSymbol typeSymbol) {
        SymbolInfo search = currentMap.get(instanceSymbol);
        if (search != null) {
            if (!search.getTypeSymbol().equals(typeSymbol))
                throw new RuntimeException("Variable and function can not use the same name ");
        } else
            currentMap.put(instanceSymbol, new SymbolInfo(typeSymbol));
    }

    public static void putNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol, SymbolTable symbolTable) {
        symbolTable.put(InstanceSymbol.THIS_IS, nonArrayTypeSymbol);
        namedSymbolTableMap.put(nonArrayTypeSymbol, symbolTable);
    }

    public static SymbolTable getNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol) {
        return namedSymbolTableMap.get(nonArrayTypeSymbol);
    }

    public SymbolInfo findAll(InstanceSymbol instanceSymbol) {
        SymbolInfo search = currentMap.get(instanceSymbol);
        if (search == null && outerSymbolTable != null) {
            search = outerSymbolTable.findAll(instanceSymbol);
        }
        return search;
    }

    public SymbolInfo findIn(InstanceSymbol instanceSymbol) {
        return currentMap.get(instanceSymbol);
    }

    public SymbolTable getOuterSymbolTable() {
        return outerSymbolTable;
    }
}
