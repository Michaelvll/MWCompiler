package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.ArrayTypeSymbol;
import mwcompiler.symbols.TypeSymbol;

/**
 * ArrayTypeNode.java
 * ArrayType extends from TypeNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public class ArrayTypeNode extends TypeNode {
    //TODO
    private Integer dim = -1;

    public ArrayTypeNode(String type, Integer dim, Location pos) {
        super(type, pos);
        this.dim = dim;
    }
    public ArrayTypeNode(NonArrayTypeNode node) {
        super(node.typename, node.location);
        this.dim = 1;
    }

    public Integer getDim() {
        return dim;
    }

    public void setDim(Integer dim) {
        this.dim = dim;
    }
    public void addDimension (){
        ++this.dim;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeSymbol getSymbol() {
        return ArrayTypeSymbol.getSymbol(super.getTypename(), dim);
    }
}
