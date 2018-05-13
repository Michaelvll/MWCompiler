package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;

public abstract class Instruction {
    public Instruction prev;
    public Instruction next;

    

    public abstract <T> T accept(IRVisitor<T> visitor);


    public Instruction setPrev(Instruction prev) {
        this.prev = prev;
        return prev;
    }

    public Instruction setNext(Instruction next) {
        this.next = next;
        return next;
    }

}
