package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;

import java.io.PrintStream;

public class DumpIRVisitor implements IRVisitor<String> {
    private String indent;
    private PrintStream out;

    public DumpIRVisitor() {
        indent = "";
        this.out = new PrintStream(System.out);
    }

    public void apply(BasicBlock block) {
        visit(block);
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

    private String visit(SSA ssa) {
        return ssa.accept(this);
    }

    public String visit(BasicBlock block) {
        return null;
    }

    @Override
    public String visit(VirtualRegisterSSA register) {
        return "%" + register.getName();
    }

    @Override
    public String visit(BinaryExpInst binaryExpInst) {
//        println(indent + visit(binaryExpInst.getTarget()) + " = " + binaryExpInst.get)
        return null;
    }

    @Override
    public String visit(MoveInst moveInst) {
        println(indent + visit(moveInst.getTarget()) + " = " + visit(moveInst.getLeft()));
        return null;
    }

    @Override
    public String visit(IntLiteral intLiteral) {
        return String.valueOf(intLiteral.getVal());
    }
}
