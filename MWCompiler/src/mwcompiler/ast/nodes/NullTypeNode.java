package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class NullTypeNode extends TypeNode {
    public NullTypeNode() {
        super("void");
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
