package mwcompiler.ir.nodes;


import mwcompiler.ir.tools.IRVisitor;

public class BasicBlock {
    private Instruction head;
    private Instruction end;

    public void push_front(Instruction instruction) {
        head = head.add_front(instruction);
    }

    public void push_back(Instruction instruction) {
        end = end.add_back(instruction);
    }

    public Instruction getLast() {
        return end;
    }

}
