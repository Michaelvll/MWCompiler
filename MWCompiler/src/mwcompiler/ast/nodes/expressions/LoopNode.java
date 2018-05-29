package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.nodes.BlockNode;
import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.nodes.expressions.BinaryExprNode;
import mwcompiler.ast.nodes.expressions.ExprNode;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * LoopNode.java
 * Loop node extends Node
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class LoopNode extends Node {
    private BinaryExprNode varInit;
    private ExprNode condition;
    private ExprNode step;
    private BlockNode body;
    private Location forPos;
    private Location varInitPos;
    private Location conditionPos;
    private Location stepPos;

    public LoopNode(BinaryExprNode varInit, ExprNode condition, ExprNode step, BlockNode body, Location forPos,
                    Location varInitPos, Location conditionPos, Location stepPos) {
        this.varInit = varInit;
        this.condition = condition;
        this.step = step;
        this.body = body;
        this.forPos = forPos;
        this.varInitPos = varInitPos;
        this.conditionPos = conditionPos;
        this.stepPos = stepPos;
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Node getVarInit() {
        return varInit;
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

    public Location getVarInitPos() {
        return varInitPos;
    }

    public Location getConditionPos() {
        return conditionPos;
    }

    public Location getStepPos() {
        return stepPos;
    }

    @Override
    public Location location() {
        return forPos;
    }
}
