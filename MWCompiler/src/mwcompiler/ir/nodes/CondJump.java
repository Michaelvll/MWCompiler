package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class CondJump extends Jump {
    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return null;
    }
}
