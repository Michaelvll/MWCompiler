package mwcompiler.ir.operands;

import mwcompiler.symbols.Instance;
import mwcompiler.symbols.SymbolTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Var extends Register {
    private String name;
    private SymbolTable symbolTable;
    private int id = -1;
    private boolean isGlobal = false;
    private Integer size;
    // Stack allocate
    private Memory stackPos;
    // Physical Register
    private PhysicalRegister physicalRegister;
    private int useTime = 0;
    private boolean compareTmp = false;

    // For graph coloring
    private Set<Var> neighbors = new HashSet<>();
    public int degree = 0;
    public boolean deleted = false;

    private static final String GLOBAL_PREFIX = "_user_global_";


    private Var(String name, int id) {
        this.name = name;
        this.id = id;
        this.size = null;
    }

    private Var(Instance symbol, SymbolTable symbolTable, int size) {
        this.name = symbol.getName();
        this.symbolTable = symbolTable;
        this.size = size;
    }

    private static Map<String, Integer> idMap = new HashMap<>();

    public static Var tmpBuilder(String preName) {
        String name = preName + "_tmp";
        Integer search = idMap.get(name);
        Var newVar;
        if (search == null) newVar = new Var(name, 0);
        else newVar = new Var(name, search + 1);
        idMap.put(name, newVar.id);
        return newVar;
    }

    public static Var tmpBuilder(String preName, boolean compareTmp) {
        Var tmp = tmpBuilder(preName);
        tmp.compareTmp = compareTmp;
        return tmp;
    }

    public static Var builder(Instance symbol, SymbolTable symbolTable, int size) {
        Var var = new Var(symbol, symbolTable, size);
        symbolTable.addVar(var);
        return var;
    }

    public String nasmName() {
        return name;
    }

    @Override
    public String irName() {
        return "$" + name + "_" + (isTmp() ? id : symbolTable.hashCode());
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public boolean isTmp() {
        return id >= 0;
    }


    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal() {
        isGlobal = true;
        name = GLOBAL_PREFIX + name;
    }

    public int getSize() {
        return size;
    }

    public Memory stackPos() {
        return stackPos;
    }

    public void setStackPos(Memory stackPos) {
        this.stackPos = stackPos;
    }

    public void addUseTime() {
        ++useTime;
    }

    public int useTime() {
        return useTime;
    }

    public PhysicalRegister getPhysicalRegister() {
        return physicalRegister;
    }

    public void setPhysicalRegister(PhysicalRegister physicalRegister) {
        this.physicalRegister = physicalRegister;
    }

    public PhysicalRegister physicalRegister() {
        return physicalRegister;
    }

    @Override
    public Operand copy(Map<Object, Object> replaceMap) {
        if (isGlobal) return this;
        Operand search = (Operand) replaceMap.get(this);
        if (search == null) {
            search = Var.tmpBuilder(name, compareTmp);
            replaceMap.put(this, search);
        }
        return search;
    }

    @Override
    public Operand dstCopy(Map<Object, Object> replaceMap) {
        if (isGlobal) return this;
        Operand search = (Operand) replaceMap.get(this);
        if (search == null || search instanceof IntLiteral) {
            search = Var.tmpBuilder(name, compareTmp);
            replaceMap.put(this, search);
        }
        return search;
    }

    public boolean isCompareTmp() {
        return compareTmp;
    }

    public Set<Var> neighbors() {
        return neighbors;
    }

    public void setDegree() {
        this.degree = neighbors.size();
    }

    public boolean isAssigned() {
        return physicalRegister != null;
    }

    public boolean isUnused() {
        return physicalRegister == null && stackPos == null;
    }
}
