package mwcompiler.ir.nodes;

import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.symbols.NonArrayTypeSymbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static mwcompiler.symbols.NonArrayTypeSymbol.*;

public class Function {
    private FunctionSymbol functionSymbol;
    private List<Var> paramVReg = new ArrayList<>();

    private final Boolean isLib;

    private List<ReturnInst> returnInsts = new ArrayList<>();

    private LinkedList<BasicBlock> blocks = new LinkedList<>();

    private Boolean isInlined = false;
    private static final Integer INLINE_BOUND = 10;

    public Function(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
        this.isLib = false;
    }

    private Function(FunctionSymbol functionSymbol, Boolean isLib) {
        this.functionSymbol = functionSymbol;
        this.isLib = isLib;
    }


    public void AddParam(Var reg) {
        paramVReg.add(reg);
    }

    public void AddReturn(ReturnInst ret) {
        returnInsts.add(ret);
    }


    public Boolean needReturn() {
        return functionSymbol.getReturnType() != NonArrayTypeSymbol.VOID_TYPE_SYMBOL;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public String getFunctionName() {
        return functionSymbol.getName();
    }

    public List<Var> getParamVReg() {
        return paramVReg;
    }

    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public void pushBack(BasicBlock block) {
        blocks.addLast(block);
    }

    public void pushFront(BasicBlock block) {
        blocks.addFirst(block);
    }

    public List<BasicBlock> getBlocks() {
        return blocks;
    }

    public void checkInlineable() {
        // Will be used in the future
        isInlined = true;
        if (functionSymbol == FunctionSymbol.MAIN) isInlined = false;
        else {
            Integer cnt = 0;
            for (BasicBlock block : blocks) {
                if (block.getName().contains("loop") || cnt > INLINE_BOUND) isInlined = false;
                for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                    ++cnt;
                    if (inst instanceof FunctionCallInst && ((FunctionCallInst) inst).getFunction() == this) {
                        isInlined = false;
                        return;
                    }
                }
                if (!isInlined) return;
            }
        }
    }

    public Boolean isInlined() {
        return isInlined;
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

    public Boolean isLib() {
        return isLib;
    }
}
