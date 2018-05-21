package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.Register;
import mwcompiler.ir.tools.IRVisitor;

public abstract class Instruction {
    public Instruction prev;
    public Instruction next;


    public abstract <T> T accept(IRVisitor<T> visitor);


    Instruction addPrev(Instruction prevInst) {
        if (this.prev != null) this.prev.next = prevInst;
        prevInst.next = this;
        prevInst.prev = this.prev;
        this.prev = prevInst;
        return prevInst;
    }

    Instruction addNext(Instruction nextInst) {
        if (this.next != null) this.next.prev = nextInst;
        nextInst.prev = this;
        nextInst.next = this.next;
        this.next = nextInst;
        return nextInst;
    }

    Instruction delete() {
        if (prev != null) prev.next = next;
        if (next != null) next.prev = prev;
        return this;
    }

}
