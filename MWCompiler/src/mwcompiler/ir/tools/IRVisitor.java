package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.StringLiteral;
import mwcompiler.ir.operands.VirtualRegister;

public interface IRVisitor<T> {
    T visit(VirtualRegister register);

    T visit(BinaryExprInst binaryExprInst);

    T visit(IntLiteral intLiteralSSA);

    T visit(BasicBlock block);

    T visit(ReturnInst inst);

    T visit(Function inst);

    T visit(MoveInst inst);

    T visit(CondJumpInst inst);

    T visit(DirectJumpInst inst);

    T visit(FunctionCallInst inst);

    T visit(StringLiteral inst);
}
