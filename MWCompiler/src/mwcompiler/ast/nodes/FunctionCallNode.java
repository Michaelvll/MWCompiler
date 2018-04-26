package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

import java.util.List;

/**
 * FunctionCallNode.java
 * Function call node extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class FunctionCallNode extends ExprNode {
    private ExprNode caller;
    private List<ExprNode> args;

    public FunctionCallNode(ExprNode caller, List<ExprNode> args, Location pos) {
        super(pos);
        this.caller = caller;
        this.args = args;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getCaller() {
        return caller;
    }

    public List<ExprNode> getArgs() {
        return args;
    }
}
