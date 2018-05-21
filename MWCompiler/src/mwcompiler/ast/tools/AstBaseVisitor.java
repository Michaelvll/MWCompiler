package mwcompiler.ast.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.nodes.declarations.ClassDeclNode;
import mwcompiler.ast.nodes.declarations.FunctionDeclNode;
import mwcompiler.ast.nodes.declarations.VariableDeclNode;
import mwcompiler.ast.nodes.expressions.*;
import mwcompiler.ast.nodes.literals.BoolLiteralNode;
import mwcompiler.ast.nodes.literals.IntLiteralNode;
import mwcompiler.ast.nodes.literals.NullLiteralNode;
import mwcompiler.ast.nodes.literals.StringLiteralNode;

public class AstBaseVisitor<T> implements AstVisitor<T> {
    @Override
    public T visit(ProgramNode node) {
        return null;
    }

    @Override
    public T visit(BlockNode node) {
        return null;
    }

    @Override
    public T visit(VariableDeclNode node) {
        return null;
    }

    @Override
    public T visit(FunctionDeclNode node) {
        return null;
    }

    @Override
    public T visit(ClassDeclNode node) {
        return null;
    }

    @Override
    public T visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public T visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public T visit(IdentifierExprNode node) {
        return null;
    }

    @Override
    public T visit(NewExprNode node) {
        return null;
    }

    @Override
    public T visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public T visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public T visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public T visit(IntLiteralNode node) {
        return null;
    }

    @Override
    public T visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public T visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public T visit(DotMemberNode node) {
        return null;
    }

    @Override
    public T visit(BrackMemberNode node) {
        return null;
    }

    @Override
    public T visit(IfNode node) {
        return null;
    }

    @Override
    public T visit(LoopNode node) {
        return null;
    }

    @Override
    public T visit(BreakNode node) {
        return null;
    }

    @Override
    public T visit(ReturnNode node) {
        return null;
    }

    @Override
    public T visit(ContinueNode node) {
        return null;
    }

    @Override
    public T visit(ConstructorCallNode node) {
        return null;
    }
}
