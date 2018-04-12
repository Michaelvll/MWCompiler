package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

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

    public BlockNode(List<Node> statements, Location location) {
        this.statements = statements;
        this.location = location;
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
}
