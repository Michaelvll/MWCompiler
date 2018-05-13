package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;

public interface IRVisitor<T> {
    T visit(VirtualRegister register);

    T visit(BinaryExprInst binaryExprInst);

    T visit(IntLiteral intLiteralSSA);

    T visit(BasicBlock block);

    T visit(Return inst);

    T visit(Function function);

    T visit(MoveInst moveInst);
}
