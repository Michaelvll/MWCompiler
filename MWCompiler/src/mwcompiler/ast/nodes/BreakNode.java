package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

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
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
