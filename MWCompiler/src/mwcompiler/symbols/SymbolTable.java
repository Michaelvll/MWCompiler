package mwcompiler.symbols;


import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {
    private static Dictionary<NonArrayTypeSymbol, SymbolTable> namedSymbolTableMap = new Hashtable<>();
    static {
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("int"), new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("string"), new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("bool"), new SymbolTable(null));
        namedSymbolTableMap.put(NonArrayTypeSymbol.builder("void"), new SymbolTable(null));
    }
    private Dictionary<InstanceSymbol, TypeSymbol> currentMap = new Hashtable<>();
    private SymbolTable outerSymbolTable;

    public SymbolTable(SymbolTable outerSymbolTable) {
        this.outerSymbolTable = outerSymbolTable;
    }

    /**
     * Put entries into symbol table
     *
     * @param instanceSymbol
     * @param typeSymbol
     * @return whether the put action overlap the former defined variable symbol in the same scope
     */
    public Boolean put(InstanceSymbol instanceSymbol, TypeSymbol typeSymbol) {
        TypeSymbol search = currentMap.get(instanceSymbol);
        if (search != null) {
            if (!search.getClass().equals(typeSymbol.getClass())) {
                throw new RuntimeException("ERROR: (Type Checking) Variable and function can not use the same name ");
            }
        }
        currentMap.put(instanceSymbol, typeSymbol);
        return search == null;
    }

    public static void putNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol, SymbolTable symbolTable) {
        namedSymbolTableMap.put(nonArrayTypeSymbol, symbolTable);
    }

    public static SymbolTable getNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol) {
        return namedSymbolTableMap.get(nonArrayTypeSymbol);
    }

    public TypeSymbol findAll(InstanceSymbol instanceSymbol) {
        TypeSymbol search = currentMap.get(instanceSymbol);
        if (search == null && outerSymbolTable != null) {
            search = outerSymbolTable.findAll(instanceSymbol);
        }
        return search;
    }

    public TypeSymbol findIn(InstanceSymbol instanceSymbol) {
        return currentMap.get(instanceSymbol);
    }

    public SymbolTable getOuterSymbolTable() {
        return outerSymbolTable;
    }
}
