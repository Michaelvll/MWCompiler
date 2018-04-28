package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * BinaryExprNode.java
 * BinaryExpr extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public class BinaryExprNode extends ExprNode {
    private OPs op;
    private ExprNode left, right;

    public BinaryExprNode(ExprNode left, OPs op, ExprNode right, Location pos) {
        super(pos);
        this.op = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public OPs getOp() {
        return op;
    }

    public ExprNode getLeft() {
        return left;
    }

    public ExprNode getRight() {
        return right;
    }

}

