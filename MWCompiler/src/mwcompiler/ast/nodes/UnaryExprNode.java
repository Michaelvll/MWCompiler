package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

/**
 * UnaryExprNode.java
 * Unary expression node extends ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-12
 */
public class UnaryExprNode extends ExprNode {

    private OPs op;
    private ExprNode expr;

    public UnaryExprNode(OPs op, ExprNode expr, Location pos, String text) {
        super(pos);
        this.op = op;
        this.expr = expr;
        super.setText(text);
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
