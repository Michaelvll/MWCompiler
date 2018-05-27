package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.List;

public abstract class Instruction {
    public Instruction prev;
    public Instruction next;


    public abstract <T> T accept(IRVisitor<T> visitor);
    public abstract List<Var> usedVar();
    public abstract List<Var> dstVar();


    void addPrev(Instruction prevInst) {
        if (this.prev != null) this.prev.next = prevInst;
        prevInst.next = this;
        prevInst.prev = this.prev;
        this.prev = prevInst;
    }

    void addNext(Instruction nextInst) {
        if (this.next != null) this.next.prev = nextInst;
        nextInst.prev = this;
        nextInst.next = this.next;
        this.next = nextInst;
    }

    void delete() {
        if (prev != null) prev.next = next;
        if (next != null) next.prev = prev;
    }

}
