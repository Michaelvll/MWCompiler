package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class BreakNode extends JumpNode {

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
