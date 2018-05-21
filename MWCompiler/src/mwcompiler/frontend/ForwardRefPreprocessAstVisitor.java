package mwcompiler.frontend;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.nodes.declarations.ClassDeclNode;
import mwcompiler.ast.nodes.declarations.FunctionDeclNode;
import mwcompiler.ast.nodes.declarations.VariableDeclNode;
import mwcompiler.ast.tools.AstBaseVisitor;
import mwcompiler.symbols.*;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.Location;
import mwcompiler.utility.StringProcess;

import static mwcompiler.symbols.SymbolTable.stringSymbolTable;

/**
 * @author Michael Wu
 * @since 2018-04-16
 */
public class ForwardRefPreprocessAstVisitor extends AstBaseVisitor<Void> {
    private SymbolTable currentSymbolTable;
    private Boolean inClass = false;
    private String stage = "Symbol Table Pre-building";
    private String funcPrefix = "";

    public void apply(Node node) {
        visit(node);
    }

    private void visit(Node node) {
        node.accept(this);
    }

    private void initBuiltinFunction() {
        currentSymbolTable.put(InstanceSymbol.PRINT, FunctionSymbol.PRINT);
        currentSymbolTable.put(InstanceSymbol.PRINTLN, FunctionSymbol.PRINTLN);
        currentSymbolTable.put(InstanceSymbol.GET_STRING, FunctionSymbol.GET_STRING);
        currentSymbolTable.put(InstanceSymbol.GET_INT, FunctionSymbol.GET_INT);
        currentSymbolTable.put(InstanceSymbol.TO_STRING, FunctionSymbol.TO_STRING);
        stringSymbolTable.put(InstanceSymbol.LENGTH, FunctionSymbol.LENGTH);
        stringSymbolTable.put(InstanceSymbol.SUBSTRING, FunctionSymbol.SUBSTRING);
        stringSymbolTable.put(InstanceSymbol.PARSE_INT, FunctionSymbol.PARSE_INT);
        stringSymbolTable.put(InstanceSymbol.ORD, FunctionSymbol.ORD);
    }

    private Location mainLocation;

    private void checkMain() {
        SymbolInfo mainSymbolIfo = currentSymbolTable
                .findIn(InstanceSymbol.builder("main"));
        if (mainSymbolIfo == null) {
            throw new CompileError(stage, "Main function is needed.", mainLocation);
        } else {
            FunctionSymbol mainTypeSymbol = (FunctionSymbol) mainSymbolIfo.getTypeSymbol();
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
        funcPrefix = "__" + node.getClassSymbol().getName() + "_";
        node.getBody().setCurrentSymbolTable(new SymbolTable(currentSymbolTable));
        if (SymbolTable.getNamedSymbolTable(node.getClassSymbol()) != null) {
            throw new CompileError(stage,
                    "Redeclare class " + StringProcess.getRefString(node.getClassSymbol().getName()),
                    node.getStartLocation());
        }
        SymbolTable.putNamedSymbolTable(node.getClassSymbol(), node.getBody().getCurrentSymbolTable());
        currentSymbolTable = node.getBody().getCurrentSymbolTable();

        visit(node.getBody());
        funcPrefix = "";
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
        currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionSymbol());
        if (inClass)
            currentSymbolTable.put(node.getInstanceSymbol(), node.getFunctionSymbol());
        node.getFunctionSymbol().addNamePrefix(funcPrefix);
        return null;
    }
}
