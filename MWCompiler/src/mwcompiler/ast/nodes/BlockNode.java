package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

import java.util.List;

/**
 * BlockNode.java
 * Block node extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-11
 * */

public class BlockNode extends Node {
    public List<Node> statements;

    public BlockNode(List<Node> statements) {
        this.statements = statements;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this );
    }
}
