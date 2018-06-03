package mwcompiler.ir.nodes.assign;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.symbols.BaseTypeSymbol;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        for (Operand operand : args) {
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

    @Override
    public AssignInst copy(Map<Object, Object> replaceMap) {
        List<Operand> newArgs = new ArrayList<>();
        for (Operand arg : args) {
            newArgs.add(arg.copy(replaceMap));
        }
        MutableOperand dst = null;
        if (dst() != null) dst = (MutableOperand) dst().dstCopy(replaceMap);
        return new FunctionCallInst(function,
                newArgs,
                (Register) dst
        );
    }

    @Override
    public AssignInst sameCopy() {
        return new FunctionCallInst(function, args, (Register) dst());
    }

    @Override
    public AssignInst processKnownReg(BasicBlock basicBlock) {
        for (int index = 0; index < args.size(); ++index) {
            if (args.get(index) instanceof Register) {
                Literal argLiteral = basicBlock.getKnownReg((Register) args.get(index));
                if (argLiteral != null) args.set(index, argLiteral);
            }
        }
        return this;
    }
}
