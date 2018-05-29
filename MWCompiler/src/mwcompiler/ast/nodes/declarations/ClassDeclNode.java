package mwcompiler.ast.nodes.declarations;

import mwcompiler.ast.nodes.BlockNode;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.BaseTypeSymbol;
import mwcompiler.utility.Location;

/**
 * ClassDeclNode.java
 * Class declaration node extends from DeclarationNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class ClassDeclNode extends DeclarationNode {
    private BaseTypeSymbol classSymbol;
    private BlockNode body;
    private Location declClassPos;

    public ClassDeclNode(String className, BlockNode body, Location declClassPos) {
        this.classSymbol = BaseTypeSymbol.builder(className); // Throw an already declared runtime error
        this.body = body;
        this.declClassPos = declClassPos;
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public BlockNode getBody() {
        return body;
    }

    public BaseTypeSymbol getClassSymbol() {
        return classSymbol;
    }


    @Override
    public Location location() {
        return declClassPos;
    }

}
