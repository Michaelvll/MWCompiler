package mwcompiler.ast.nodes.literals;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * StringLiteralNode.java
 * String literal node extends LiteralExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class StringLiteralNode extends LiteralExprNode {
    private String val;

    public StringLiteralNode(String val, Location pos) {
        super(pos);
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
