package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.InstanceSymbol;

public class VirtualRegisterSSA extends RegisterSSA {
    private String name;

    private VirtualRegisterSSA(String name) {
        this.name = name;
    }

    public static VirtualRegisterSSA builder(String preName) {
        return new VirtualRegisterSSA(NameBuilder.builder(preName));
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
