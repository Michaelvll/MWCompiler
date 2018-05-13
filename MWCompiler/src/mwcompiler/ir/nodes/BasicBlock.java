package mwcompiler.ir.nodes;


import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.TypeSymbol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasicBlock {
    private Instruction front;
    private Instruction end;
    private String name;
    private Function parentFunction;

    private Set<BasicBlock> fromBasicBlock;
    private Set<BasicBlock> toBasicBlock;

    private Map<Register, IntLiteral> regIntMap = new HashMap<>();

    public BasicBlock(Function parentFunction) {
        this.parentFunction = parentFunction;
        this.name = NameBuilder.builder(this);
    }

    public BasicBlock(Function parentFunction, String name) {
        this.parentFunction = parentFunction;
        this.name = NameBuilder.builder(name);
    }

    public void pushFront(Instruction instruction) {
        instruction.setNext(front);
        if (front != null)
            front = front.setPrev(instruction);
        else
            front = instruction;
    }

    public void pushBack(Instruction instruction) {
        instruction.setPrev(end);
        if (end != null)
            end = end.setNext(instruction);
        else {
            front = instruction;
            end = front;
        }
    }

    public void pushBack(Jump jump) {
        pushBack((Instruction) jump);
        if (jump instanceof Return)
            parentFunction.AddReturn((Return) jump);
        regIntMap.clear(); // Clear up the map when exit the Basic Block
    }

    public void insert(Instruction pos, Instruction instruction) {
        instruction.setPrev(pos);
        instruction.setNext(pos.next);
        pos.setNext(instruction);
    }

    public Instruction front() {
        return front;
    }

    public Instruction back() {
        return end;
    }

    public void addKnownReg(Register reg, IntLiteral val) {
        regIntMap.put(reg, val);
    }

    public IntLiteral getKnownReg(Register reg) {
        return regIntMap.get(reg);
    }

    public String getName() {
        return name;
    }

    public Function getParentFunction() {
        return parentFunction;
    }

    public TypeSymbol getFunctionReturnType() {
        return parentFunction.getFunctionTypeSymbol().getReturnType();
    }
}
