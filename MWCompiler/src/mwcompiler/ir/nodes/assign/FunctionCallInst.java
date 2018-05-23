package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.NonArrayTypeSymbol;

import java.util.List;

public class FunctionCallInst extends AssignInst {
    private Function function;
    private List<Operand> args;

    public FunctionCallInst(Function function, List<Operand> args, Register dst) {
        super((function.getFunctionSymbol().getReturnType() != NonArrayTypeSymbol.VOID_TYPE_SYMBOL) ? dst : null);
        this.function = function;
        this.args = args;
    }

    @Override
    public List<Operand> getOperand() {
        return args;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Function getFunction() {
        return function;
    }

    public String getFunctionName() {
        return function.getFunctionName();
    }

    public List<Operand> getArgs() {
        return args;
    }
}
