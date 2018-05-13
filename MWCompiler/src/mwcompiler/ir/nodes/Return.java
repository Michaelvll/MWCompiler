package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public class Return extends Jump{
    private RegOrImm retVal;

    public Return(RegOrImm retVal) {
        this.retVal = retVal;
    }

    public RegOrImm getRetVal() {
        return retVal;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
