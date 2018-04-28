package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * ProgramNode.java
 * The root Node of AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public class ProgramNode extends Node {
    private BlockNode block;
    Location location;
    private Location mainLocation;

    public ProgramNode(BlockNode block, Location pos) {
        this.block = block;
        this.location = pos;
    }

    public BlockNode getBlock() {
        return block;
    }

    @Override
    public Location getStartLocation() {
        return location;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Location getMainLocation() {
        return mainLocation;
    }

    public void setMainLocation(Location mainLocation) {
        this.mainLocation = mainLocation;
    }
}
