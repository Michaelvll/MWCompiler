package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.InstanceSymbol;

import java.util.HashMap;
import java.util.Map;

public class VirtualRegister extends Register {
    private static Map<String, Integer> preTextMap = new HashMap<>();

    private String name;

    private VirtualRegister(String name) {
        this.name = name;
    }

    public static VirtualRegister builder(String preName) {
        Integer suf = preTextMap.put(preName, preTextMap.getOrDefault(preName, -1) + 1);
        return new VirtualRegister(preName + "_" + String.valueOf(suf));
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
