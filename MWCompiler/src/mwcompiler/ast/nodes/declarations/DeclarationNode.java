package mwcompiler.ast.nodes.declarations;

import mwcompiler.ast.nodes.Node;
import mwcompiler.utility.Location;

/**
 * DeclarationNode.java
 * Declarator extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public abstract class DeclarationNode extends Node {
    public abstract Location location();
}
