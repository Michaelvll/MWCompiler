/**
 * Node.java
 * The abstract Node class for AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.*;

abstract public class Node {
    //TODO

    abstract void accept(AstVisitor visitor);
}

