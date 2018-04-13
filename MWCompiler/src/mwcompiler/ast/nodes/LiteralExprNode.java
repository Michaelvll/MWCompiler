package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.Location;

/**
 * LiteralExprNode.java
 * abstract node for literal expression extends ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public abstract class LiteralExprNode extends ExprNode {

    LiteralExprNode(Location pos) {
        super(pos);
    }
}
