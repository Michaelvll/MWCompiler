package mwcompiler.symbols;


import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {
    private static Dictionary<NonArrayTypeSymbol, SymbolTable> type2SymbolTableMap = new Hashtable<>();
    private Dictionary<VariableSymbol, TypeSymbol> currentMap = new Hashtable<>();
    private SymbolTable outerSymbolTable;

    public SymbolTable(SymbolTable outerSymbolTable) {
        this.outerSymbolTable = outerSymbolTable;
    }

    /**
     * Put entries into symbol table
     *
     * @param variableSymbol
     * @param typeSymbol
     * @return whether the put action overlap the former defined variable symbol in the same scope
     */
    public Boolean put(VariableSymbol variableSymbol, TypeSymbol typeSymbol) {
        TypeSymbol search = currentMap.get(variableSymbol);
        currentMap.put(variableSymbol, typeSymbol);
        return search == null;
    }

    public static void putNamedSymbolTable(NonArrayTypeSymbol nonArrayTypeSymbol, SymbolTable symbolTable){
        type2SymbolTableMap.put(nonArrayTypeSymbol, symbolTable);
    }

    public TypeSymbol findall(VariableSymbol variableSymbol) {
        TypeSymbol search = currentMap.get(variableSymbol);
        if (search == null && outerSymbolTable != null) {
            search = outerSymbolTable.findall(variableSymbol);
        }
        return search;
    }

    public TypeSymbol findin(VariableSymbol variableSymbol) {
        return currentMap.get(variableSymbol);
    }

}
