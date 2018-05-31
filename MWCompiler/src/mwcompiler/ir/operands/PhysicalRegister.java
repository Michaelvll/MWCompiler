package mwcompiler.ir.operands;

import java.util.*;

public class PhysicalRegister extends Register {
    private String name;
    private String lowByteName;
    private int id;
    private boolean calleeSave;
    public static final int REG_NUM = 16;

    private PhysicalRegister(String name, String lowByteName, int id, boolean calleeSave) {
        this.id = id;
        this.calleeSave = calleeSave;
        this.name = name;
        this.lowByteName = lowByteName;
    }

    public int id() {
        return id;
    }

    public static final PhysicalRegister R0 = new PhysicalRegister("rax", "al", 0, false);
    public static final PhysicalRegister R1 = new PhysicalRegister("rcx", "cl", 1, false);
    public static final PhysicalRegister R2 = new PhysicalRegister("rdx", "dl", 2, false);
    public static final PhysicalRegister R3 = new PhysicalRegister("rbx", "bl", 3, true);
    public static final PhysicalRegister R4 = new PhysicalRegister("rsp", "spl", 4, false);
    public static final PhysicalRegister R5 = new PhysicalRegister("rbp", "bpl", 5, true);
    public static final PhysicalRegister R6 = new PhysicalRegister("rsi", "sil", 6, true);
    public static final PhysicalRegister R7 = new PhysicalRegister("rdi", "dib", 7, true);
    public static final PhysicalRegister R8 = new PhysicalRegister("r8", "r8b", 8, false);
    public static final PhysicalRegister R9 = new PhysicalRegister("r9", "r9b", 9, false);
    public static final PhysicalRegister R10 = new PhysicalRegister("r10", "r10b", 10, false);
    public static final PhysicalRegister R11 = new PhysicalRegister("r11", "r11b", 11, false);
    public static final PhysicalRegister R12 = new PhysicalRegister("r12", "r12b", 12, true);
    public static final PhysicalRegister R13 = new PhysicalRegister("r13", "r13b", 13, true);
    public static final PhysicalRegister R14 = new PhysicalRegister("r14", "r14b", 14, true);
    public static final PhysicalRegister R15 = new PhysicalRegister("r15", "r15b", 15, true);

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

    public static final Set<PhysicalRegister> calleeSaveRegs = new HashSet<>(
            Arrays.asList(RBX, RBP, R12, R13, R14, R15));

    public static PhysicalRegister get(int i) {
        return regs.get(i);
    }


    @Override
    public String irName() {
        return name;
    }

    public String nasmName() {
        return name;
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    public String lowByte() {
        return lowByteName;
    }

    @Override
    public boolean isTmp() {
        throw new RuntimeException("Compiler Bug: Physical register do not have isTmp()");
    }

    public PhysicalRegister physicalRegister() {
        return this;
    }

    @Override
    public Operand copy(Map<Object, Object> replaceMap) {
        throw new RuntimeException("??? No copy for physical reg");
    }

    public boolean isCalleeSave() {
        return calleeSave;
    }
}
