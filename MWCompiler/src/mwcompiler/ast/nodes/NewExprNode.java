package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

import java.util.List;

public class NewExprNode extends ExprNode {
    private TypeNode createType;
    private List<ExprNode> dimArgs;

    public NewExprNode(String createType, Integer dim, List<ExprNode> dimArgs, Location pos) {
        super(pos);
        this.createType = TypeNode.builder(createType,dim,pos);
        this.dimArgs = dimArgs;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public TypeNode getCreateType() {
        return createType;
    }

    public List<ExprNode> getDimArgs() {
        return dimArgs;
    }
}
