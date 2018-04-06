/**
 * ArrayTypeNode.java
 * ArrayType extends from TypeNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

public class ArrayTypeNode extends TypeNode {
    //TODO
    private Integer dimension = 0;

    public ArrayTypeNode(String type, Integer dimension) {
        super(type);
        this.dimension = dimension;
    }

    public Integer getDimension() {
        return dimension;
    }

    public void setDimension(Integer dimension) {
        this.dimension = dimension;
    }
}
