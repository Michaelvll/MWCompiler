package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.*;
import mwcompiler.utility.Location;

/**
 * Node.java
 * The abstract Node class for AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public abstract class Node {
    public abstract Location location();
    public abstract <T> T accept(AstVisitor<T> visitor);
}

