package mwcompiler.ir.nodes;


import mwcompiler.ir.tools.NameBuilder;

public class BasicBlock {
    private Instruction head;
    private Instruction end;
    private String name;

    public BasicBlock() {
        this.name = NameBuilder.builder(this);
    }

    public BasicBlock(String name) {
        this.name = NameBuilder.builder(name);
    }

    public void push_front(Instruction instruction) {
        head = head.add_front(instruction);
    }

    public void push_back(Instruction instruction) {
        end = end.add_back(instruction);
    }

    public Instruction getHead() {
        return head;
    }

    public Instruction getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }
}
