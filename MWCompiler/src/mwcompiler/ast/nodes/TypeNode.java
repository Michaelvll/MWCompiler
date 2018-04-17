package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.*;

/**
 * TypeNode.java
 * NonArrayTypeSymbol extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public class TypeNode extends Node {
    //TODO
    TypeSymbol typeSymbol;
    Location location;

    public TypeNode(String type, Integer dim, Location pos) {
        if (dim == 0) {
            this.typeSymbol = NonArrayTypeSymbol.builder(type);
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
    public void accept(AstVisitor visitor) {
        //Do Nothing
    }
}
