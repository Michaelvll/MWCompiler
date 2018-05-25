package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhysicalRegister extends Register {
    private Integer id;

    private PhysicalRegister(Integer id) {
        this.id = id;
    }

    public static final PhysicalRegister R0 = new PhysicalRegister(0);
    public static final PhysicalRegister R1 = new PhysicalRegister(1);
    public static final PhysicalRegister R2 = new PhysicalRegister(2);
    public static final PhysicalRegister R3 = new PhysicalRegister(3);
    public static final PhysicalRegister R4 = new PhysicalRegister(4);
    public static final PhysicalRegister R5 = new PhysicalRegister(5);
    public static final PhysicalRegister R6 = new PhysicalRegister(6);
    public static final PhysicalRegister R7 = new PhysicalRegister(7);
    public static final PhysicalRegister R8 = new PhysicalRegister(8);
    public static final PhysicalRegister R9 = new PhysicalRegister(9);
    public static final PhysicalRegister R10 = new PhysicalRegister(10);
    public static final PhysicalRegister R11 = new PhysicalRegister(11);
    public static final PhysicalRegister R12 = new PhysicalRegister(12);
    public static final PhysicalRegister R13 = new PhysicalRegister(13);
    public static final PhysicalRegister R14 = new PhysicalRegister(14);
    public static final PhysicalRegister R15 = new PhysicalRegister(15);

    public static final PhysicalRegister RAX = R0;
    public static final PhysicalRegister RCX = R1;
    public static final PhysicalRegister RDX = R2;
    public static final PhysicalRegister RBX = R3;
    public static final PhysicalRegister RSP = R4;
    public static final PhysicalRegister RBP = R5;
    public static final PhysicalRegister RSI = R6;
    public static final PhysicalRegister RDI = R7;

    public static final List<PhysicalRegister> physicalRegisters = new ArrayList<>(
            Arrays.asList(R0, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15));

    public static final List<String> names = new ArrayList<>(
            Arrays.asList("rax", "rcx", "rdx", "rbx", "rsp", "rbp", "rsi", "rdi", "r8", "r9", "r10", "r11", "r12", "r13", "r14", "r15"));

    public static PhysicalRegister get(Integer i) {
        return physicalRegisters.get(i);
    }


    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return null;
    }

    @Override
    public String toString() {
        return names.get(id);
    }

    @Override
    public Boolean isTmp() {
        throw new RuntimeException("Compiler Bug: Physical register do not have isTmp()");
    }
}
