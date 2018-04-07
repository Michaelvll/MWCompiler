package mwcompiler.ast;

import com.sun.org.apache.xpath.internal.operations.Bool;
import mwcompiler.ast.tools.AstVisitor;

public class BoolLiteralNode extends LiteralExprNode {
    public enum BoolVal{
        TRUE, FALSE
    };
    public BoolVal val;

    public BoolLiteralNode(String val) {
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
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
