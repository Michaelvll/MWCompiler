package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.nodes.Node;
import mwcompiler.symbols.TypeSymbol;
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
    private TypeSymbol type;

    protected ExprNode(Location pos) {
        location = pos;
    }

    public void setType(TypeSymbol type) {
        this.type = type;
    }

    public TypeSymbol type() {
        return type;
    }

    @Override
    public Location location() {
        return location;
    }

}
