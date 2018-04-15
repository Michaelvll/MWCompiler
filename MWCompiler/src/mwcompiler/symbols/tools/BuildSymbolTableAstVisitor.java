package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.SymbolTable;

public class BuildSymbolTableAstVisitor implements AstVisitor {
    private SymbolTable currentSymbolTable = null;

    @Override
    public void visit(ProgramNode node) {
        currentSymbolTable = new SymbolTable(currentSymbolTable);
        node.getBlock().accept(this);
    }

    @Override
    public void visit(BlockNode node) {
        node.setCurrentSymbolTable(currentSymbolTable);
        for (Node statement : node.getStatements()) {
            statement.accept(this);
        }
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();

    }

    @Override
    public void visit(VariableDeclNode node) {
    }

    @Override
    public void visit(FunctionDeclNode node) {
        currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
        currentSymbolTable = new SymbolTable(currentSymbolTable);
        for (VariableDeclNode param : node.getParamList()) {
            currentSymbolTable.put(param.getVarSymbol(), param.getTypeSymbol());
        }

    }

    @Override
    public void visit(ClassDeclNode node) {
        node.getBody().accept(this);
    }

    // Unused


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
}
