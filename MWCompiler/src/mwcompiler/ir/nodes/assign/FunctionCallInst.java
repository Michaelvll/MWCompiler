package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.BaseTypeSymbol;

import java.util.LinkedList;
import java.util.List;

public class FunctionCallInst extends AssignInst {
    private Function function;
    private List<Operand> args;

    public FunctionCallInst(Function function, List<Operand> args, Register dst) {
        super((function.getFunctionSymbol().getReturnType() != BaseTypeSymbol.VOID_TYPE_SYMBOL) ? dst : null);
        this.function = function;
        this.args = args;
    }

    @Override
    public List<Var> usedVar() {
        LinkedList<Var> registers = new LinkedList<>();
        for (Operand operand:args) {
            appendUsedVar(operand, registers);
        }
        return registers;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Function function() {
        return function;
    }

    public String functionName() {
        return function.name();
    }

    public List<Operand> args() {
        return args;
    }
}
