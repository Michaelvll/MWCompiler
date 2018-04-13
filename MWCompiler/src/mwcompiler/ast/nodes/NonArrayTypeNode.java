package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.NonArrayTypeSymbol;
import mwcompiler.symbols.TypeSymbol;

public class NonArrayTypeNode extends TypeNode {

    public NonArrayTypeNode(String type, Location pos) {
        super(type, pos);
    }

    @Override
    public TypeSymbol getSymbol() {
        return NonArrayTypeSymbol.getSymbol(super.getTypename());
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
