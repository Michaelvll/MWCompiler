package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

import java.util.List;
/**
 * ProgramNode.java
 * The root Node of AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public class ProgramNode extends  Node {
    //TODO
    private final BlockNode declarators;

    public ProgramNode(BlockNode declarators){
        this.declarators = declarators;
    }

    public BlockNode getDeclarators() {
        return declarators;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
