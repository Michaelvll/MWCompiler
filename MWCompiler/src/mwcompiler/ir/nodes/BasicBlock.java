package mwcompiler.ir.nodes;


import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.JumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.NameBuilder;
import mwcompiler.symbols.SymbolTable;
import mwcompiler.utility.ExprOps;

import java.util.HashMap;
import java.util.Map;

import static mwcompiler.ir.operands.IntLiteral.ZERO_LITERAL;

public class BasicBlock {
    private Instruction front;
    private Instruction end;
    private String name;
    private Function parentFunction;
    private SymbolTable currentSymbolTable;

    public boolean isEnded = false;

    private int instNum = 0;

//    private Map<Register, IntLiteral> regIntMap = new HashMap<>();

    public BasicBlock(Function parentFunction, SymbolTable currentSymbolTable) {
        this.parentFunction = parentFunction;
        this.currentSymbolTable = currentSymbolTable;
        this.name = NameBuilder.builder(parentFunction.name());

    }

    public BasicBlock(Function parentFunction, SymbolTable currentSymbolTable, String name) {
        this.parentFunction = parentFunction;
        this.currentSymbolTable = currentSymbolTable;
        this.name = NameBuilder.builder(name);
    }


    private void pushBack(Instruction instruction) {
        if (front == null) front = instruction;
        if (end != null) end.addNext(instruction);
        end = instruction;
        ++instNum;
    }

    public Operand pushBack(AssignInst assignInst, int valTag) {
        MutableOperand dst = assignInst.dst();
        if (assignInst instanceof MoveInst) {
            MoveInst moveInst = (MoveInst) assignInst;
            if (dst instanceof Var)
                addKnownReg((Var) dst, moveInst.val(), valTag);
            else if (moveInst.val() instanceof Memory) {
                Var memDst = Var.tmpBuilder("mem_dst");
                pushBack(new MoveInst(memDst, moveInst.val()));
                moveInst.setVal(memDst);
            }
            pushBack(assignInst);
        } else {

            pushBack(assignInst);
            if (dst instanceof Var)
                addKnownReg((Var) assignInst.dst(), null, valTag);
            else if (dst instanceof Memory) {
                Var memDst = Var.tmpBuilder("mem_dst");
                pushBack(new MoveInst(dst, memDst), valTag);
                assignInst.setDst(memDst);
                dst = memDst;
            }
            if (assignInst instanceof FunctionCallInst && parentFunction != null)
                parentFunction.addCallee(((FunctionCallInst) assignInst).function());
        }
        return dst;
    }


    public void pushBack(JumpInst jumpInst) {
        isEnded = true;
        if (jumpInst instanceof ReturnInst)
            parentFunction.addReturn((ReturnInst) jumpInst);
        if (jumpInst instanceof CondJumpInst) {
            CondJumpInst condJumpInst = (CondJumpInst) jumpInst;
            if (condJumpInst.getCond() instanceof IntLiteral) {
                if (((IntLiteral) condJumpInst.getCond()).val() == 0)
                    jumpInst = new DirectJumpInst(condJumpInst.ifFalse());
                else jumpInst = new DirectJumpInst(condJumpInst.ifTrue());
            } else if (condJumpInst.getCmp() == null) {
                assert condJumpInst.getCond() instanceof MutableOperand;
                MutableOperand cond = (MutableOperand) condJumpInst.getCond();
                Var dst = Var.tmpBuilder("cmp");
                BinaryExprInst cmp = new BinaryExprInst(dst, cond, ExprOps.NEQ, ZERO_LITERAL);
                if (cond.isTmp() && end instanceof BinaryExprInst) {
                    cmp = (BinaryExprInst) popBack();
                }
                condJumpInst.setCmp(cmp);
            }
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
//                if (assignInst instanceof MoveInst && assignInst.dst() == ((MoveInst) assignInst).val())
//                    delete(inst);
                if (assignInst.dst() instanceof Register) {
                    Var dst = (Var) assignInst.dst();
                    Boolean latterDef = defineTable.get(dst);
                    if (latterDef != null && latterDef) delete(inst);
                    else {
                        defineTable.put(dst, true);
//                        if (!assignInst.isCompare() && parentFunction != null) parentFunction.addVar(dst);
                    }
                }
                for (Register reg : assignInst.usedVar()) {
                    defineTable.put(reg, false);
                }
            } else if (inst instanceof JumpInst) {
                //Nothing to do, as last define of a register will always be kept
            }
        }
    }


    public Instruction delete(Instruction inst) {
        inst.delete();
        if (inst.next == null) end = inst.prev;
        if (inst.prev == null) front = inst.next;
        --instNum;
        return inst;
    }

    private void addKnownReg(Register operand, Operand val, int valTag) {
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

    public Literal getKnownReg(Register reg, int valTag) {
        Literal val = assignTable.get(reg);
        if (val != null) return val;
        return reg.getVal(valTag);
    }

    public String name() {
        return name;
    }

    public Function parentFunction() {
        return parentFunction;
    }

    public SymbolTable getCurrentSymbolTable() {
        return currentSymbolTable;
    }

    private Map<Register, Literal> assignTable = new HashMap<>();

    public boolean empty() {
        return front == null;
    }

    public int instNum() {
        return instNum;
    }

    public void setFront(Instruction front) {
        this.front = front;
    }

    public void setEnd(Instruction end) {
        this.end = end;
    }

    public BasicBlock copy(Map<Object, Object> replaceMap) {
        BasicBlock search = (BasicBlock) replaceMap.get(this);
        if (search == null) {
            search = new BasicBlock((Function) replaceMap.get(parentFunction), currentSymbolTable, ((Function) replaceMap.get(parentFunction)).name() + "_inline_" + name);
            replaceMap.put(this, search);
        }
        return search;
    }

    public BasicBlock deepCopy(Map<Object, Object> replaceMap) {
        BasicBlock newBlock = this.copy(replaceMap);
        for (Instruction inst = front; inst != null; inst = inst.next) {
            if (inst instanceof AssignInst) newBlock.pushBack(inst.copy(replaceMap));
            else if (inst instanceof ReturnInst) {
                if (((ReturnInst) inst).retVal() != null)
                    newBlock.pushBack(((ReturnInst) inst).copy(replaceMap));
                newBlock.pushBack(new DirectJumpInst((BasicBlock) replaceMap.get("inline_next")));
            } else newBlock.pushBack(inst.copy(replaceMap));
        }
        return newBlock;
    }

    public void setName(String name) {
        this.name = name;
    }
}
