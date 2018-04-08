package mwcompiler.ast.tools;

import mwcompiler.ast.*;

import java.io.PrintStream;
import java.util.List;

public class AstDump implements AstVisitor {
    private String indent;
    private PrintStream out;

    public AstDump() {
        indent = "";
        this.out = new PrintStream(System.out);
    }

    public AstDump(PrintStream out) {
        indent = "";
        this.out = out;
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


    @Override
    public void visit(ProgramNode node) {
        println("<<ProgramNode>>");
        List<DeclaratorNode> declarators = node.getDeclarators();
        for (DeclaratorNode declarator : declarators) {
            declarator.accept(this);
        }
    }

    @Override
    public void visit(NonArrayTypeNode node) {
        println("type: " + node.type.getName());
    }

    @Override
    public void visit(ArrayTypeNode node) {
        StringBuilder s = new StringBuilder("type: " + node.type.getName());
        for (int i = 0; i < node.getDimension(); ++i) s.append("[]");
        println(String.valueOf(s));
    }


    @Override
    public void visit(VariableDeclNode node) {
        addIndent();
        println("<<VariableDeclNode>>");
        node.type.accept(this);
        println("var: " + node.var.getName());
        if (node.init != null) {
            println("init:");
            node.init.accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(IdentifierNode node) {

    }

    @Override
    public void visit(IdentifierExprNode node) {
        addIndent();
        println("<IdentifierExprNode>");
        println("Val: " + node.getName());
        subIndent();
    }


    @Override
    public void visit(BinaryExprNode node) {

    }

    @Override
    public void visit(StringLiteralNode node) {
        addIndent();
        println("<StringLiteralNode>");
        println("Val: " + node.getVal());
        subIndent();
    }

    @Override
    public void visit(BoolLiteralNode node) {
        addIndent();
        println("<BoolLiteralNode>");
        println("Val: " + String.valueOf(node.getVal()));
        subIndent();
    }

    @Override
    public void visit(IntLiteralNode node) {
        addIndent();
        println("<IntLiteralNode>");
        println("Val: " + String.valueOf(node.getVal()));
        subIndent();
    }

    @Override
    public void visit(NullLiteralNode node) {

    }
}
