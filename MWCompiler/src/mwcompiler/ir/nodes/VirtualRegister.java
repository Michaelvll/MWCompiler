package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.InstanceSymbol;

import java.util.HashMap;
import java.util.Map;

public class VirtualRegister extends Register {
    private String name;
    private Integer id = -1;
    private static Map<String, VirtualRegister> identifierMap = new HashMap<>();

    private VirtualRegister(String name) {
        this.name = name;
    }

    private VirtualRegister(VirtualRegister ssa) {
        this.name = ssa.name;
        this.id = ssa.id + 1;
    }

    public static VirtualRegister builder(String preName) {
        VirtualRegister search = new VirtualRegister(identifierMap.getOrDefault(preName, new VirtualRegister(preName)));
        identifierMap.put(preName, search);
        return search;
    }

    public static VirtualRegister builder(InstanceSymbol symbol) {
        return builder(symbol.getName());
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getName() {
        return name;
    }
}
