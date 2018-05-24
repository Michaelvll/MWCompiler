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

/**
 * AstVisitor.java
 * Interface for the visitors of the AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public interface AstVisitor<ReturnType> {
    ReturnType visit(ProgramNode node);

    // Block
    ReturnType visit(BlockNode node);

    // Type and declarations
    // There is no need to visit TypeNodes as they there will be TypeSymbols in the upper node

    ReturnType visit(VariableDeclNode node);

    ReturnType visit(FunctionDeclNode node);

    ReturnType visit(ClassDeclNode node);


    // Expressions
    ReturnType visit(BinaryExprNode node);

    ReturnType visit(UnaryExprNode node);

    ReturnType visit(IdentifierExprNode node);

    ReturnType visit(NewExprNode node);

    ReturnType visit(NullLiteralNode node);

    // Literals
    ReturnType visit(StringLiteralNode node);

    ReturnType visit(BoolLiteralNode node);

    ReturnType visit(IntLiteralNode node);


    ReturnType visit(EmptyExprNode node);

    ReturnType visit(FunctionCallNode node);

    ReturnType visit(DotMemberNode node);

    ReturnType visit(BrackMemberNode node);

    ReturnType visit(IfNode node);

    ReturnType visit(LoopNode node);

    ReturnType visit(BreakNode node);

    ReturnType visit(ReturnNode node);

    ReturnType visit(ContinueNode node);

    ReturnType visit(ConstructorCallNode node);

}
