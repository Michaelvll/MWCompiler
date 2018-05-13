package mwcompiler.frontend;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstBaseVisitor;
import mwcompiler.symbols.*;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.Location;
import mwcompiler.utility.StringProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class ForwardRefPreprocessAstVisitor extends AstBaseVisitor<Void> {
    private SymbolTable currentSymbolTable;
    private Boolean inClass = false;
    private String stage = "Symbol Table Pre-building";

    public void apply(Node node) {
        visit(node);
    }

    private void visit(Node node) {
        node.accept(this);
    }

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

    private Location mainLocation;

    private void checkMain() {
        SymbolInfo mainSymbolIfo = currentSymbolTable
                .findIn(InstanceSymbol.builder("main"));
        if (mainSymbolIfo == null) {
            throw new CompileError(stage, "Main function is needed.", mainLocation);
        } else {
            FunctionTypeSymbol mainTypeSymbol = (FunctionTypeSymbol) mainSymbolIfo.getTypeSymbol();
            if (mainTypeSymbol.getReturnType() != NonArrayTypeSymbol.INT_TYPE_SYMBOL
                    || mainTypeSymbol.getParams().size() != 0) {
                throw new CompileError(stage, "Main function must return int and have no parameters.", mainLocation);
            }
        }
    }

    @Override
    public Void visit(ProgramNode node) {
        currentSymbolTable = new SymbolTable(null);
        node.getBlock().setCurrentSymbolTable(currentSymbolTable);
        initBuiltinFunction();
        visit(node.getBlock());
        currentSymbolTable = node.getBlock().getCurrentSymbolTable();
        checkMain();
        return null;
    }

    @Override
    public Void visit(ClassDeclNode node) {
        inClass = true;
        node.getBody().setCurrentSymbolTable(new SymbolTable(currentSymbolTable));
        if (SymbolTable.getNamedSymbolTable(node.getClassSymbol()) != null) {
            throw new CompileError(stage,
                    "Redeclare class " + StringProcess.getRefString(node.getClassSymbol().getName()),
                    node.getStartLocation());
        }
        SymbolTable.putNamedSymbolTable(node.getClassSymbol(), node.getBody().getCurrentSymbolTable());
        currentSymbolTable = node.getBody().getCurrentSymbolTable();

        visit(node.getBody());
        inClass = false;
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        if (node.getCurrentSymbolTable() == null) {
            currentSymbolTable = new SymbolTable(currentSymbolTable);
            node.setCurrentSymbolTable(currentSymbolTable);
        }
        node.getStatements().forEach(this::visit);
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
        return null;
    }

    @Override
    public Void visit(VariableDeclNode node) {
        if (inClass) {
            SymbolInfo search = currentSymbolTable.findIn(node.getVarSymbol());
            if (search != null) {
                throw new CompileError(stage, "Redeclare a variable "
                        + StringProcess.getRefString(node.getVarSymbol().getName()) + "in the same scope",
                        node.getStartLocation());
            }
            currentSymbolTable.put(node.getVarSymbol(), node.getTypeSymbol());
        }
        return null;
    }

    @Override
    public Void visit(FunctionDeclNode node) {
        if (currentSymbolTable.findIn(node.getInstanceSymbol()) != null) {
            throw new CompileError(stage, "Redeclare function "
                    + StringProcess.getRefString(node.getInstanceSymbol().getName()) + "in the same scope ",
                    node.getStartLocation());
        }
        if (node.getInstanceSymbol().getName().equals("main"))
            mainLocation = node.getStartLocation();
        currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
        if (inClass)
            currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionTypeSymbol());
        return null;
    }
}
