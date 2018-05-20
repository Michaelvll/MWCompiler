package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.NonArrayTypeSymbol;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Function {
    private FunctionTypeSymbol functionTypeSymbol;
    private InstanceSymbol instanceSymbol;
    private List<VirtualRegister> paramVReg = new ArrayList<>();

    private Boolean isLib;

    private List<ReturnInst> returnInsts = new ArrayList<>();

    private List<BasicBlock> blocks = new LinkedList<>();

    public Function(InstanceSymbol instanceSymbol, FunctionTypeSymbol functionTypeSymbol) {
        this.functionTypeSymbol = functionTypeSymbol;
        this.instanceSymbol = instanceSymbol;
    }

    private Function(InstanceSymbol instanceSymbol, FunctionTypeSymbol functionTypeSymbol, Boolean isLib) {
        this.functionTypeSymbol = functionTypeSymbol;
        this.instanceSymbol = instanceSymbol;
        this.isLib = isLib;
    }


    public void AddParam(VirtualRegister reg) {
        paramVReg.add(reg);
    }

    public void AddReturn(ReturnInst ret) {
        returnInsts.add(ret);
    }


    public Boolean needReturn() {
        return functionTypeSymbol.getReturnType() != NonArrayTypeSymbol.VOID_TYPE_SYMBOL;
    }

    public InstanceSymbol getInstanceSymbol() {
        return instanceSymbol;
    }

    public List<VirtualRegister> getParamVReg() {
        return paramVReg;
    }

    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public void pushBack(BasicBlock block) {
        blocks.add(block);
    }

    public List<BasicBlock> getBlocks() {
        return blocks;
    }

    public static final Function PRINT = new Function(InstanceSymbol.PRINT, FunctionTypeSymbol.PRINT, true);
    public static final Function PRINTLN = new Function(InstanceSymbol.PRINTLN, FunctionTypeSymbol.PRINTLN, true);
    public static final Function GET_STRING = new Function(InstanceSymbol.GET_STRING, FunctionTypeSymbol.GET_STRING, true);
    public static final Function GET_INT = new Function(InstanceSymbol.GET_INT, FunctionTypeSymbol.GET_INT, true);
    public static final Function TO_STRING = new Function(InstanceSymbol.TO_STRING, FunctionTypeSymbol.TO_STRING, true);
    public static final Function SIZE = new Function(InstanceSymbol.SIZE, FunctionTypeSymbol.SIZE, true);
    public static final Function LENGTH = new Function(InstanceSymbol.LENGTH, FunctionTypeSymbol.LENGTH, true);
    public static final Function SUBSTRING = new Function(InstanceSymbol.SUBSTRING, FunctionTypeSymbol.SUBSTRING, true);
    public static final Function PARSE_INT = new Function(InstanceSymbol.PARSE_INT, FunctionTypeSymbol.PARSE_INT, true);
    public static final Function ORD = new Function(InstanceSymbol.ORD, FunctionTypeSymbol.ORD, true);
}
