package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.*;

/**
 * TypeNode.java
 * NonArrayTypeSymbol extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public abstract class TypeNode extends Node {
    //TODO
    protected String typename;
    Location location;

    protected TypeNode(String typename, Location location) {
        this.typename = typename;
        this.location = location;
    }

    public static TypeNode builder(String type, Integer dim, Location pos) {
        if (dim == 0) {
            return new NonArrayTypeNode(type, pos);
        } else {
            return new ArrayTypeNode(type, dim, pos);
        }
    }

    public static TypeNode builder(String type, Location pos) {
        return new NonArrayTypeNode(type, pos);
    }

    public String getTypename() {
        return this.typename;
    }

    public abstract TypeSymbol getSymbol();

    @Override
    public Location getStartLocation() {
        return location;
    }
}
