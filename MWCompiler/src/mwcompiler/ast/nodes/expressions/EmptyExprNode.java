package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * EmptyExprNode.java
 * Node for empty expression
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class EmptyExprNode extends ExprNode {

    public EmptyExprNode(Location pos) {
        super(pos);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
