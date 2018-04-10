package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class UnaryExprNode extends ExprNode {
    public OPs op;


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
