package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.StringLiteral;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.CompilerOptions;

public class CodeGenerator implements IRVisitor<Void> {
    private CompilerOptions options;
    public CodeGenerator(CompilerOptions options) {
        this.options = options;
    }

    @Override
    public Void visit(VirtualRegister register) {
        return null;
    }

    @Override
    public Void visit(BinaryExprInst binaryExprInst) {
        return null;
    }

    @Override
    public Void visit(IntLiteral intLiteral) {
        return null;
    }

    @Override
    public Void visit(Memory memory) {
        return null;
    }

    @Override
    public Void visit(BasicBlock block) {
        return null;
    }

    @Override
    public Void visit(ReturnInst inst) {
        return null;
    }

    @Override
    public Void visit(Function inst) {
        return null;
    }

    @Override
    public Void visit(MoveInst inst) {
        return null;
    }

    @Override
    public Void visit(CondJumpInst inst) {
        return null;
    }

    @Override
    public Void visit(DirectJumpInst inst) {
        return null;
    }

    @Override
    public Void visit(FunctionCallInst inst) {
        return null;
    }

    @Override
    public Void visit(StringLiteral inst) {
        return null;
    }
}
