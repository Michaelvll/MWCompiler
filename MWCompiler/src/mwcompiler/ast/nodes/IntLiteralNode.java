package mwcompiler.ast.nodes;

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
    private Integer val;

    public IntLiteralNode(String val, Location pos) {
        super(pos);
        this.val = Integer.decode(val);
    }

    public Integer getVal() {
        return val;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
