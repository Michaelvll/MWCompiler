package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.tools.SSAValue;

public class IRBuilder implements AstVisitor<SSAValue> {
    private BasicBlock startBB;
    private BasicBlock currentBB;

    public BasicBlock build(Node node) {
        visit(node);
        return startBB;
    }

    private SSAValue visit(Node node) {
        return node.accept(this);
    }

    @Override
    public SSAValue visit(ProgramNode node) {
        currentBB = new BasicBlock();
        startBB = currentBB;
        visit(node.getBlock());
        return null;
    }

    @Override
    public SSAValue visit(BlockNode node) {
        for (Node statement : node.getStatements()) {
            visit(statement);
        }
        return null;
    }

    @Override
    public SSAValue visit(VariableDeclNode node) {
        return null;
    }

    @Override
    public SSAValue visit(FunctionDeclNode node) {
        return null;
    }

    @Override
    public SSAValue visit(ClassDeclNode node) {
        return null;
    }

    @Override
    public SSAValue visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public SSAValue visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public SSAValue visit(IdentifierExprNode node) {
        return null;
    }

    @Override
    public SSAValue visit(NewExprNode node) {
        return null;
    }

    @Override
    public SSAValue visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public SSAValue visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public SSAValue visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public SSAValue visit(IntLiteralNode node) {
        return null;
    }

    @Override
    public SSAValue visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public SSAValue visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public SSAValue visit(DotMemberNode node) {
        return null;
    }

    @Override
    public SSAValue visit(BrackMemberNode node) {
        return null;
    }

    @Override
    public SSAValue visit(IfNode node) {
        return null;
    }

    @Override
    public SSAValue visit(LoopNode node) {
        return null;
    }

    @Override
    public SSAValue visit(BreakNode node) {
        return null;
    }

    @Override
    public SSAValue visit(ReturnNode node) {
        return null;
    }

    @Override
    public SSAValue visit(ContinueNode node) {
        return null;
    }

    @Override
    public SSAValue visit(ConstructorCallNode node) {
        return null;
    }
}
