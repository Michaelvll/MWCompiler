/**
 * ArrayTypeNode.java
 * ArrayType extends from TypeNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.Type;

public class ArrayTypeNode extends TypeNode {
    //TODO
    private Integer dimension = 0;

    public ArrayTypeNode(String type, Integer dimension) {
        super(type);
        this.dimension = dimension;
    }
    public ArrayTypeNode(NonArrayTypeNode node) {
        super(node.type);
        this.dimension = 1;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }
    public void addDimension (){
        ++this.dimension;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
