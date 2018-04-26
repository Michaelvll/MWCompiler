package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;

public class ReturnNode extends JumpNode {
    private ExprNode returnVal;
    public ReturnNode(ExprNode returnVal, Location pos) {
        super(pos);
        this.returnVal = returnVal;
    }
    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getReturnVal() {
        return returnVal;
    }
}
