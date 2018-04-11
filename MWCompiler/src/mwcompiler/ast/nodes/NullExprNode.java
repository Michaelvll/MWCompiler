package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class NullExprNode extends ExprNode {

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
