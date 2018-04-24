package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

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
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
