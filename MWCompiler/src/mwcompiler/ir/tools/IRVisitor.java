package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;

public interface IRVisitor<T> {
    T visit(VirtualRegisterSSA register);

    T visit(BinaryExpInst binaryExpInst);

    T visit(MoveInst moveInst);

    T visit(IntLiteral intLiteral);

    T visit(BasicBlock block);
}
