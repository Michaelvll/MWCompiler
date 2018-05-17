package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Function {
    private FunctionTypeSymbol functionTypeSymbol;
    private InstanceSymbol instanceSymbol;
    private List<VirtualRegister> paramVReg = new ArrayList<>();
    private BasicBlock startBasicBlock;
    private BasicBlock endBasicBlock;

    private List<ReturnInst> returnInsts = new ArrayList<>();

    private List<BasicBlock> blocks = new LinkedList<>();

    public Function(InstanceSymbol instanceSymbol, FunctionTypeSymbol functionTypeSymbol) {
        this.functionTypeSymbol = functionTypeSymbol;
        this.instanceSymbol = instanceSymbol;
        this.startBasicBlock = new BasicBlock(this, instanceSymbol.getName());
    }

    public void AddParam(VirtualRegister reg) {
        paramVReg.add(reg);
    }

    public void AddReturn(ReturnInst ret) {
        returnInsts.add(ret);
    }

    public FunctionTypeSymbol getFunctionTypeSymbol() {
        return functionTypeSymbol;
    }

    public InstanceSymbol getInstanceSymbol() {
        return instanceSymbol;
    }

    public List<VirtualRegister> getParamVReg() {
        return paramVReg;
    }

    //TODO
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }


    public BasicBlock getStartBasicBlock() {
        return startBasicBlock;
    }

    public void pushBack(BasicBlock block) {
        blocks.add(block);
    }

    public List<BasicBlock> getBlocks() {
        return blocks;
    }
}
