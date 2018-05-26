package mwcompiler.ir.nodes;

import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.PhysicalRegister;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.BaseTypeSymbol;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.symbols.SymbolTable;

import java.util.*;

import static mwcompiler.symbols.BaseTypeSymbol.*;

public class Function {
    private FunctionSymbol functionSymbol;
    private List<Var> paramVReg = new ArrayList<>();
    private SymbolTable symbolTable;

    private final boolean isLib;

    private List<ReturnInst> returnInsts = new ArrayList<>();

    private LinkedList<BasicBlock> basicBlocks = new LinkedList<>();

    private Set<Var> vars = new HashSet<>();

    private int varStackSize = 0;

    private boolean isInline = false;
    private static final int INLINE_BOUND = 10;

    // For allocation
    private List<PhysicalRegister> usedPhysicalRegs = new ArrayList<>();

    public Function(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
        this.isLib = false;
    }

    private Function(FunctionSymbol functionSymbol, boolean isLib) {
        this.functionSymbol = functionSymbol;
        this.isLib = isLib;
    }


    public void AddParam(Var reg) {
        paramVReg.add(reg);
    }

    public void AddReturn(ReturnInst ret) {
        returnInsts.add(ret);
    }


    public boolean needReturn() {
        return functionSymbol.getReturnType() != BaseTypeSymbol.VOID_TYPE_SYMBOL;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public String name() {
        return functionSymbol.getName();
    }

    public List<Var> getParamVReg() {
        return paramVReg;
    }

    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public void pushBack(BasicBlock block) {
        basicBlocks.addLast(block);
    }

    public void pushFront(BasicBlock block) {
        basicBlocks.addFirst(block);
    }

    public List<BasicBlock> getBasicBlocks() {
        return basicBlocks;
    }


    public void setSymbolTable(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void addVar(Var var) {
//        assert var.isTmp();
        vars.add(var);
    }

    public void cleanUp() {
        Map<BasicBlock, BasicBlock> jumpLabelChangeMap = new HashMap<>();
        LinkedList<BasicBlock> newBlocks = new LinkedList<>();
//        Set<BasicBlock> includeBlocks = new HashSet<>();
        // May need to rearrange the sequence of block
        int size = basicBlocks.size();
        for (int i = size - 1; i >= 0; --i) {
            BasicBlock block = basicBlocks.get(i);
            if (block.front() == block.back() && block.back() instanceof DirectJumpInst) {
                DirectJumpInst directJumpInst = (DirectJumpInst) block.back();
                jumpLabelChangeMap.put(block, directJumpInst.target());
            } else {
                newBlocks.addFirst(block);
                if (block.back() instanceof DirectJumpInst) {
                    DirectJumpInst directJumpInst = (DirectJumpInst) block.back();
                    BasicBlock search = jumpLabelChangeMap.get(directJumpInst.target());
                    if (search != null) directJumpInst.setTarget(search);
                } else if (block.back() instanceof CondJumpInst) {
                    CondJumpInst condJumpInst = (CondJumpInst) block.back();
                    BasicBlock search = jumpLabelChangeMap.get(condJumpInst.getIfTrue());
                    if (search != null) condJumpInst.setIfTrue(search);
                    search = jumpLabelChangeMap.get(condJumpInst.getIfFalse());
                    if (search != null) condJumpInst.setIfFalse(search);
                }
            }
        }
        basicBlocks = newBlocks;
    }

    public Set<Var> getVars() {
        return vars;
    }

    public void addUsedPReg(PhysicalRegister register) {
        usedPhysicalRegs.add(register);
    }

    public List<PhysicalRegister> getUsedPhysicalRegs() {
        return usedPhysicalRegs;
    }

    public void checkInlineable() {
        // Will be used in the future
        isInline = true;
        if (functionSymbol == FunctionSymbol.MAIN) isInline = false;
        else {
            int cnt = 0;
            for (BasicBlock block : basicBlocks) {
                if (block.name().contains("loop") || cnt > INLINE_BOUND) isInline = false;
                for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                    ++cnt;
                    if (inst instanceof FunctionCallInst && ((FunctionCallInst) inst).getFunction() == this) {
                        isInline = false;
                        return;
                    }
                }
                if (!isInline) return;
            }
        }
    }

    public boolean isInline() {
        return isInline;
    }




    // Language BuiltIn Function
    public static final Function PRINT_INT = new Function(FunctionSymbol.PRINT_INT, true);
    public static final Function PRINT_STR = new Function(FunctionSymbol.PRINT_STR, true);
    public static final Function PRINT = new Function(FunctionSymbol.PRINT, true);
    public static final Function PRINTLN = new Function(FunctionSymbol.PRINTLN, true);
    public static final Function GET_STRING = new Function(FunctionSymbol.GET_STRING, true);
    public static final Function GET_INT = new Function(FunctionSymbol.GET_INT, true);
    public static final Function TO_STRING = new Function(FunctionSymbol.TO_STRING, true);
    public static final Function SIZE = new Function(FunctionSymbol.SIZE, true);
    public static final Function LENGTH = new Function(FunctionSymbol.LENGTH, true);
    public static final Function SUBSTRING = new Function(FunctionSymbol.SUBSTRING, true);
    public static final Function PARSE_INT = new Function(FunctionSymbol.PARSE_INT, true);
    public static final Function ORD = new Function(FunctionSymbol.ORD, true);

    // BuiltIn Function for string Operation
    public static final Function STR_ADD = new Function(new FunctionSymbol(STRING_TYPE_SYMBOL, "__string_add_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);
    public static final Function STR_GT = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "__string_gt_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);
    public static final Function STR_LT = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "__string_lt_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);
    public static final Function STR_GTE = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "__string_gte_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);
    public static final Function STR_LTE = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "__string_lte_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);
    public static final Function STR_EQ = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "__string_eq_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);
    public static final Function STR_NEQ = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "__string_neq_", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), true);

    public static final Function MALLOC = new Function(new FunctionSymbol(INT_TYPE_SYMBOL, "malloc", INT_TYPE_SYMBOL), true);

    public static final List<Function> builtinFunctions = new ArrayList<>(Arrays.asList(PRINT_INT, PRINT_STR, PRINT, PRINTLN, GET_STRING, GET_INT,
            TO_STRING, SIZE, LENGTH, SUBSTRING, PARSE_INT, ORD, STR_ADD, STR_GT, STR_LT, STR_GTE, STR_LTE, STR_EQ, STR_NEQ));

    public boolean isLib() {
        return isLib;
    }

    public int getVarStackSize() {
        return varStackSize;
    }

    public void setVarStackSize(int varStackSize) {
        this.varStackSize = varStackSize;
    }

    public boolean isMain() {
        return functionSymbol == FunctionSymbol.MAIN;
    }
}
