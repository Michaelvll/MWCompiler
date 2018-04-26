package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;
import mwcompiler.symbols.NonArrayTypeSymbol;

/**
 * ClassDeclNode.java
 * Class declaration node extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class ClassDeclNode extends DeclaratorNode {
    private NonArrayTypeSymbol classSymbol;
    private BlockNode body;
    private Location declClassPos;

    public ClassDeclNode(String className, BlockNode body, Location declClassPos) {
        this.classSymbol = NonArrayTypeSymbol.builder(className); // Throw an already declared runtime error
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

    public NonArrayTypeSymbol getClassSymbol() {
        return classSymbol;
    }


    @Override
    public Location getStartLocation() {
        return declClassPos;
    }

}
