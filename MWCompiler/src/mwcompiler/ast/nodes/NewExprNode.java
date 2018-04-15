package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.ArrayTypeSymbol;
import mwcompiler.symbols.NonArrayTypeSymbol;
import mwcompiler.symbols.TypeSymbol;

import java.util.List;

public class NewExprNode extends ExprNode {
    private TypeSymbol createType;
    private List<ExprNode> dimArgs;

    public NewExprNode(String createType, Integer dim, List<ExprNode> dimArgs, Location pos) {
        super(pos);
        if (dim == 0) {
            this.createType = NonArrayTypeSymbol.builder(createType);
        } else {
            this.createType = ArrayTypeSymbol.builder(createType,dim);
        }
        this.dimArgs = dimArgs;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public TypeSymbol getCreateType() {
        return createType;
    }

    public List<ExprNode> getDimArgs() {
        return dimArgs;
    }
}
