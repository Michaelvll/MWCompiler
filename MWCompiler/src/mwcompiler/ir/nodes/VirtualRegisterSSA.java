package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.InstanceSymbol;

import java.util.HashMap;
import java.util.Map;

public class VirtualRegisterSSA extends RegisterSSA {
    private String name;
    private Integer id = -1;
    private static Map<String, VirtualRegisterSSA> identifierMap = new HashMap<>();

    private VirtualRegisterSSA(String name) {
        this.name = name;
    }

    private VirtualRegisterSSA(VirtualRegisterSSA ssa) {
        this.name = ssa.name;
        this.id = ssa.id + 1;
    }

    public static VirtualRegisterSSA builder(String preName) {
        VirtualRegisterSSA search = new VirtualRegisterSSA(identifierMap.getOrDefault(preName, new VirtualRegisterSSA(preName)));
        identifierMap.put(preName, search);
        return search;
    }

    public static VirtualRegisterSSA builder(InstanceSymbol symbol) {
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
