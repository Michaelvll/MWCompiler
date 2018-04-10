/**
 * ArrayTypeNode.java
 * ArrayType extends from TypeNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class ArrayTypeNode extends TypeNode {
    //TODO
    private Integer dim = 0;

    public ArrayTypeNode(String type, Integer dim) {
        super(type);
        this.dim = dim;
    }
    public ArrayTypeNode(NonArrayTypeNode node) {
        super(node.type);
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
}
