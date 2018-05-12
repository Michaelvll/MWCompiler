package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;

public interface IRVisitor<T> {
    T visit(VirtualRegister register);

    T visit(BinaryExpInst binaryExpInst);

    T visit(MoveInst moveInst);

    T visit(IntLiteral intLiteralSSA);

    T visit(BasicBlock block);
}
