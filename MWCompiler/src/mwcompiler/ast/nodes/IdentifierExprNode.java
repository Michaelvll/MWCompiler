/**
 * IdentifierExprNode.java
 * Identifier extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class IdentifierExprNode extends ExprNode {
    //TODO

    public String name;
    Location pos;
    public IdentifierExprNode(String name, Location pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
