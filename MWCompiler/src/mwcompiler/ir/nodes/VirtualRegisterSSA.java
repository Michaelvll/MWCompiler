package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.InstanceSymbol;

import java.util.HashMap;
import java.util.Map;

public class VirtualRegisterSSA extends RegisterSSA {
    private static Map<String, Integer> preTextMap = new HashMap<>();

    private String name;

    private VirtualRegisterSSA(String name) {
        this.name = name;
    }

    public static VirtualRegisterSSA builder(String preName) {
        Integer suf = preTextMap.put(preName, preTextMap.getOrDefault(preName, -1) + 1);
        return new VirtualRegisterSSA(preName + "_" + String.valueOf(suf));
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
