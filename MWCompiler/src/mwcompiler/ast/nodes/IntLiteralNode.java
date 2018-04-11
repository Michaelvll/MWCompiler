package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class IntLiteralNode extends LiteralExprNode {
    public Integer val;
    public IntLiteralNode(String val) {
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
