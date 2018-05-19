package mwcompiler.ir.nodes;


import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.symbols.TypeSymbol;
import mwcompiler.utility.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasicBlock {
    private Instruction front;
    private Instruction end;
    private String name;
    private Function parentFunction;
    SymbolTable currentSymbolTable;

    public Boolean isEnded = false;

    private Set<BasicBlock> fromBasicBlock;
    private Set<BasicBlock> toBasicBlock;

//    private Map<Register, IntLiteral> regIntMap = new HashMap<>();

    public BasicBlock(Function parentFunction, SymbolTable currentSymbolTable) {
        this.parentFunction = parentFunction;
        this.currentSymbolTable = currentSymbolTable;
        this.name = NameBuilder.builder(parentFunction.getInstanceSymbol().getName());

    }

    public BasicBlock(Function parentFunction, SymbolTable currentSymbolTable, String name) {
        this.parentFunction = parentFunction;
        this.currentSymbolTable = currentSymbolTable;
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

    public void pushBack(MoveInst moveInst, Integer valTag) {
        pushBack((Instruction) moveInst);
        addKnownReg((VirtualRegister) moveInst.getDst(), moveInst.getVal(), valTag);
    }

    public void pushBack(AssignInst assignInst, Integer valTag) {
        pushBack((Instruction) assignInst);
        addKnownReg((VirtualRegister) assignInst.getDst(), null, valTag);
    }

    public void pushBack(JumpInst jumpInst) {
        pushBack((Instruction) jumpInst);
        isEnded = true;
        if (jumpInst instanceof ReturnInst)
            parentFunction.AddReturn((ReturnInst) jumpInst);

        eliminateAssignInst(); // eliminate the unused assignInst
    }

    private void eliminateAssignInst() {
        // TODO: Uncheck for the Call instruction
        Map<Register, Pair<AssignInst, Boolean>> regAssignTable = new HashMap<>();
        for (Instruction inst = front; inst != null; inst = inst.next) {
            if (inst instanceof AssignInst) {
                AssignInst assignInst = (AssignInst) inst;
                Register dst = assignInst.getDst();

                for (Operand operand : assignInst.getOperand()) {
                    if (operand instanceof Register) {
                        Pair<AssignInst, Boolean> operandInst = regAssignTable.get(operand);
                        if (operandInst != null) operandInst.second = true;
                    }
                }

                Pair<AssignInst, Boolean> lastAssign = regAssignTable.get(dst);
                if (lastAssign != null && !lastAssign.second) {
                    erase(lastAssign.first);
                }
                regAssignTable.put(dst, new Pair<>(assignInst, false));
            }
        }
        //TODO: Code Below has a problem for
//        for (Map.Entry<Register, Pair<AssignInst, Boolean>> item : regAssignTable.entrySet()) {
//            if (!item.getValue().second) erase(item.getValue().first);
//        }
    }

    public void insert(Instruction pos, Instruction instruction) {
        instruction.setPrev(pos);
        instruction.setNext(pos.next);
        pos.setNext(instruction);
    }

    public void erase(Instruction inst) {
        if (inst.prev != null) {
            inst.prev.next = inst.next;
        } else {
            front = inst.next;
            front.prev = null;
        }
        if (inst.next != null) {
            inst.next.prev = inst.prev;
        } else {
            end = inst.prev;
            end.next = null;
        }
    }

    public Instruction front() {
        return front;
    }

    public Instruction back() {
        return end;
    }

    private void addKnownReg(VirtualRegister reg, Operand val, Integer valTag) {
        if (reg.getSymbolTable() == currentSymbolTable && val instanceof IntLiteral)
            reg.setVal((IntLiteral) val, valTag);
        else
            reg.setVal(null, valTag);
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
