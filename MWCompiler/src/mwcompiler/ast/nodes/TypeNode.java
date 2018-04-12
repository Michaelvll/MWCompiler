/**
 * TypeNode.java
 * TypeSymbol extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.symbols.*;

public abstract class TypeNode extends Node {
    //TODO
    protected String type;

    public TypeNode(TypeSymbol type) {
        this.type = type.getName();
    }

    public TypeNode(TypeNode node) {
        this.type = node.type;
    }

    public TypeNode(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
