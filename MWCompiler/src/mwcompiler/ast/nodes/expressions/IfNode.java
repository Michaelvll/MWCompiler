package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.nodes.BlockNode;
import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * IfNode.java
 * If node extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class IfNode extends Node {
    private ExprNode condition;
    private BlockNode body;
    private IfNode elseNode;
    private Location location;

    public IfNode(ExprNode condition, BlockNode body, Location pos) {
        this.condition = condition;
        this.body = body;
        this.location = pos;
    }


    @Override
    public Location location() {
        return location;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Location getLocation() {
        return location;
    }

    public ExprNode getCondition() {
        return condition;
    }

    public BlockNode getBody() {
        return body;
    }

    public IfNode getElseNode() {
        return elseNode;
    }

    public void setElseNode(IfNode elseNode) {
        this.elseNode = elseNode;
    }

}
