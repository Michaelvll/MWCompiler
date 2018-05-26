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
        iprintln("%" + block.name() + ":");
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
        iprint("func " + inst.name() + " ");
        StringJoiner params = new StringJoiner(" ");
        inst.getParamVReg().forEach(param -> params.add(visit(param)));
        print(params.toString());
        println(" {");
        inst.getBasicBlocks().forEach(this::visit);
        iprintln("}");
        //TODO
        return null;
    }

    @Override
    public String visit(MoveInst inst) {
        addIndent();
        iprintln(visit(inst.dst()) + " = MOV " + visit(inst.val()));
        subIndent();
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
        visit(inst.getCmp());
        addIndent();
//        iprintln( inst.op() + " %" + inst.getIfTrue().name() + " %" + inst.getIfFalse().name());
        iprintln("br " + visit(inst.getCmp().dst()) + " %" + inst.getIfTrue().name() + " %" + inst.getIfFalse().name());
        subIndent();
        return null;
    }

    @Override
    public String visit(DirectJumpInst inst) {
        addIndent();
        iprintln("jmp %" + inst.target().name());
        subIndent();
        return null;
    }

    @Override
    public String visit(FunctionCallInst inst) {
        addIndent();
        iprint("");
        if (inst.dst() != null) print(visit(inst.dst()) + " = ");
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
    public String visit(Register reg) {
        return reg.toString();
    }

    @Override
    public String visit(BinaryExprInst binaryExprInst) {
        addIndent();
        iprintln(visit(binaryExprInst.dst()) + " = " + binaryExprInst.op().toString() + " " + visit(binaryExprInst.left())
                + " " + visit(binaryExprInst.right()));
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
        if (memory.baseReg() != null) s += visit(memory.baseReg());
        if (memory.indexReg() != null) s += " + " + visit(memory.indexReg()) + " * " + memory.scale();
        if (memory.disp() != 0) s += " + " + memory.disp();
        s += "]";
        subIndent();
        return s;
    }
}
