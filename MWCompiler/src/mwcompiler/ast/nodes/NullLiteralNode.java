package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

/**
 * NullLiteralNode.java
 * Node for null literal expression
 *
 * @see mwcompiler.ast.nodes.LiteralExprNode
 * @author Michael Wu
 * @since 2018-04-11
 * */
public class NullLiteralNode extends LiteralExprNode {
    public NullLiteralNode(Location pos) {
        super(pos);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
