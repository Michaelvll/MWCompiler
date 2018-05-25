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

    public Var(Instance symbol, SymbolTable symbolTable) {
        this.name = symbol.getName();
        this.symbolTable = symbolTable;
    }

    private static Map<String, Var> identifierMap = new HashMap<>();

    public static Var builder(String preName) {
        String name = preName + "_tmp";
        Var search = identifierMap.get(name);
        if (search == null) search = new Var(name, 0);
        else search = new Var(name, search.id + 1);
        identifierMap.put(name, search);
        return search;
    }


    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "$" + name + "_" + ((symbolTable != null) ? symbolTable.hashCode() : id);
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
