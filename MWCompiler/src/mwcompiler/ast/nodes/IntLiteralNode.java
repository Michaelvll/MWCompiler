package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class IntLiteralNode extends LiteralExprNode {
    private Integer val;

    public IntLiteralNode(String val, Location pos) {
        this.val = Integer.decode(val);
        super.location = pos;
    }

    public Integer getVal() {
        return val;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
