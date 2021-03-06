package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.assign.UnaryExprInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.utility.CompilerOptions;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.StringJoiner;

public class DumpIRVisitor implements IRVisitor<String> {
    private String indent;
    private PrintStream out;
    private CompilerOptions options;

    public DumpIRVisitor(CompilerOptions options) {
        indent = "";
        this.options = options;
        this.out = options.irOut;
    }

    public DumpIRVisitor(OutputStream out) {
        indent = "";
        this.out = new PrintStream(out);
    }

    public void apply(ProgramIR programIR) {
        programIR.functionMap().values().forEach(this::visit);
        programIR.getStringPool().values().forEach(s -> println(s.getLabel() + " db " + s.stringVal()));
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
//        instruction.liveOut().forEach(x -> print(x.irName() + " "));
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
        iprintln("ret " + (ret.retVal() != null ? visit(ret.retVal()) : ""));
        subIndent();
        return null;
    }

    @Override
    public String visit(Function inst) {
        if (inst.notUserFunc()) return null; // output extern func is good for nasm
        println("");
        iprint("func " + inst.name() + " ");
        StringJoiner params = new StringJoiner(" ");
        inst.paramVars().forEach(param -> params.add(visit(param)));
        print(params.toString());
        println(" {");
        inst.basicBlocks().forEach(this::visit);
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
//        iprintln( inst.op() + " %" + inst.ifTrue().name() + " %" + inst.ifFalse().name());
        iprintln("br " + visit(inst.getCmp().dst()) + " %" + inst.ifTrue().name() + " %" + inst.ifFalse().name());
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
        print("call " + inst.functionName() + " ");
        StringJoiner args = new StringJoiner(" ");
        inst.args().forEach(arg -> args.add(visit(arg)));
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
        return reg.irName();
    }

    @Override
    public String visit(UnaryExprInst unaryExprInst) {
        addIndent();
        iprintln(visit(unaryExprInst.dst()) + " = " + unaryExprInst.op().toString() + visit(unaryExprInst.src()));
        subIndent();
        return null;
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
        return String.valueOf(intLiteral.val());
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
