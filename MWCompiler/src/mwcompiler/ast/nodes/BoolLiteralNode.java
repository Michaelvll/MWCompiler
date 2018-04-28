package mwcompiler.ast.nodes;

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
    public enum BoolVal{
        TRUE, FALSE
    };
    private BoolVal val;

    public BoolLiteralNode(String val, Location pos) {
        super(pos);
        switch (val) {
            case "true":
                this.val = BoolVal.TRUE;
                break;
            case "false":
                this.val = BoolVal.FALSE;
                break;
            default:
                throw new RuntimeException("Literal value not match BoolLiteral");
        }
    }

    public BoolVal getVal() {
        return val;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
