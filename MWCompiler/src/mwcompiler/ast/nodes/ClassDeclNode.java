package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.NonArrayTypeSymbol;
import mwcompiler.symbols.SymbolTable;

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

    public ClassDeclNode(String classSymbol, BlockNode body, Location declClassPos) {
        this.classSymbol = NonArrayTypeSymbol.builder(classSymbol); // Throw an already declared runtime error
        this.body = body;
        this.declClassPos = declClassPos;
        SymbolTable.putNamedSymbolTable(this.classSymbol, body.getCurrentSymbolTable());
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
    public void transform2Symbol() {
        // nothing need to be down
    }

    @Override
    public Location getStartLocation() {
        return declClassPos;
    }
}
