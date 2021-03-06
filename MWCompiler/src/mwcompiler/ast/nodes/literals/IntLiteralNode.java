package mwcompiler.ast.nodes.literals;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * IntLiteralNode.java
 * Int literal node extends from LiteralNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class IntLiteralNode extends LiteralExprNode {
    private int val;

    public IntLiteralNode(String val, Location pos) {
        super(pos);
        this.val = Integer.decode(val);
    }

    public int getVal() {
        return val;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
