package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Wu
 * @since 2018-04-16
 * */
public class ForwardRefPreprocessAstVisitor implements AstVisitor {
    private SymbolTable currentSymbolTable;
    private Boolean inClass = false;


    private FunctionTypeSymbol getFunctionType(String returnType, String... params) {
        TypeSymbol returnTypeSymbol = NonArrayTypeSymbol.builder(returnType);
        List<TypeSymbol> paramList = new ArrayList<>();
        for (String param : params) {
            paramList.add(NonArrayTypeSymbol.builder(param));
        }
        return FunctionTypeSymbol.builder(returnTypeSymbol, paramList);
    }

    // Put builtin functions into Symbol Table
    private void putBuiltin(String returnType, String name, String... params) {
        currentSymbolTable.put(InstanceSymbol.builder(name), getFunctionType(returnType, params));
    }

    private void initBuiltinFunction() {
        putBuiltin("void", "print", "string");
        putBuiltin("void", "println", "string");
        putBuiltin("string", "getString");
        putBuiltin("int", "getInt");
        putBuiltin("string", "toString", "int");
        SymbolTable stringSymbolTable = SymbolTable.getNamedSymbolTable(NonArrayTypeSymbol.builder("string"));
        stringSymbolTable.put(InstanceSymbol.builder("length"), getFunctionType("int"));
        stringSymbolTable.put(InstanceSymbol.builder("substring"), getFunctionType("string", "int", "int"));
        stringSymbolTable.put(InstanceSymbol.builder("parseInt"), getFunctionType("int"));
        stringSymbolTable.put(InstanceSymbol.builder("ord"), getFunctionType("int", "int"));
    }

    private void checkMain() {
        FunctionTypeSymbol mainTypeSymbol = (FunctionTypeSymbol) currentSymbolTable.findIn(InstanceSymbol.builder("main"));
        if (mainTypeSymbol == null) {
            throw new RuntimeException("ERROR: (Type Checking) Main function is needed.");
        } else if (mainTypeSymbol.getReturnType() != NonArrayTypeSymbol.intTypeSymbol || mainTypeSymbol.getParams().size() != 0) {
            throw new RuntimeException("ERROR: (Type Checking) Main function must return int and have no parameters.");
        }
    }

    @Override
    public void visit(ProgramNode node) {
        currentSymbolTable = new SymbolTable(null);
        node.getBlock().setCurrentSymbolTable(currentSymbolTable);
        initBuiltinFunction();
        node.getBlock().accept(this);
        currentSymbolTable = node.getBlock().getCurrentSymbolTable();
        checkMain();
    }

    @Override
    public void visit(ClassDeclNode node) {
        inClass = true;
        node.getBody().setCurrentSymbolTable(new SymbolTable(currentSymbolTable));
        if (SymbolTable.getNamedSymbolTable(node.getClassSymbol()) != null) {
            throw new RuntimeException("ERROR: (Type Checking) Redeclare class <" + node.getClassSymbol().getName() + "> "
                    + node.getStartLocation().getLocation());
        }
        SymbolTable.putNamedSymbolTable(node.getClassSymbol(), node.getBody().getCurrentSymbolTable());
        currentSymbolTable = node.getBody().getCurrentSymbolTable();

        node.getBody().accept(this);
        inClass = false;
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
        if (inClass) {
            TypeSymbol search = currentSymbolTable.findIn(node.getVarSymbol());
            if (search != null) {
                throw new RuntimeException("ERROR: (Type Checking) Redeclare a variable <"
                        + node.getVarSymbol().getName() + "> in the same scope" + node.getStartLocation().getLocation());
            }
            currentSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());
        }
    }

    @Override
    public void visit(FunctionDeclNode node) {
        if (currentSymbolTable.findIn(node.getInstanceSymbol()) != null) {
            throw new RuntimeException("ERROR: (Type Checking) Redeclare function <" + node.getInstanceSymbol().getName()
                    + "> in the same scope " + node.getStartLocation().getLocation());
        }
        currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
        if (inClass)
            currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
    }

    // Unused
    @Override
    public void visit(ConstructorCallNode node) {

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
