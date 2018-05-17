package mwcompiler.ir.nodes;


import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
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

    public Boolean isEnded = false;

    private Set<BasicBlock> fromBasicBlock;
    private Set<BasicBlock> toBasicBlock;

//    private Map<Register, IntLiteral> regIntMap = new HashMap<>();

    public BasicBlock(Function parentFunction) {
        this.parentFunction = parentFunction;
        this.name = NameBuilder.builder(parentFunction.getInstanceSymbol().getName());
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

    private void pushBack(Instruction instruction) {
        instruction.setPrev(end);
        if (end != null)
            end = end.setNext(instruction);
        else {
            front = instruction;
            end = front;
        }
    }

    public void pushBack(MoveInst moveInst) {
        pushBack((Instruction) moveInst);
        addKnownReg(moveInst.getDst(), moveInst.getVal());
    }

    public void pushBack(AssignInst assignInst) {
        pushBack((Instruction) assignInst);
        addKnownReg(assignInst.getDst(), null);
    }

    public void pushBack(JumpInst jumpInst) {
        pushBack((Instruction) jumpInst);
        isEnded = true;
        if (jumpInst instanceof ReturnInst)
            parentFunction.AddReturn((ReturnInst) jumpInst);
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

    private void addKnownReg(Register reg, Operand val) {
        if (val instanceof IntLiteral)
            reg.setVal((IntLiteral) val);
        else
            reg.setVal(null);
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
