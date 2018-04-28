package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

/**
 * BreakNode.java
 * Break node extends from JumpNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class BreakNode extends JumpNode {

    public BreakNode(Location pos) {
        super(pos);
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
