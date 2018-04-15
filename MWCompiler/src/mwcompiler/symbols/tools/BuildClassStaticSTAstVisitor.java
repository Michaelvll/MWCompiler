package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.SymbolTable;

public class BuildClassStaticSTAstVisitor implements AstVisitor {
    private SymbolTable classSymbolTable;

    @Override
    public void visit(ClassDeclNode node) {
        classSymbolTable = new SymbolTable(null);
        SymbolTable.putNamedSymbolTable(node.getClassSymbol(), classSymbolTable);
        node.getBody().accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        for (Node statement : node.getStatements()) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(VariableDeclNode node) {
        classSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());
    }

    @Override
    public void visit(ProgramNode node) {

    }




    @Override
    public void visit(FunctionDeclNode node) {

    }


    @Override
    public void visit(BinaryExprNode node) {

    }

    @Override
    public void visit(UnaryExprNode node) {

    }

    @Override
    public void visit(IdentifierExprNode node) {

    }

    @Override
    public void visit(NewExprNode node) {

    }

    @Override
    public void visit(NullLiteralNode node) {

    }

    @Override
    public void visit(StringLiteralNode node) {

    }

    @Override
    public void visit(BoolLiteralNode node) {

    }

    @Override
    public void visit(IntLiteralNode node) {

    }

    @Override
    public void visit(EmptyExprNode node) {

    }

    @Override
    public void visit(FunctionCallNode node) {

    }

    @Override
    public void visit(DotMemberNode node) {

    }

    @Override
    public void visit(BrackMemberNode node) {

    }

    @Override
    public void visit(IfNode node) {

    }

    @Override
    public void visit(LoopNode node) {

    }

    @Override
    public void visit(BreakNode node) {

    }

    @Override
    public void visit(ReturnNode node) {

    }

    @Override
    public void visit(ContinueNode node) {

    }

    // Unused
}
