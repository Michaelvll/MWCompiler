package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class DotMemberNode extends ExprNode {
    private ExprNode container;
    private String member;

    public DotMemberNode(ExprNode container, String member, Location pos) {
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

    public String getMember() {
        return member;
    }
}
