package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class UnaryExprNode extends ExprNode {

    private OPs op;
    private ExprNode expr;

    public UnaryExprNode(OPs op, ExprNode expr, Location pos) {
        super(pos);
        this.op = op;
        this.expr = expr;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public OPs getOp() {
        return op;
    }


    public ExprNode getExpr() {
        return expr;
    }
}
