package mwcompiler.symbols;


import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private static Map<NonArrayTypeSymbol, SymbolTable> namedSymbolTableMap = new HashMap<>();
    public static final SymbolTable stringSymbolTable = new SymbolTable(null);

    static {
        namedSymbolTableMap.put(NonArrayTypeSymbol.INT_TYPE_SYMBOL, new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.STRING_TYPE_SYMBOL, stringSymbolTable);
        namedSymbolTableMap.put(NonArrayTypeSymbol.BOOL_TYPE_SYMBOL, new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.VOID_TYPE_SYMBOL, new SymbolTable(null));
    }

    private Map<Instance, SymbolInfo> currentMap = new HashMap<>();
    private SymbolTable outerSymbolTable;

    public SymbolTable(SymbolTable outerSymbolTable) {
        this.outerSymbolTable = outerSymbolTable;
    }

    /**
     * Put entries into symbol table
     *
     * @param instance
     * @param typeSymbol
     */
    public void put(Instance instance, Symbol typeSymbol) {
        SymbolInfo search = currentMap.get(instance);
        if (search != null) {
            if (!search.getSymbol().equals(typeSymbol))
                throw new RuntimeException("Variable and function can not use the same name ");
        } else
            currentMap.put(instance, new SymbolInfo(typeSymbol));
    }

    public static void putNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol, SymbolTable symbolTable) {
        symbolTable.put(Instance.THIS, nonArrayTypeSymbol);
        namedSymbolTableMap.put(nonArrayTypeSymbol, symbolTable);
    }

    public static SymbolTable getNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol) {
        return namedSymbolTableMap.get(nonArrayTypeSymbol);
    }

    public SymbolInfo findAll(Instance instance) {
        SymbolInfo search = currentMap.get(instance);
        if (search == null && outerSymbolTable != null) {
            search = outerSymbolTable.findAll(instance);
        }
        return search;
    }

    public SymbolInfo findIn(Instance instance) {
        return currentMap.get(instance);
    }

    public SymbolTable getOuterSymbolTable() {
        return outerSymbolTable;
    }

    public Integer getVariableDeclSize() {
        Integer cnt = 0;
        for (SymbolInfo info : currentMap.values()) {
            if (!(info.getSymbol() instanceof FunctionSymbol)) ++cnt;
        }
        return cnt;
    }
}
