/**
 * IdentifierNode.java
 * Identifier extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.Location;

public class IdentifierNode extends ExprNode {
    //TODO

    public String name;
    Location pos;
    public IdentifierNode(String name, Location pos) {
        this.name = name;
        this.pos = pos;
    }
}
