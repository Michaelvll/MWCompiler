/**
 * TypeNode.java
 * Type extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;

public class TypeNode extends Node {
    //TODO
    protected String type;

    public TypeNode(String type){
        this.type = type;
    }
    @Override
    void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
