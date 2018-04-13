package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.SymbolTable;

import java.util.List;

/**
 * BlockNode.java
 * Block node extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-11
 */

public class BlockNode extends Node {
    private List<Node> statements;
    private Location location;
    private SymbolTable currentSymbolTable;

    public BlockNode(List<Node> statements, Location location, SymbolTable outerSymbolTable) {
        this.statements = statements;
        this.location = location;
        this.currentSymbolTable = new SymbolTable(outerSymbolTable);
    }

    @Override
    public Location getStartLocation() {
        return location;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public List<Node> getStatements() {
        return statements;
    }

    public Location getLocation() {
        return location;
    }

    public SymbolTable getCurrentSymbolTable() {
        return currentSymbolTable;
    }
}
