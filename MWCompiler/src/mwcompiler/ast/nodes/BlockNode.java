package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.Location;

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

    public BlockNode(List<Node> statements, Location location) {
        this.statements = statements;
        this.location = location;
    }

    @Override
    public Location getStartLocation() {
        return location;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
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

    public void setCurrentSymbolTable(SymbolTable currentSymbolTable) {
        this.currentSymbolTable = currentSymbolTable;
    }

    public void clearStatement(Integer i) {
        statements = statements.subList(0, i);
    }
}
