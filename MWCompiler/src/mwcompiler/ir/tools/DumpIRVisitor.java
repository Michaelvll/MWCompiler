package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.utility.ExprOps;

import java.io.PrintStream;
import java.util.StringJoiner;

public class DumpIRVisitor implements IRVisitor<String> {
    private String indent;
    private PrintStream out;

    public DumpIRVisitor() {
        indent = "";
        this.out = new PrintStream(System.out);
    }

    public void apply(ProgramIR programIR) {
        programIR.getFunctionMap().values().forEach(this::visit);
        //TODO
    }

    private void addIndent() {
        indent += "\t";
    }

    private void subIndent() {
        indent = indent.substring(1);
    }

    private void println(String s) {
        this.out.println(indent + s);
    }

    private void print(String s) {
        this.out.print(indent + s);
    }

    private String visit(Operand operand) {
        return operand.accept(this);
    }

    private String visit(Instruction instruction) {
        return instruction.accept(this);
    }

    private String visit(ExprOps op) {
        switch (op) {
            case ADD: return "add";
            default: return null;
        }
    }

    public String visit(BasicBlock block) {
        println("\n" + block.getName() + ":");
        for (Instruction instruction = block.front(); instruction != null; instruction = instruction.next) {
            visit(instruction);
        }
        return null;
    }

    @Override
    public String visit(ReturnInst ret) {
        addIndent();
        println("ret " + (ret.getRetVal() != null ? visit(ret.getRetVal()) : ""));
        subIndent();
        return null;
    }

    @Override
    public String visit(Function inst) {
        print("define " + "@" + inst.getInstanceSymbol().getName() + "(");
        StringJoiner params = new StringJoiner(", ");
        inst.getParamVReg().forEach(param -> params.add(visit(param)));
        print(params.toString());
        println(") {");
        inst.getBlocks().forEach(this::visit);
        println("}");
        //TODO
        return null;
    }

    @Override
    public String visit(MoveInst inst) {
        addIndent();
        println(visit(inst.getDst()) + " = move " + visit(inst.getVal()));
        subIndent();
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
        addIndent();
        println("if " + visit(inst.getCond()) + " " + inst.getIfTrue().getName() + " " + inst.getIfFalse().getName());
        subIndent();
        return null;
    }

    @Override
    public String visit(DirectJumpInst inst) {
        addIndent();
        println("jmp " + inst.getTarget().getName());
        subIndent();
        return null;
    }

    @Override
    public String visit(VirtualRegister register) {
        return "%" + register.getName();
    }

    @Override
    public String visit(BinaryExprInst binaryExprInst) {
        addIndent();
        println(visit(binaryExprInst.getDst()) + " = " + binaryExprInst.getOp().toString() + " " + visit(binaryExprInst.getLeft())
                + " " + visit(binaryExprInst.getRight()));
        subIndent();
        return null;
    }

    @Override
    public String visit(IntLiteral intLiteral) {
        return String.valueOf(intLiteral.getVal());
    }
}
