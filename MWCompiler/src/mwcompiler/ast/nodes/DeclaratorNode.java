package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.Location;

/**
 * DeclaratorNode.java
 * Declarator extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public abstract class DeclaratorNode extends Node {
    public abstract Location getStartLocation();
}
