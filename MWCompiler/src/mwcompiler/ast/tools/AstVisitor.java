/**
 * AstVisitor.java
 * Interface for the visitors of the AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.tools;

import mwcompiler.ast.*;

public interface AstVisitor {
    public void visit(ProgramNode node);

    public void visit(ArrayTypeNode node);

    public void visit(NonArrayTypeNode node);

    public void visit(VariableDeclNode node);

    public void visit(IdentifierNode node);

    public  void visit(IdentifierExprNode node);

    public void visit(BinaryExprNode node);

    public void visit(StringLiteralNode stringLiteralNode);

    public void visit(BoolLiteralNode boolLiteralNode);

    public void visit(IntLiteralNode intLiteralNode);

    public void visit(NullLiteralNode nullLiteralNode);
    //TODO
}
