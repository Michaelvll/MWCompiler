/**
 * BinaryExprNode.java
 * BinaryExpr extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;

public class BinaryExprNode extends ExprNode {
    //TODO
    public String operator;
    public ExprNode left, right;

    public BinaryExprNode(ExprNode left, String op, ExprNode right) {
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

