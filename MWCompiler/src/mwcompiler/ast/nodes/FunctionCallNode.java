package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

import java.util.List;

public class FunctionCallNode extends ExprNode {
    private ExprNode caller;
    private List<ExprNode> args;

    public FunctionCallNode(ExprNode caller, List<ExprNode> args, Location pos) {
        this.caller = caller;
        this.args = args;
        super.location = pos;
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
