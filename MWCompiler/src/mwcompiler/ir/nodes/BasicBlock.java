package mwcompiler.ir.nodes;


import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasicBlock {
    private Instruction front;
    private Instruction end;
    private String name;
    private Function parentFunction;
    private SymbolTable currentSymbolTable;

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
        if (end == null) end = instruction;
        if (front != null) front.addPrev(instruction);
        front = instruction;
    }

    private void pushBack(Instruction instruction) {
        if (front == null) front = instruction;
        if (end != null) end.addNext(instruction);
        end = instruction;
    }

    public void pushBack(MoveInst moveInst, Integer valTag) {
        pushBack(moveInst);
        addKnownReg((VirtualRegister) moveInst.getDst(), moveInst.getVal(), valTag);
    }

    public void pushBack(AssignInst assignInst, Integer valTag) {
        pushBack(assignInst);
        addKnownReg((VirtualRegister) assignInst.getDst(), null, valTag);
    }


    public void pushBack(JumpInst jumpInst) {
        //TODO for function call
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
                if (dst != null) {
                    Pair<AssignInst, Boolean> lastAssign = regAssignTable.get(dst);
                    if (lastAssign != null && !lastAssign.second) {
                        delete(lastAssign.first);
                    }
                    regAssignTable.put(dst, new Pair<>(assignInst, false));
                }
            }
        }
        //TODO: Code Below has a problem for
//        for (Map.Entry<Register, Pair<AssignInst, Boolean>> item : regAssignTable.entrySet()) {
//            if (!item.getValue().second) delete(item.getValue().first);
//        }
    }


    public void delete(Instruction inst) {
        inst.delete();
        if (inst.next == null) end = inst.prev;
        if (inst.prev == null) front = inst.next;
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

}
