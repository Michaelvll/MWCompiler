package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class BrackMemberNode extends ExprNode {
    private ExprNode mom;
    private ExprNode subscript;

    public BrackMemberNode(ExprNode mom, ExprNode subscript, Location pos) {
        this.mom = mom;
        this.subscript = subscript;
        super.location = pos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getMom() {
        return mom;
    }

    public ExprNode getSubscript() {
        return subscript;
    }
}
