package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.NonArrayTypeSymbol;
import mwcompiler.symbols.TypeSymbol;

public class NullTypeNode extends TypeNode {
    public NullTypeNode(Location pos) {
        super("void",pos);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public TypeSymbol getSymbol() {
        return NonArrayTypeSymbol.getSymbol("");
    }
}
