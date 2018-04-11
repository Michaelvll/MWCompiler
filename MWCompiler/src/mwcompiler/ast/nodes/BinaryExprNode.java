/**
 * BinaryExprNode.java
 * BinaryExpr extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class BinaryExprNode extends ExprNode {
    //TODO
    private OPs op;
    private ExprNode left, right;

    public BinaryExprNode(ExprNode left, OPs op, ExprNode right) {
        this.op = op;
        this.left = left;
        this.right = right;
        super.location = left.getLocation();
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

