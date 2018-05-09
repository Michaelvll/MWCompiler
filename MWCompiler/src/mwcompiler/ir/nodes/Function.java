package mwcompiler.ir.nodes;

import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;

public class Function {
    private FunctionTypeSymbol functionTypeSymbol;
    private InstanceSymbol instanceSymbol;
    private BasicBlock startBasicBlock;
    private BasicBlock endBasicBlock;

    public Function(FunctionTypeSymbol functionTypeSymbol, InstanceSymbol instanceSymbol) {
        this.functionTypeSymbol = functionTypeSymbol;
        this.instanceSymbol = instanceSymbol;
        this.startBasicBlock = new BasicBlock(this, instanceSymbol.getName());
    }
    //TODO
}
