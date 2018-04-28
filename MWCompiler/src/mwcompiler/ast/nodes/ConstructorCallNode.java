package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;
import mwcompiler.symbols.NonArrayTypeSymbol;

import java.util.List;

/**
 * ConstructorCallNode.java
 * Constructor call node extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-17
 */
public class ConstructorCallNode extends ExprNode {
    private NonArrayTypeSymbol classTypeSymbol;
    private List<ExprNode> args;

    public ConstructorCallNode(NonArrayTypeSymbol classTypeSymbol, List<ExprNode> args, Location pos) {
        super(pos);
        this.classTypeSymbol = classTypeSymbol;
        this.args = args;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public NonArrayTypeSymbol getClassTypeSymbol() {
        return classTypeSymbol;
    }

    public List<ExprNode> getArgs() {
        return args;
    }
}
