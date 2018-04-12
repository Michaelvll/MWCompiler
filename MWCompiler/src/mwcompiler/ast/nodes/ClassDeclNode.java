package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.TypeSymbol;

import java.util.List;

/**
 * ClassDeclNode.java
 * Class declaration node extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class ClassDeclNode extends DeclaratorNode {
    private TypeSymbol declClass;
    private BlockNode body;
    private Location declClassPos;

    public ClassDeclNode(String declClass, BlockNode body, Location declClassPos) {
        this.declClass = TypeSymbol.builder(declClass);
        this.body = body;
        this.declClassPos = declClassPos;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }


    public BlockNode getBody() {
        return body;
    }

    public Location getDeclClassPos() {
        return declClassPos;
    }

    public TypeSymbol getDeclClass() {
        return declClass;
    }
}
