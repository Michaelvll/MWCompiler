package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.BinaryExpr;
import mwcompiler.ir.nodes.VirtualRegister;

public class IRDumper implements IRVisitor<Void> {
    @Override
    public Void visit(VirtualRegister register) {
        return null;
    }

    @Override
    public Void visit(BinaryExpr binaryExpr) {
        return null;
    }
}
