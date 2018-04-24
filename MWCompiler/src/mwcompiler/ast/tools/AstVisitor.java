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

    // Block
    void visit(BlockNode node);

    // Type and declarations
    // There is no need to visit TypeNodes as they there will be TypeSymbols in the upper node

    void visit(VariableDeclNode node);

    void visit(FunctionDeclNode node);

    void visit(ClassDeclNode node);


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


    void visit(EmptyExprNode node);

    void visit(FunctionCallNode node);

    void visit(DotMemberNode node);

    void visit(BrackMemberNode node);

    void visit(IfNode node);

    void visit(LoopNode node);

    void visit(BreakNode node);

    void visit(ReturnNode node);

    void visit(ContinueNode node);

    void visit(ConstructorCallNode node);

}
