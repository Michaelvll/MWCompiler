package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.VariableSymbol;

/**
 * IdentifierExprNode.java
 * Identifier extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class IdentifierExprNode extends ExprNode {
    //TODO

    private String instanceName;
    private InstanceSymbol instanceSymbol;

    public IdentifierExprNode(String name, Location pos) {
        super(pos);
        this.instanceName = name;
    }

    @Override
    public void transform2Symbol() {
        this.instanceSymbol = InstanceSymbol.getSymbol(this.instanceName);
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public InstanceSymbol getInstanceSymbol() {
        return instanceSymbol;
    }
}
