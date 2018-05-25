package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.Instance;
import mwcompiler.symbols.SymbolTable;

import java.util.HashMap;
import java.util.Map;

public class Var extends Register {
    private String name;
    private SymbolTable symbolTable;
    private Integer id = -1;
    private Boolean isGlobal = false;

    private Var(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    private Var(Instance symbol, SymbolTable symbolTable) {
        this.name = symbol.getName();
        this.symbolTable = symbolTable;
    }

    private static Map<String, Integer> idMap = new HashMap<>();

    public static Var tmpBuilder(String preName) {
        String name = preName + "_tmp";
        Integer search = idMap.get(name);
        Var newVar;
        if (search == null) newVar = new Var(name, 0);
        else newVar = new Var(name, search + 1);
        idMap.put(name, newVar.id);
        return newVar;
    }

    public static Var builder(Instance symbol, SymbolTable symbolTable) {
        Var var = new Var(symbol, symbolTable);
        symbolTable.addVar(var);
        return var;
    }


    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "$" + name + "_" + (isTmp() ? id : symbolTable.hashCode());
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public Boolean isTmp() {
        return id >= 0;
    }

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal() {
        isGlobal = true;
    }
}
