package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;

public class NullLiteralNode extends LiteralExprNode {
    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
