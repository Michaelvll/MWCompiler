package mwcompiler.ir.nodes;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;

import java.util.ArrayList;
import java.util.List;

public class Function {
    private FunctionTypeSymbol functionTypeSymbol;
    private InstanceSymbol instanceSymbol;
    private List<VirtualRegister> paramVReg = new ArrayList<>();
    private BasicBlock startBasicBlock;
    private BasicBlock endBasicBlock;

    private List<Return> returns = new ArrayList<>();

    public Function(InstanceSymbol instanceSymbol, FunctionTypeSymbol functionTypeSymbol) {
        this.functionTypeSymbol = functionTypeSymbol;
        this.instanceSymbol = instanceSymbol;
        this.startBasicBlock = new BasicBlock(this, instanceSymbol.getName());
    }

    public void AddParam(VirtualRegister reg) {
        paramVReg.add(reg);
    }

    public void AddReturn(Return ret) {
        returns.add(ret);
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
}
