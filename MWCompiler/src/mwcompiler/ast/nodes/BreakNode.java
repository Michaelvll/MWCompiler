package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class BreakNode extends JumpNode {

    public BreakNode(Location pos) {
        super(pos);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
