package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Register extends MutableOperand {
    private Literal val;
    private Integer valTag;

    // for coloring
    private Set<Register> neighbors = new HashSet<>();
    public int degree = 0;
    public boolean deleted = false;
    private Set<Register> movNeighors = new LinkedHashSet<>();

    public Literal getVal(Integer valTag) {
        if (valTag.equals(this.valTag))
            return val;
        return null;
    }

    public void setVal(Literal val, Integer valTag) {
        this.val = val;
        if (valTag != null)
            this.valTag = valTag;
    }

    public Set<Register> neighbors() {
        return neighbors;
    }

    public Set<Register> movNeighors() {return movNeighors;}

    public abstract boolean isAssigned();

    public void setDegree() {
        this.degree = neighbors.size();
    }


    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public abstract String nasmName();

    public abstract String irName();


    public abstract boolean isGlobal();

    public abstract void setPhysicalRegister(PhysicalRegister preg);
}
