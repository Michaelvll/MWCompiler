package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.NonArrayTypeSymbol;

import java.util.List;

public class ConstructorCallNode extends ExprNode {
    private NonArrayTypeSymbol classTypeSymbol;
    private List<ExprNode> args;

    public ConstructorCallNode(NonArrayTypeSymbol classTypeSymbol, List<ExprNode> args, Location pos) {
        super(pos);
        this.classTypeSymbol = classTypeSymbol;
        this.args = args;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public NonArrayTypeSymbol getClassTypeSymbol() {
        return classTypeSymbol;
    }

    public List<ExprNode> getArgs() {
        return args;
    }
}
