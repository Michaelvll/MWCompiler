package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.InstanceSymbol;

/**
 * IdentifierExprNode.java
 * Identifier extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class IdentifierExprNode extends ExprNode {
    private InstanceSymbol instanceSymbol;

    public IdentifierExprNode(String name, Location pos, String text) {
        super(pos);
        this.instanceSymbol = InstanceSymbol.builder(name);
        super.setText(text);
    }

    public IdentifierExprNode(InstanceSymbol instanceSymbol, Location pos) {
        super(pos);
        this.instanceSymbol = instanceSymbol;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public InstanceSymbol getInstanceSymbol() {
        return instanceSymbol;
    }
}
