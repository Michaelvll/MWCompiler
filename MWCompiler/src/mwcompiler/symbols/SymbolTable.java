package mwcompiler.symbols;


import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.Register;
import mwcompiler.utility.Pair;

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
        symbolTable.put(InstanceSymbol.THIS, nonArrayTypeSymbol);
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
