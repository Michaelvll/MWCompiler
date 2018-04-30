package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Instruction {
    private Register target;
    private Register left;
    private Register right;

    public abstract <T> T accept(IRVisitor<T> visitor);

    public Register getTarget() {
        return target;
    }

    public void setTarget(Register target) {
        this.target = target;
    }

    public Register getLeft() {
        return left;
    }

    public void setLeft(Register left) {
        this.left = left;
    }

    public Register getRight() {
        return right;
    }

    public void setRight(Register right) {
        this.right = right;
    }
}
