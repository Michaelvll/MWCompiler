/**
 * BinaryExprNode.java
 * BinaryExpr extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class BinaryExprNode extends ExprNode {
    //TODO
    public OPs operator;
    public ExprNode left, right;

    public BinaryExprNode(ExprNode left, OPs op, ExprNode right) {
        super();
        this.operator = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}

