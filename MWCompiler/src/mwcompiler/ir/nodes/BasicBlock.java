package mwcompiler.ir.nodes;


import java.util.LinkedList;

public class BasicBlock {
    private LinkedList<InstructionNode> instructions = new LinkedList<>();

    public void insert(InstructionNode instruction) {
        instructions.add(instruction);
    }

}
