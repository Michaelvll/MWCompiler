package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.IRNode;

public class BuildIR implements AstVisitor<IRNode> {

    @Override
    public IRNode visit(ProgramNode node) {
        return null;
    }

    @Override
    public IRNode visit(BlockNode node) {
        return null;
    }

    @Override
    public IRNode visit(VariableDeclNode node) {
        return null;
    }

    @Override
    public IRNode visit(FunctionDeclNode node) {
        return null;
    }

    @Override
    public IRNode visit(ClassDeclNode node) {
        return null;
    }

    @Override
    public IRNode visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public IRNode visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public IRNode visit(IdentifierExprNode node) {
        return null;
    }

    @Override
    public IRNode visit(NewExprNode node) {
        return null;
    }

    @Override
    public IRNode visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public IRNode visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public IRNode visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public IRNode visit(IntLiteralNode node) {
        return null;
    }

    @Override
    public IRNode visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public IRNode visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public IRNode visit(DotMemberNode node) {
        return null;
    }

    @Override
    public IRNode visit(BrackMemberNode node) {
        return null;
    }

    @Override
    public IRNode visit(IfNode node) {
        return null;
    }

    @Override
    public IRNode visit(LoopNode node) {
        return null;
    }

    @Override
    public IRNode visit(BreakNode node) {
        return null;
    }

    @Override
    public IRNode visit(ReturnNode node) {
        return null;
    }

    @Override
    public IRNode visit(ContinueNode node) {
        return null;
    }

    @Override
    public IRNode visit(ConstructorCallNode node) {
        return null;
    }
}
