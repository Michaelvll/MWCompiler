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
    //TODO

    public void transform2Symbol(){
    }
    public abstract Location getStartLocation();
    public abstract void accept(AstVisitor visitor);
}

