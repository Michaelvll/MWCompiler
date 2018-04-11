package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class UnaryExprNode extends ExprNode {

    private OPs op;
    private ExprNode expr;
    private Location opPos;

    public UnaryExprNode(OPs op, ExprNode expr, Location opPos) {
        this.op = op;
        this.expr = expr;
        this.opPos = opPos;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public OPs getOp() {
        return op;
    }

    public Location getOpPos() {
        return opPos;
    }

    public ExprNode getExpr() {
        return expr;
    }
}
