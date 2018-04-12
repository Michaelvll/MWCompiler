package mwcompiler.symbols;


import java.util.Dictionary;
import java.util.Hashtable;

public class SymbolTable {
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

    public TypeSymbol find(VariableSymbol variableSymbol) {
        TypeSymbol search = currentMap.get(variableSymbol);
        if (search == null && outerSymbolTable != null) {
            search = outerSymbolTable.find(variableSymbol);
        }
        return search;
    }

}
