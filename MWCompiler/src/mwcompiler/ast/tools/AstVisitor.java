package mwcompiler.ast.tools;

import mwcompiler.ast.nodes.*;

/**
 * AstVisitor.java
 * Interface for the visitors of the AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public interface AstVisitor {
    void visit(ProgramNode node);

    void visit(ArrayTypeNode node);

    void visit(NonArrayTypeNode node);

    void visit(VariableDeclNode node);

    void visit(FunctionDeclNode node);


    // Expressions
    void visit(BinaryExprNode node);

    void visit(UnaryExprNode node);

    void visit(IdentifierExprNode node);

    void visit(NewExprNode node);

    void visit(NullLiteralNode node);

    // Literals
    void visit(StringLiteralNode node);

    void visit(BoolLiteralNode node);

    void visit(IntLiteralNode node);

    // Block
    void visit(BlockNode node);

    void visit(VoidTypeNode node);

    void visit(ClassDeclNode node);

    void visit(NullTypeNode node);

    void visit(NullExprNode node);

    void visit(FunctionCallNode node);

    void visit(DotMemberNode node);

    void visit(BrackMemberNode node);

    //TODO
}
