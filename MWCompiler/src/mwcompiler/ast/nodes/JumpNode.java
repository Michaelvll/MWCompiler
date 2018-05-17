package mwcompiler.ast.nodes;


import mwcompiler.utility.Location;

/**
 * JumpNode.java
 * JumpInst node extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
abstract class JumpNode extends ExprNode {
    JumpNode(Location pos) {
        super(pos);
    }
}
