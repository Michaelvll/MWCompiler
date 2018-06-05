package mwcompiler.ir.nodes;

import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;

import java.util.*;

public abstract class Instruction {
    public Instruction prev;
    public Instruction next;

    // For liveness analysis
    private Set<Register> liveIn = new HashSet<>();
    private Set<Register> liveOut = new HashSet<>();


    public abstract <T> T accept(IRVisitor<T> visitor);

    public abstract List<Var> usedVar();

    public abstract List<Var> dstVar();

    public List<Var> usedLocalVar() {
        List<Var> localVars = new ArrayList<>();
        for (Var var : usedVar()) {
            if (!var.isGlobal()) localVars.add(var);
        }
        return localVars;
    }

    public List<Var> dstLocalVar() {
        List<Var> localVars = new ArrayList<>();
        for (Var var : dstVar()) {
            if (!var.isGlobal()) localVars.add(var);
        }
        return localVars;
    }

    public Set<Register> liveIn() {
        return liveIn;
    }

    public Set<Register> liveOut() {
        return liveOut;
    }

    public void addPrev(Instruction prevInst) {
        if (this.prev != null) this.prev.next = prevInst;
        prevInst.next = this;
        prevInst.prev = this.prev;
        this.prev = prevInst;
    }

    void addNext(Instruction nextInst) {
        if (this.next != null) this.next.prev = nextInst;
        nextInst.prev = this;
        nextInst.next = this.next;
        this.next = nextInst;
    }

    public void delete() {
        if (prev != null) prev.next = next;
        if (next != null) next.prev = prev;
    }

    public abstract Instruction copy(Map<Object, Object> replaceMap);

    public abstract Instruction sameCopy();
}
