package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.StringLiteral;
import mwcompiler.ir.operands.VirtualRegister;
import mwcompiler.utility.ExprOps;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.StringJoiner;

public class DumpIRVisitor implements IRVisitor<String> {
    private String indent;
    private PrintStream out;

    public DumpIRVisitor() {
        indent = "";
        this.out = new PrintStream(System.out);
    }

    public DumpIRVisitor(OutputStream out) {
        indent = "";
        this.out = new PrintStream(out);
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

    private void iprintln(String s) {
        this.out.println(indent + s);
    }

    private void iprint(String s) {
        this.out.print(indent + s);
    }

    private void print(String s) {
        this.out.print(s);
    }

    private void println(String s) {
        this.out.println(s);
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
        addIndent();
        iprintln("%" + block.getName() + ":");
        for (Instruction instruction = block.front(); instruction != null; instruction = instruction.next) {
            visit(instruction);
        }
        println("");
        subIndent();
        return null;
    }

    @Override
    public String visit(ReturnInst ret) {
        addIndent();
        iprintln("ret " + (ret.getRetVal() != null ? visit(ret.getRetVal()) : ""));
        subIndent();
        return null;
    }

    @Override
    public String visit(Function inst) {
        if (inst.isLib()) return null; // output extern func is good for nasm
        println("");
        iprint("func " + inst.getFunctionName() + " ");
        StringJoiner params = new StringJoiner(" ");
        inst.getParamVReg().forEach(param -> params.add(visit(param)));
        print(params.toString());
        println(" {");
        inst.getBlocks().forEach(this::visit);
        iprintln("}");
        //TODO
        return null;
    }

    @Override
    public String visit(MoveInst inst) {
        addIndent();
        iprintln(visit(inst.getDst()) + " = MOV " + visit(inst.getVal()));
        subIndent();
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
        addIndent();
        iprintln("br " + visit(inst.getCond()) + " %" + inst.getIfTrue().getName() + " %" + inst.getIfFalse().getName());
        subIndent();
        return null;
    }

    @Override
    public String visit(DirectJumpInst inst) {
        addIndent();
        iprintln("jmp %" + inst.getTarget().getName());
        subIndent();
        return null;
    }

    @Override
    public String visit(FunctionCallInst inst) {
        addIndent();
        if (inst.getDst() != null) iprint(visit(inst.getDst()) + " = ");
        print("call " + inst.getFunctionName() + " ");
        StringJoiner args = new StringJoiner(" ");
        inst.getArgs().forEach(arg -> args.add(visit(arg)));
        println(args.toString());
        subIndent();
        return null;
    }

    @Override
    public String visit(StringLiteral inst) {
        return inst.getLabel();
    }

    @Override
    public String visit(VirtualRegister register) {
        return "$" + register.getName();
    }

    @Override
    public String visit(BinaryExprInst binaryExprInst) {
        addIndent();
        iprintln(visit(binaryExprInst.getDst()) + " = " + binaryExprInst.getOp().toString() + " " + visit(binaryExprInst.getLeft())
                + " " + visit(binaryExprInst.getRight()));
        subIndent();
        return null;
    }

    @Override
    public String visit(IntLiteral intLiteral) {
        return String.valueOf(intLiteral.getVal());
    }
}
