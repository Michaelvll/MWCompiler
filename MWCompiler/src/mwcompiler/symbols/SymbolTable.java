package mwcompiler.symbols;


import mwcompiler.ir.operands.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private static Map<BaseTypeSymbol, SymbolTable> classSymbolTableMap = new HashMap<>();
    public static final SymbolTable STRING_SYMBOL_TABLE = new SymbolTable(null);
    public static SymbolTable globalSymbolTable;

    static {
        classSymbolTableMap.put(BaseTypeSymbol.INT_TYPE_SYMBOL, new SymbolTable(null));
        classSymbolTableMap.put(BaseTypeSymbol.STRING_TYPE_SYMBOL, STRING_SYMBOL_TABLE);
        classSymbolTableMap.put(BaseTypeSymbol.BOOL_TYPE_SYMBOL, new SymbolTable(null));
        classSymbolTableMap.put(BaseTypeSymbol.VOID_TYPE_SYMBOL, new SymbolTable(null));
    }

    private Map<Instance, SymbolInfo> currentMap = new HashMap<>();
    private SymbolTable parentSymbolTable;
    private List<Var> varList = new ArrayList<>();
    private List<SymbolTable> children = new ArrayList<>();

    private SymbolTable(SymbolTable parentSymbolTable) {
        this.parentSymbolTable = parentSymbolTable;
    }

    public static SymbolTable builder(SymbolTable outerSymbolTable) {
        SymbolTable symbolTable = new SymbolTable(outerSymbolTable);
        if (outerSymbolTable != null) outerSymbolTable.addChild(symbolTable);
        return symbolTable;
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
                throw new RuntimeException("Variable and callee can not use the same name ");
        } else {
            SymbolInfo symbolInfo = new SymbolInfo(typeSymbol);
            currentMap.put(instance, symbolInfo);
        }
    }

    public static void putNamedSymbolTable(BaseTypeSymbol baseTypeSymbol, SymbolTable symbolTable) {
        symbolTable.put(Instance.THIS, baseTypeSymbol);
        classSymbolTableMap.put(baseTypeSymbol, symbolTable);
    }

    public static SymbolTable getClassSymbolTable(BaseTypeSymbol baseTypeSymbol) {
        return classSymbolTableMap.get(baseTypeSymbol);
    }

    public SymbolInfo findAll(Instance instance) {
        SymbolInfo search = currentMap.get(instance);
        if (search == null && parentSymbolTable != null) {
            search = parentSymbolTable.findAll(instance);
        }
        return search;
    }

    public SymbolInfo findIn(Instance instance) {
        return currentMap.get(instance);
    }

    public SymbolTable getParentSymbolTable() {
        return parentSymbolTable;
    }

    public void addVar(Var var) {
        varList.add(var);
    }

    public void addChild(SymbolTable symbolTable) {
        children.add(symbolTable);
    }

    public List<SymbolTable> getChildren(){
        return children;
    }

    public List<Var> getVarList() {
        return varList;
    }
}
