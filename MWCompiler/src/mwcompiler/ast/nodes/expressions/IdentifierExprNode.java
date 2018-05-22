package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.Instance;
import mwcompiler.utility.Location;

/**
 * IdentifierExprNode.java
 * Identifier extends from ExprNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class IdentifierExprNode extends ExprNode {
    private Instance instance;

    public IdentifierExprNode(String name, Location pos) {
        super(pos);
        this.instance = Instance.builder(name);
    }

    public IdentifierExprNode(Instance instance, Location pos) {
        super(pos);
        this.instance = instance;
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Instance getInstance() {
        return instance;
    }
}
