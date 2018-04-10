package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class NonArrayTypeNode extends TypeNode {

    public NonArrayTypeNode(String type) {
        super(type);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
