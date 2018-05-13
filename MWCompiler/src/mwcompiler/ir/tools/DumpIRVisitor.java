package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.*;
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

    private String visit(RegOrImm regOrImm) {
        return regOrImm.accept(this);
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
        println(block.getName() + ".entry:");
        for (Instruction instruction = block.front(); instruction != null; instruction = instruction.next) {
            visit(instruction);
        }
        return null;
    }

    @Override
    public String visit(Return ret) {
        addIndent();
        println("ret " + (ret.getRetVal() != null ? visit(ret.getRetVal()) : ""));
        subIndent();
        return null;
    }

    @Override
    public String visit(Function function) {
        print("define " + "@" + function.getInstanceSymbol().getName() + "(");
        StringJoiner params = new StringJoiner(", ");
        function.getParamVReg().forEach(param -> params.add(visit(param)));
        print(params.toString());
        println(") {");
        visit(function.getStartBasicBlock());
        println("}");
        //TODO
        return null;
    }

    @Override
    public String visit(MoveInst moveInst) {
        addIndent();
        println(visit(moveInst.getDst()) + " = move " + visit(moveInst.getVal()));
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
