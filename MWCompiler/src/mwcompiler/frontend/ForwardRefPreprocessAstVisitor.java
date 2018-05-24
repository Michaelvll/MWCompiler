package mwcompiler.frontend;

import mwcompiler.ast.nodes.BlockNode;
import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.nodes.ProgramNode;
import mwcompiler.ast.nodes.declarations.ClassDeclNode;
import mwcompiler.ast.nodes.declarations.FunctionDeclNode;
import mwcompiler.ast.nodes.declarations.VariableDeclNode;
import mwcompiler.ast.tools.AstBaseVisitor;
import mwcompiler.symbols.*;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.Location;
import mwcompiler.utility.StringProcess;

import static mwcompiler.symbols.SymbolTable.STRING_SYMBOL_TABLE;

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
        currentSymbolTable.put(Instance.PRINT, FunctionSymbol.PRINT);
        currentSymbolTable.put(Instance.PRINTLN, FunctionSymbol.PRINTLN);
        currentSymbolTable.put(Instance.GET_STRING, FunctionSymbol.GET_STRING);
        currentSymbolTable.put(Instance.GET_INT, FunctionSymbol.GET_INT);
        currentSymbolTable.put(Instance.TO_STRING, FunctionSymbol.TO_STRING);
        STRING_SYMBOL_TABLE.put(Instance.LENGTH, FunctionSymbol.LENGTH);
        STRING_SYMBOL_TABLE.put(Instance.SUBSTRING, FunctionSymbol.SUBSTRING);
        STRING_SYMBOL_TABLE.put(Instance.PARSE_INT, FunctionSymbol.PARSE_INT);
        STRING_SYMBOL_TABLE.put(Instance.ORD, FunctionSymbol.ORD);
    }

    private Location mainLocation;

    private void checkMain() {
        SymbolInfo mainSymbolInfo = currentSymbolTable
                .findIn(Instance.builder("main"));
        if (mainSymbolInfo == null) {
            throw new CompileError(stage, "Main function is needed.", mainLocation);
        } else {
            FunctionSymbol mainSymbol = (FunctionSymbol) mainSymbolInfo.getSymbol();
            if (mainSymbol.getReturnType() != NonArrayTypeSymbol.INT_TYPE_SYMBOL
                    || mainSymbol.getParams().size() != 0) {
                throw new CompileError(stage, "Main function must return int and have no parameters.", mainLocation);
            }
            FunctionSymbol.MAIN = mainSymbol;
        }
    }

    @Override
    public Void visit(ProgramNode node) {
        currentSymbolTable = new SymbolTable(null);
        node.getBlock().setCurrentSymbolTable(currentSymbolTable);
        SymbolTable.globalSymbolTable = currentSymbolTable;
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
        if (SymbolTable.getClassSymbolTable(node.getClassSymbol()) != null) {
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
        if (currentSymbolTable.findIn(node.getInstance()) != null) {
            throw new CompileError(stage, "Redeclare function "
                    + StringProcess.getRefString(node.getInstance().getName()) + "in the same scope ",
                    node.getStartLocation());
        }
        if (node.getInstance().getName().equals("main"))
            mainLocation = node.getStartLocation();
        currentSymbolTable.put(node.getInstance(), node.getFunctionSymbol());
        if (inClass)
            currentSymbolTable.put(node.getInstance(), node.getFunctionSymbol());
        node.getFunctionSymbol().addNamePrefix(funcPrefix);
        return null;
    }
}
