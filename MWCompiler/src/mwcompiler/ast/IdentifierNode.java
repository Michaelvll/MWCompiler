package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;

public class IdentifierNode extends Node {
    public String name;

    public IdentifierNode(String s) {
        this.name = s;
    }

    public String getName() {return this.name;}

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
