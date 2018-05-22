package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.StringLiteral;
import mwcompiler.ir.operands.VirtualRegister;

public interface IRVisitor<T> {
    T visit(VirtualRegister register);

    T visit(BinaryExprInst binaryExprInst);

    T visit(IntLiteral intLiteral);

    T visit(Memory memory);

    T visit(BasicBlock block);

    T visit(ReturnInst inst);

    T visit(Function inst);

    T visit(MoveInst inst);

    T visit(CondJumpInst inst);

    T visit(DirectJumpInst inst);

    T visit(FunctionCallInst inst);

    T visit(StringLiteral inst);

}
