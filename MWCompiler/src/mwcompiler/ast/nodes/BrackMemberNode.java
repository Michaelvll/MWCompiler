package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * BrackMemberNode.java
 * Bracket member node extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class BrackMemberNode extends ExprNode {
    private ExprNode container;
    private ExprNode subscript;

    public BrackMemberNode(ExprNode container, ExprNode subscript, Location pos) {
        super(pos);
        this.container = container;
        this.subscript = subscript;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExprNode getContainer() {
        return container;
    }

    public ExprNode getSubscript() {
        return subscript;
    }
}
