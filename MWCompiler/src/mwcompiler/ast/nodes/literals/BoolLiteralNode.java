package mwcompiler.ast.nodes.literals;

import mwcompiler.ast.nodes.literals.LiteralExprNode;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;
/**
 * BoolLiteralNode.java
 * Bool literal node extends from LiteralExprNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class BoolLiteralNode extends LiteralExprNode {

    private Boolean val;

    public BoolLiteralNode(String val, Location pos) {
        super(pos);
        switch (val) {
            case "true":
                this.val = true;
                break;
            case "false":
                this.val = false;
                break;
            default:
                throw new RuntimeException("Literal value not match BoolLiteral");
        }
    }

    public Boolean getVal() {
        return val;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
