package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

import java.util.List;

public class DotMemberNode extends ExprNode {
    private ExprNode mom;
    private String member;

    public DotMemberNode(ExprNode mom, String member, Location pos) {
        this.mom = mom;
        this.member = member;
        super.location = pos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getMom() {
        return mom;
    }

    public void setMom(ExprNode mom) {
        this.mom = mom;
    }

    public String getMember() {
        return member;
    }
}
