package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;

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
        programIR.getStringPool().values().forEach(s -> println(s.getLabel() + " db " + s.getVal()));
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
        visit(inst.getCmp());
        addIndent();
//        iprintln( inst.getOp() + " %" + inst.getIfTrue().getName() + " %" + inst.getIfFalse().getName());
        iprintln("br " + visit(inst.getCmp().getDst()) + " %" + inst.getIfTrue().getName() + " %" + inst.getIfFalse().getName());
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
        iprint("");
        if (inst.getDst() != null) print(visit(inst.getDst()) + " = ");
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
    public String visit(Var register) {
        return register.toString();
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

    @Override
    public String visit(Memory memory) {
        addIndent();
        String s = "[";
        if (memory.getBaseReg() != null) s += visit(memory.getBaseReg());
        if (memory.getIndexReg() != null) s += " + " + visit(memory.getIndexReg()) + " * " + memory.getScale();
        if (memory.getDisplacement() != 0) s += " + " + memory.getDisplacement();
        s += "]";
        subIndent();
        return s;
    }
}
