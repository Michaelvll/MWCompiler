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
    public void visit(ProgramNode node);

    public void visit(ArrayTypeNode node);

    public void visit(NonArrayTypeNode node);

    public void visit(VariableDeclNode node);

    public void visit(FunctionDeclNode node);


    // Expressions
    public void visit(BinaryExprNode node);

    public void visit(UnaryExprNode node);

    public void visit(IdentifierExprNode node);

    public void visit(NewExprNode node);

    public void visit(NullLiteralNode node);

    // Literals
    public void visit(StringLiteralNode node);

    public void visit(BoolLiteralNode node);

    public void visit(IntLiteralNode node);

    // Block
    public void visit(BlockNode node);

    public void visit(VoidTypeNode node);
    //TODO
}
