package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;
import mwcompiler.symbols.ArrayTypeSymbol;
import mwcompiler.symbols.BaseTypeSymbol;
import mwcompiler.symbols.TypeSymbol;

import java.util.List;

/**
 * NewExprNode.java
 * New expression node extends ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class NewExprNode extends ExprNode {
    private TypeSymbol createType;
    private List<ExprNode> dimArgs;
    private int emptyDim = 0;


    public NewExprNode(String createType, int dim, List<ExprNode> dimArgs, Location pos) {
        super(pos);
        if (dim == 0) {
            this.createType = BaseTypeSymbol.builder(createType);
        } else {
            this.createType = ArrayTypeSymbol.builder(createType, dim);
            this.emptyDim = dim - dimArgs.size();
        }
        this.dimArgs = dimArgs;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public TypeSymbol getCreateType() {
        return createType;
    }

    public List<ExprNode> getDimArgs() {
        return dimArgs;
    }

    public int getEmptyDim() {
        return emptyDim;
    }
}
