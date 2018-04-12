package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

/**
 * VoidTypeNode.java
 * VoidNode extends from TypeNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 * */
public class VoidTypeNode extends TypeNode {
    public VoidTypeNode() {
        super("void");
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
