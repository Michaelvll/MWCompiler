package mwcompiler.symbols;


import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {
    private static Dictionary<NonArrayTypeSymbol, SymbolTable> type2SymbolTableMap = new Hashtable<>(); //TODO: add inner function to it
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
        currentMap.put(instanceSymbol, typeSymbol);
        return search == null;
    }

    public static void putNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol, SymbolTable symbolTable){
        type2SymbolTableMap.put(nonArrayTypeSymbol, symbolTable);
    }

    public TypeSymbol findall(InstanceSymbol instanceSymbol) {
        TypeSymbol search = currentMap.get(instanceSymbol);
        if (search == null && outerSymbolTable != null) {
            search = outerSymbolTable.findall(instanceSymbol);
        }
        return search;
    }

    public TypeSymbol findin(InstanceSymbol instanceSymbol) {
        return currentMap.get(instanceSymbol);
    }

}
