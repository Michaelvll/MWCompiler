package mwcompiler.ir.nodes;


import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.JumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.ExprOps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static mwcompiler.ir.operands.IntLiteral.ZERO_LITERAL;

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
        this.name = NameBuilder.builder(parentFunction.getFunctionName());

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

    public Operand pushBack(AssignInst assignInst, Integer valTag) {
        pushBack(assignInst);
        MutableOperand dst = assignInst.getDst();
        if (assignInst instanceof MoveInst) {
            MoveInst moveInst = (MoveInst) assignInst;
            if (dst instanceof Var)
                addKnownReg((Var) dst, moveInst.getVal(), valTag);
        } else if (dst instanceof Var)
            addKnownReg((Var) assignInst.getDst(), null, valTag);
        else if (dst instanceof Memory) {
            Var memDst = Var.tmpBuilder("mem_dst");
            pushBack(new MoveInst(dst, memDst), valTag);
            assignInst.setDst(memDst);
            dst = memDst;
        }
        return dst;
    }


    public void pushBack(JumpInst jumpInst) {
        //TODO for function call
        isEnded = true;
        if (jumpInst instanceof ReturnInst)
            parentFunction.AddReturn((ReturnInst) jumpInst);
        if (jumpInst instanceof CondJumpInst) {
            CondJumpInst condJumpInst = (CondJumpInst) jumpInst;
            assert condJumpInst.getCond() instanceof MutableOperand;
            MutableOperand cond = (MutableOperand) condJumpInst.getCond();
            Var dst = Var.tmpBuilder("cmp");
            BinaryExprInst cmp = new BinaryExprInst(dst, cond, ExprOps.NEQ, ZERO_LITERAL);
            if (cond.isTmp() && end instanceof BinaryExprInst) {
                cmp = (BinaryExprInst) popBack();
            }
            condJumpInst.setCmp(cmp);
        }
        pushBack((Instruction) jumpInst);
        assignTable.clear();
        eliminateAssignInst(); // eliminate the unused assignInst
    }

    public Instruction popBack() {
        return delete(end);
    }

    public Instruction front() {
        return front;
    }

    public Instruction back() {
        return end;
    }


    private void eliminateAssignInst() {
        Map<Register, Boolean> defineTable = new HashMap<>();
        for (Instruction inst = end; inst != null; inst = inst.prev) {
            if (inst instanceof AssignInst) {
                AssignInst assignInst = (AssignInst) inst;
                if (assignInst.getDst() instanceof Register) {
                    Register dst = (Register) assignInst.getDst();
                    Boolean latterDef = defineTable.get(dst);
                    if (latterDef != null && latterDef) {
                        delete(inst);
                    }
                    defineTable.put(dst, true);
                }
                for (Register reg : assignInst.usedRegister()) {
                    defineTable.put(reg, false);
                }
            } else if (inst instanceof ReturnInst) {
                //Nothing to do, as last define of a register will always be kept
            }
        }
    }


    public Instruction delete(Instruction inst) {
        inst.delete();
        if (inst.next == null) end = inst.prev;
        if (inst.prev == null) front = inst.next;
        return inst;
    }

    private void addKnownReg(Register operand, Operand val, Integer valTag) {
        Var reg = (Var) operand;
        if (val instanceof Literal) {
            assignTable.put(reg, (Literal) val);
            if (reg.getSymbolTable() == currentSymbolTable) {
                reg.setVal((Literal) val, valTag);
                return;
            }
        } else {
            assignTable.put(reg, null);
        }
        reg.setVal(null, valTag);
    }

    public Literal getKnownReg(Register reg, Integer valTag) {
        Literal val = assignTable.get(reg);
        if (val != null) return val;
        return reg.getVal(valTag);
    }

    public String getName() {
        return name;
    }

    public Function getParentFunction() {
        return parentFunction;
    }

    public SymbolTable getCurrentSymbolTable() {
        return currentSymbolTable;
    }

    private Map<Register, Literal> assignTable = new HashMap<>();

}
