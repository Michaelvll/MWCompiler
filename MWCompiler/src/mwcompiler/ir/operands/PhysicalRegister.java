package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhysicalRegister extends Register {
    private int id;
    private boolean calleeSave;
    public static final int REG_NUM = 16;

    private PhysicalRegister(int id, boolean calleeSave) {
        this.id = id;
        this.calleeSave = calleeSave;
    }

    public int id() {
        return id;
    }

    public static final PhysicalRegister R0 = new PhysicalRegister(0, false);
    public static final PhysicalRegister R1 = new PhysicalRegister(1, false);
    public static final PhysicalRegister R2 = new PhysicalRegister(2, false);
    public static final PhysicalRegister R3 = new PhysicalRegister(3, true);
    public static final PhysicalRegister R4 = new PhysicalRegister(4, false);
    public static final PhysicalRegister R5 = new PhysicalRegister(5, true);
    public static final PhysicalRegister R6 = new PhysicalRegister(6, true);
    public static final PhysicalRegister R7 = new PhysicalRegister(7, true);
    public static final PhysicalRegister R8 = new PhysicalRegister(8, false);
    public static final PhysicalRegister R9 = new PhysicalRegister(9, false);
    public static final PhysicalRegister R10 = new PhysicalRegister(10, false);
    public static final PhysicalRegister R11 = new PhysicalRegister(11, false);
    public static final PhysicalRegister R12 = new PhysicalRegister(12, true);
    public static final PhysicalRegister R13 = new PhysicalRegister(13, true);
    public static final PhysicalRegister R14 = new PhysicalRegister(14, true);
    public static final PhysicalRegister R15 = new PhysicalRegister(15, true);

    public static final PhysicalRegister RAX = R0;
    public static final PhysicalRegister RCX = R1;
    public static final PhysicalRegister RDX = R2;
    public static final PhysicalRegister RBX = R3;
    public static final PhysicalRegister RSP = R4;
    public static final PhysicalRegister RBP = R5;
    public static final PhysicalRegister RSI = R6;
    public static final PhysicalRegister RDI = R7;

    public static final List<PhysicalRegister> regs = new ArrayList<>(
            Arrays.asList(R0, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15));

    public static final List<PhysicalRegister> paramRegs = new ArrayList<>(
            Arrays.asList(RDI, RSI, RDX, RCX, R8, R9));

    public static final List<String> names = new ArrayList<>(
            Arrays.asList("rax", "rcx", "rdx", "rbx", "rsp", "rbp", "rsi", "rdi", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15"));

    public static final List<PhysicalRegister> calleeSaveRegs = new ArrayList<>(
            Arrays.asList(RBX, RBP, RBX, RBP, RDI, RSI, RSP, R12, R13, R14, R15));

    public static PhysicalRegister get(int i) {
        return regs.get(i);
    }


    @Override
    public String toString() {
        return names.get(id);
    }

    @Override
    public boolean isTmp() {
        throw new RuntimeException("Compiler Bug: Physical register do not have isTmp()");
    }

    public PhysicalRegister physicalRegister(){
        return this;
    }
}
