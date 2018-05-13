package mwcompiler.ir.nodes;


public abstract class AssignInst extends Instruction {
    private Register dst;

    public AssignInst(Register dst) {
        this.dst = dst;
    }

    public void setDst(Register dst) {
        this.dst = dst;
    }

    public Register getDst() {
        return dst;
    }
}
