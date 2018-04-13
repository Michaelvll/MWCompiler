package mwcompiler.ast.nodes;


import mwcompiler.ast.tools.Location;

public abstract class JumpNode extends ExprNode {


    JumpNode(Location pos) {
        super(pos);
    }

}
