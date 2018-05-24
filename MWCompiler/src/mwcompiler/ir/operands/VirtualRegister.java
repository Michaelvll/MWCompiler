package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.Instance;
import mwcompiler.symbols.SymbolTable;

import java.util.HashMap;
import java.util.Map;

public class VirtualRegister extends Register {
    private String name;
    private SymbolTable symbolTable;
    private Integer id = -1;

    private VirtualRegister(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public VirtualRegister(Instance symbol, SymbolTable symbolTable) {
        this.name = symbol.getName();
        this.symbolTable = symbolTable;
    }

    private static Map<String, VirtualRegister> identifierMap = new HashMap<>();

    public static VirtualRegister builder(String preName) {
        String name = preName + "_tmp";
        VirtualRegister search = identifierMap.get(name);
        if (search == null) {
            search = new VirtualRegister(name, 0);
        } else {
            search = new VirtualRegister(name, search.id + 1);
        }
        identifierMap.put(name, search);
        return search;
    }


    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return id == -1 ? name : name + "." + String.valueOf(id);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Boolean isTmp() {
        return id >= 0;
    }
}
