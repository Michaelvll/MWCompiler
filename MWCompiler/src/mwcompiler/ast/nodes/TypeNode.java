package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;
import mwcompiler.symbols.*;

/**
 * TypeNode.java
 * BaseTypeSymbol extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class TypeNode extends Node {
    private TypeSymbol typeSymbol;
    Location location;

    public TypeNode(String type, int dim, Location pos) {
        if (dim == 0) {
            this.typeSymbol = BaseTypeSymbol.builder(type);
        } else {
            this.typeSymbol = ArrayTypeSymbol.builder(type, dim);
        }
        this.location = pos;
    }


    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    @Override
    public Location getStartLocation() {
        return location;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        //Do Nothing
        return null;
    }
}
