package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;

import java.util.ArrayList;
import java.util.List;

public class ConstructSymbolTableAstVisitor implements AstVisitor {
    private SymbolTable currentSymbolTable;
    private SymbolTable staticClassSymbolTable;


    private FunctionTypeSymbol getFunctionType(String returnType, String... params) {
        TypeSymbol returnTypeSymbol = NonArrayTypeSymbol.builder(returnType);
        List<TypeSymbol> paramList = new ArrayList<>();
        for (String param : params) {
            paramList.add(NonArrayTypeSymbol.builder(param));
        }
        return FunctionTypeSymbol.builder(returnTypeSymbol, paramList);
    }

    private void putInner(String returnType, String name, String... params) {
        currentSymbolTable.put(InstanceSymbol.builder(name), getFunctionType(returnType, params));
    }

    private void initInnerFunction() {
        putInner("void", "print", "string");
        putInner("void", "println", "string");
        putInner("string", "getString");
        putInner("int", "getInt");
        putInner("string", "toString", "int");
        SymbolTable stringSymbolTable = SymbolTable.getNamedSymbolTable(NonArrayTypeSymbol.builder("string"));
        stringSymbolTable.put(InstanceSymbol.builder("length"), getFunctionType("int"));
        stringSymbolTable.put(InstanceSymbol.builder("substring"), getFunctionType("string", "int", "int"));
        stringSymbolTable.put(InstanceSymbol.builder("parseInt"), getFunctionType("int"));
        stringSymbolTable.put(InstanceSymbol.builder("ord"), getFunctionType("int", "int"));
    }

    @Override
    public void visit(ProgramNode node) {
        currentSymbolTable = new SymbolTable(null);
        node.getBlock().setCurrentSymbolTable(currentSymbolTable);
        initInnerFunction();
        node.getBlock().accept(this);
    }

    @Override
    public void visit(ClassDeclNode node) {
        staticClassSymbolTable = new SymbolTable(null);
        SymbolTable.putNamedSymbolTable(node.getClassSymbol(), staticClassSymbolTable);
        node.getBody().accept(this);
        staticClassSymbolTable = null;
    }

    @Override
    public void visit(BlockNode node) {
        if (node.getCurrentSymbolTable() == null) {
            currentSymbolTable = new SymbolTable(currentSymbolTable);
            node.setCurrentSymbolTable(currentSymbolTable);
        }
        for (Node statement : node.getStatements()) {
            statement.accept(this);
        }
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
    }

    @Override
    public void visit(VariableDeclNode node) {
        if (staticClassSymbolTable != null)
            staticClassSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());
    }

    @Override
    public void visit(FunctionDeclNode node) {
        if (currentSymbolTable.findIn(node.getInstanceSymbol()) != null) {
            throw new RuntimeException("ERROR: (Type Checking) Redeclare function <"+ node.getInstanceSymbol().getName()
                    +"> in the same scope "+node.getStartLocation().getLocation());
        }
        currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
        if (staticClassSymbolTable != null)
            staticClassSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
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

    // Unused
}
