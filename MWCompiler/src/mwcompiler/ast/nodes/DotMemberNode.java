package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

/**
 * DotMemberNode.java
 * Dot member node extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class DotMemberNode extends ExprNode {
    private ExprNode container;
    private IdentifierExprNode member;

    public DotMemberNode(ExprNode container, IdentifierExprNode member, Location pos) {
        super(pos);
        this.container = container;
        this.member = member;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }


    public ExprNode getContainer() {
        return container;
    }

    public IdentifierExprNode getMember() {
        return member;
    }
}
