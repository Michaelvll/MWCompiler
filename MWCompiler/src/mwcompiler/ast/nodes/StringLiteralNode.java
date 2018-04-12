package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class StringLiteralNode extends LiteralExprNode {
    private String val;

    public StringLiteralNode(String val, Location pos) {
        this.val = val;
        super.location = pos;
    }

    public String getVal() {
        return val;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
