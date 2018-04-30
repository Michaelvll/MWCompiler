package mwcompiler.ir.nodes;


import java.util.LinkedList;

public class BasicBlock {
    private LinkedList<Instruction> instructions = new LinkedList<>();

    public void insert(Instruction instruction) {
        instructions.add(instruction);
    }

}
