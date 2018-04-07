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

public class TypeNode extends Node {
    //TODO
    public Type type;

    public TypeNode(String type){
        switch (type) {
            case "int":
                this.type = new IntType();
                break;
            case "string":
                this.type = new StringType();
                break;
            case "bool":
                this.type = new BoolType();
                break;
            default:
                this.type = new IdentifierType(type);
        }

    }
    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
