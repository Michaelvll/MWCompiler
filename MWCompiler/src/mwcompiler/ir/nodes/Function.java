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
    private List<Var> paramVars = new ArrayList<>();
    private SymbolTable symbolTable;

    public enum FuncType {
        USER, EXTERN, LIB, TEMP
    }

    private final FuncType funcType;

    private List<ReturnInst> returnInsts = new ArrayList<>();

    private LinkedList<BasicBlock> basicBlocks = new LinkedList<>();

    private boolean isInline = false;
    private static final int INLINE_BOUND = 10;

    // For allocation
    private Set<Var> vars = new HashSet<>();
    private List<PhysicalRegister> usedCalleeSaveRegs = new ArrayList<>();
    private int varStackSize = 0;

    public Function(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
        this.funcType = FuncType.USER;
    }

    private Function(FunctionSymbol functionSymbol, FuncType funcType) {
        this.functionSymbol = functionSymbol;
        this.funcType = funcType;
    }


    public void addParam(Var reg) {
        paramVars.add(reg);
    }

    public void addReturn(ReturnInst ret) {
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

    public String nasmName() {
        String name = functionSymbol.getName();
        if (funcType == FuncType.USER && !isMain()) return "__user_" + name;
        if (funcType == FuncType.LIB) return "__lib_" + name;
        return name;
    }

    public List<Var> paramVars() {
        return paramVars;
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

    public LinkedList<BasicBlock> basicBlocks() {
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
            }
        }

        for (int i = size - 1; i >= 0; --i) {
            BasicBlock block = basicBlocks.get(i);
            if (block.back() instanceof DirectJumpInst) {
                DirectJumpInst directJumpInst = (DirectJumpInst) block.back();
                directJumpInst.setTarget(searchForNewTarget(jumpLabelChangeMap, directJumpInst.target()));
            } else if (block.back() instanceof CondJumpInst) {
                CondJumpInst condJumpInst = (CondJumpInst) block.back();
                condJumpInst.setIfTrue(searchForNewTarget(jumpLabelChangeMap, condJumpInst.ifTrue()));
                condJumpInst.setIfFalse(searchForNewTarget(jumpLabelChangeMap, condJumpInst.ifFalse()));
                if (condJumpInst.ifTrue() == newBlocks.getFirst()) condJumpInst.not();
            }
            if (block.front() != block.back() || !(block.back() instanceof DirectJumpInst)) newBlocks.addFirst(block);
        }
        basicBlocks = newBlocks;
    }

    private BasicBlock searchForNewTarget(Map<BasicBlock, BasicBlock> map, BasicBlock block) {
        BasicBlock preSearch = block;
        BasicBlock search = map.get(block);
        while (search != null) {
            preSearch = search;
            search = map.get(search);
        }
        return preSearch;
    }

    public Set<Var> getVars() {
        return vars;
    }

    public void addUsedPReg(PhysicalRegister register) {
        usedCalleeSaveRegs.add(register);
    }

    public List<PhysicalRegister> usedPRegs() {
        return usedCalleeSaveRegs;
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
                    if (inst instanceof FunctionCallInst && ((FunctionCallInst) inst).function() == this) {
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
    public static final Function PRINT = new Function(FunctionSymbol.PRINT, FuncType.TEMP);
    public static final Function PRINTLN = new Function(FunctionSymbol.PRINTLN, FuncType.TEMP);
    public static final Function GET_STRING = new Function(FunctionSymbol.GET_STRING, FuncType.LIB);
    public static final Function GET_INT = new Function(FunctionSymbol.GET_INT, FuncType.TEMP);
    public static final Function TO_STRING = new Function(FunctionSymbol.TO_STRING, FuncType.LIB);
    public static final Function SIZE = new Function(FunctionSymbol.SIZE, FuncType.TEMP);
    public static final Function LENGTH = new Function(FunctionSymbol.LENGTH, FuncType.EXTERN);
    public static final Function SUBSTRING = new Function(FunctionSymbol.SUBSTRING, FuncType.LIB);
    public static final Function PARSE_INT = new Function(FunctionSymbol.PARSE_INT, FuncType.TEMP);
    public static final Function STR_ORD = new Function(FunctionSymbol.ORD, FuncType.LIB);

    public static final Function PRINTF = new Function(FunctionSymbol.PRINTF, FuncType.EXTERN);
    public static final Function PUTS = new Function(new FunctionSymbol(VOID_TYPE_SYMBOL, "puts", STRING_TYPE_SYMBOL), FuncType.EXTERN);
    public static final Function SCANF_INT = new Function(new FunctionSymbol(INT_TYPE_SYMBOL, "scanf", STRING_TYPE_SYMBOL), FuncType.EXTERN);
    // BuiltIn Function for string Operation
    public static final Function STR_ADD = new Function(new FunctionSymbol(STRING_TYPE_SYMBOL, "string_add", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), FuncType.LIB);
    public static final Function STR_CMP = new Function(new FunctionSymbol(BOOL_TYPE_SYMBOL, "strcmp", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), FuncType.EXTERN);
    public static final Function STR_PARSE_INT = new Function(new FunctionSymbol(INT_TYPE_SYMBOL, "sscanf", STRING_TYPE_SYMBOL, STRING_TYPE_SYMBOL), FuncType.EXTERN);

    public static final Function MALLOC = new Function(new FunctionSymbol(INT_TYPE_SYMBOL, "malloc", INT_TYPE_SYMBOL), FuncType.EXTERN);

    public static final List<Function> builtinFunctions = new ArrayList<>(Arrays.asList(PRINTF, PRINT, PRINTLN, GET_STRING, GET_INT,
            TO_STRING, SIZE, LENGTH, SUBSTRING, PARSE_INT, STR_ORD, STR_ADD, STR_CMP));

    public FuncType funcType() {
        return funcType;
    }

    public boolean isUserFunc() {
        return funcType == FuncType.USER;
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
