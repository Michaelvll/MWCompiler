package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Instruction {
    public Instruction pre;
    public Instruction next;

    private RegisterSSA target;
    private SSA left;
    private SSA right;

    public abstract <T> T accept(IRVisitor<T> visitor);

    public Instruction add_front(Instruction pre) {
        this.pre = pre;
        return pre;
    }

    public Instruction add_back(Instruction next) {
        this.next = next;
        return next;
    }

    public RegisterSSA getTarget() {
        return target;
    }

    public void setTarget(RegisterSSA target) {
        this.target = target;
    }

    public SSA getLeft() {
        return left;
    }

    public void setLeft(SSA left) {
        this.left = left;
    }

    public SSA getRight() {
        return right;
    }

    public void setRight(SSA right) {
        this.right = right;
    }
}
