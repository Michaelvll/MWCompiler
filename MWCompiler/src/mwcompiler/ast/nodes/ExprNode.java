/**
 * ExprNode.java
 * Expression extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.Location;

public abstract class ExprNode extends Node {
    //TODO

    protected Location location;
    public enum OPs{
        // Binary
        Assign,
        // Unary
        INC,DEC,
        ADD, SUB,
        NOT, BITNOT,
        // New
        NEW
    }
    public Location getLocation(){
        return location;
    }
}
