package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.ExprOps;
import mwcompiler.utility.Location;

/**
 * UnaryExprNode.java
 * Unary expression node extends ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-12
 */
public class UnaryExprNode extends ExprNode {

    private ExprOps op;
    private ExprNode expr;

    public UnaryExprNode(ExprOps op, ExprNode expr, Location pos) {
        super(pos);
        this.op = op;
        this.expr = expr;
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExprOps getOp() {
        return op;
    }


    public ExprNode getExpr() {
        return expr;
    }
}
