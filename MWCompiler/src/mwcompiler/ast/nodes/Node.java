package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.*;

/**
 * Node.java
 * The abstract Node class for AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public abstract class Node {
    private String text;
    public abstract Location getStartLocation();
    public abstract void accept(AstVisitor visitor);

    public void setText(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

