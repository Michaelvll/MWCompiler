package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.BaseTypeSymbol;
import mwcompiler.utility.Location;

import java.util.List;

/**
 * ConstructorCallNode.java
 * Constructor call node extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-17
 */
public class ConstructorCallNode extends ExprNode {
    private BaseTypeSymbol classTypeSymbol;
    private List<ExprNode> args;

    public ConstructorCallNode(BaseTypeSymbol classTypeSymbol, List<ExprNode> args, Location pos) {
        super(pos);
        this.classTypeSymbol = classTypeSymbol;
        this.args = args;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public BaseTypeSymbol getClassTypeSymbol() {
        return classTypeSymbol;
    }

    public List<ExprNode> getArgs() {
        return args;
    }
}
