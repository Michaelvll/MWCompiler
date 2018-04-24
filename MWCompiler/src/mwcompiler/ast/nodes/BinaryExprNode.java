package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

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

    public BinaryExprNode(ExprNode left, OPs op, ExprNode right, Location pos, String text) {
        super(pos);
        super.setText(text);
        this.op = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
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

