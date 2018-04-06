/**
 * ProgramNode.java
 * The root Node of AST
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;
import org.apache.maven.model.Build;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends  Node {
    //TODO
    private final List<DeclaratorNode> declarators;

    public ProgramNode(List<DeclaratorNode> declarators){
        this.declarators = declarators;
    }

    public List<DeclaratorNode> getDeclarators() {
        return declarators;
    }

    @Override
    void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
