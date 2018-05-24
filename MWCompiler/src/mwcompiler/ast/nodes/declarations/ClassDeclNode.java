package mwcompiler.ast.nodes.declarations;

import mwcompiler.ast.nodes.BlockNode;
import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.NonArrayTypeSymbol;
import mwcompiler.symbols.SymbolInfo;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.Location;

/**
 * ClassDeclNode.java
 * Class declaration node extends from DeclarationNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 */
public class ClassDeclNode extends DeclarationNode {
    private NonArrayTypeSymbol classSymbol;
    private BlockNode body;
    private Location declClassPos;

    public ClassDeclNode(String className, BlockNode body, Location declClassPos) {
        this.classSymbol = NonArrayTypeSymbol.builder(className); // Throw an already declared runtime error
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

    public NonArrayTypeSymbol getClassSymbol() {
        return classSymbol;
    }


    @Override
    public Location getStartLocation() {
        return declClassPos;
    }

}
