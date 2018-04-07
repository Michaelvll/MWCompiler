package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;

public class StringLiteralNode extends LiteralExprNode {
    public String val;

    public StringLiteralNode(String val){
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
