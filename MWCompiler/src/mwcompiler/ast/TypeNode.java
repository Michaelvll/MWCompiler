/**
 * TypeNode.java
 * Type extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.*;

public abstract class TypeNode extends Node {
    //TODO
    public Type type;

    public TypeNode(Type type) {
        this.type = Type.builder(type);
    }

    public TypeNode(TypeNode node) {
        this.type = Type.builder(node.type);
    }

    public TypeNode(String type) {
        this.type = Type.builder(type);
    }
}
