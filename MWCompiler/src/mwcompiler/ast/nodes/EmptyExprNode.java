package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

/**
 * EmptyExprNode.java
 * Node for empty expression
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class EmptyExprNode extends ExprNode {

    public EmptyExprNode(Location pos) {
        super.location = pos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}