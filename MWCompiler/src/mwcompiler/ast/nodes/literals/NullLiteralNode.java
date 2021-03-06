package mwcompiler.ast.nodes.literals;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * NullLiteralNode.java
 * Node for null literal expression
 *
 * @see LiteralExprNode
 * @author Michael Wu
 * @since 2018-04-11
 * */
public class NullLiteralNode extends LiteralExprNode {
    public NullLiteralNode(Location pos) {
        super(pos);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
