package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;


public class IfNode extends Node {
    private ExprNode condition;
    private BlockNode body;
    private IfNode elseCondition;
    private Location location;

    public IfNode(ExprNode condition, BlockNode body, Location pos) {
        this.condition = condition;
        this.body = body;
        this.location = pos;
    }


    @Override
    public Location getStartLocation() {
        return location;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
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

    public IfNode getElseCondition() {
        return elseCondition;
    }

    public void setElseCondition(IfNode elseCondition) {
        this.elseCondition = elseCondition;
    }
}
