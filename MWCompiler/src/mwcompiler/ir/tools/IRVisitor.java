package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.BinaryExpr;
import mwcompiler.ir.nodes.VirtualRegister;

public interface IRVisitor<T> {
    T visit(VirtualRegister register);

    T visit(BinaryExpr binaryExpr);
}
