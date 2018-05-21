package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.FunctionSymbol;
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
    private FunctionSymbol functionSymbol;

    public FunctionCallNode(ExprNode caller, List<ExprNode> args, Location pos) {
        super(pos);
        this.caller = caller;
        this.args = args;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExprNode getCaller() {
        return caller;
    }

    public List<ExprNode> getArgs() {
        return args;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }
}
