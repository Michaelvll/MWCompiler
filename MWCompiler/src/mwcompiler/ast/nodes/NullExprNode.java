package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

/**
 * NullExprNode.java
 * Node for empty expression
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class NullExprNode extends ExprNode {

    public NullExprNode(Location pos) {
        super.location = pos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
