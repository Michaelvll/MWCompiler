package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

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
