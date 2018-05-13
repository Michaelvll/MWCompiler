package mwcompiler.ast.nodes;

import mwcompiler.utility.Location;

/**
 * ExprNode.java
 * Expression extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public abstract class ExprNode extends Node {
    private Location location;

    ExprNode(Location pos) {
        location = pos;
    }


    @Override
    public Location getStartLocation() {
        return location;
    }

}
