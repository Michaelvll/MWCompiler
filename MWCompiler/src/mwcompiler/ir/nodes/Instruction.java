package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Instruction {
    public Instruction pre;
    public Instruction next;

    private Register target;
    private RegOrImm left;
    private RegOrImm right;

    public abstract <T> T accept(IRVisitor<T> visitor);

    public Instruction add_front(Instruction pre) {
        this.pre = pre;
        return pre;
    }

    public Instruction add_back(Instruction next) {
        this.next = next;
        return next;
    }

    public Register getTarget() {
        return target;
    }

    public void setTarget(Register target) {
        this.target = target;
    }

    public RegOrImm getLeft() {
        return left;
    }

    public void setLeft(RegOrImm left) {
        this.left = left;
    }

    public RegOrImm getRight() {
        return right;
    }

    public void setRight(RegOrImm right) {
        this.right = right;
    }
}
