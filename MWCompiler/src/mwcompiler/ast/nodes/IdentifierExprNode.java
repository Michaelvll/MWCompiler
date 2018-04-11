package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

/**
 * IdentifierExprNode.java
 * Identifier extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class IdentifierExprNode extends ExprNode {
    //TODO

    private String name;

    public IdentifierExprNode(String name, Location pos) {
        this.name = name;
        super.location = pos;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
