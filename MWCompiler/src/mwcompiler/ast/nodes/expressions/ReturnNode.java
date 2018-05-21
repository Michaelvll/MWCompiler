package mwcompiler.ast.nodes.expressions;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

public class ReturnNode extends JumpNode {
    private ExprNode returnVal;
    public ReturnNode(ExprNode returnVal, Location pos) {
        super(pos);
        this.returnVal = returnVal;
    }
    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public ExprNode getReturnVal() {
        return returnVal;
    }
}
