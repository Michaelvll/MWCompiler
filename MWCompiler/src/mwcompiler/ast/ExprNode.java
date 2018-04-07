/**
 * ExprNode.java
 * Expression extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public abstract class ExprNode extends Node {
    //TODO

    protected Location location;
    public enum OPs{
        ASSIGN
    }
}
