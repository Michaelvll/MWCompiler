/**
 * Node.java
 * The abstract Node class for AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.*;

public abstract class Node {
    //TODO

    public abstract void accept(AstVisitor visitor);
}

