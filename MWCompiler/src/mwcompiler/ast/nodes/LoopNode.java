package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class LoopNode extends Node {
    private ExprNode varDecl;
    private ExprNode condition;
    private ExprNode step;
    private BlockNode body;
    private Location varDeclPos;
    private Location conditionPos;
    private Location stepPos;

    public LoopNode(ExprNode varDecl, ExprNode condition, ExprNode step, BlockNode body,
                    Location varDeclPos, Location conditionPos, Location stepPos) {
        this.varDecl = varDecl;
        this.condition = condition;
        this.step = step;
        this.body = body;
        this.varDeclPos = varDeclPos;
        this.conditionPos = conditionPos;
        this.stepPos = stepPos;
    }



    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getVarDecl() {
        return varDecl;
    }

    public ExprNode getCondition() {
        return condition;
    }

    public ExprNode getStep() {
        return step;
    }

    public BlockNode getBody() {
        return body;
    }

    public Location getVarDeclPos() {
        return varDeclPos;
    }

    public Location getConditionPos() {
        return conditionPos;
    }

    public Location getStepPos() {
        return stepPos;
    }
}
