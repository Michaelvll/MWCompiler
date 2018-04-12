package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;

public class ReturnNode extends JumpNode {
    private ExprNode returnVal;
    public ReturnNode(ExprNode returnVal) {
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
