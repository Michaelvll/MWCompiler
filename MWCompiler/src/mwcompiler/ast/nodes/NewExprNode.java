package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.TypeSymbol;

import java.util.List;

public class NewExprNode extends ExprNode {
    public TypeSymbol createType;
    public Integer dim;
    public List<ExprNode> dimArgs;

    public NewExprNode(String createType, Integer dim, List<ExprNode> dimArgs, Location pos) {
        this.createType = TypeSymbol.builder(createType);
        this.dim = dim;
        this.dimArgs = dimArgs;
        super.location = pos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
