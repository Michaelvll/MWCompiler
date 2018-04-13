package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class BrackMemberNode extends ExprNode {
    private ExprNode container;
    private ExprNode subscript;

    public BrackMemberNode(ExprNode container, ExprNode subscript, Location pos) {
        this.container = container;
        this.subscript = subscript;
        super.location = pos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getContainer() {
        return container;
    }

    public ExprNode getSubscript() {
        return subscript;
    }
}