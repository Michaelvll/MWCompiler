package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.Type;

import java.util.List;

public class NewExprNode extends ExprNode {
    public Type createType;
    public Integer dim;
    public List<ExprNode> dimArgs;

    public NewExprNode(String createType, Integer dim, List<ExprNode> dimArgs) {
        this.createType = Type.builder(createType);
        this.dim = dim;
        this.dimArgs = dimArgs;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
