package mwcompiler.ast.tools;

import mwcompiler.ast.nodes.*;

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
        println("type: " + node.type.getName());
        println("dim: " + node.getDim());
    }

    @Override
    public void visit(VoidTypeNode node) {
        println("type: void");
    }

    @Override
    public void visit(ClassDeclNode node) {
        addIndent();
        println("<<ClassDeclNode>>");
        println("name: " + node.getName());
        println("body:");
        for (DeclaratorNode declarator : node.getBody()) {
            declarator.accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(NullTypeNode node) {
        println("Constructor function");
    }

    @Override
    public void visit(NullExprNode node) {

    }

    @Override
    public void visit(FunctionCallNode node) {
        addIndent();
        println("<FunctionCallNode>");
        println("caller:");
        node.getCaller().accept(this);
        println("args: ");
        for(ExprNode arg: node.getArgs()) {
            arg.accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(DotMemberNode node) {
        addIndent();
        println("<DotMemberNode>");
        println("mom: ");
        node.getMom().accept(this);
        println("member: " + node.getMember());
        subIndent();
    }

    @Override
    public void visit(BrackMemberNode node) {
        addIndent();
        println("<BrackMemberNode>");
        println("mom: ");
        node.getMom().accept(this);
        println("subscript: ");
        node.getSubscript().accept(this);
        subIndent();
    }


    @Override
    public void visit(VariableDeclNode node) {
        addIndent();
        println("<VariableDeclNode>");
        node.getType().accept(this);
        println("var: " + node.getVar());
        if (node.getInit() != null) {
            println("init:");
            node.getInit().accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(FunctionDeclNode node) {
        addIndent();
        println("<<FunctionDeclNode>>");
        node.getReturnType().accept(this);
        println("name: " + node.getName());
        println("params:");
        for (Node param : node.getParamList()) {
            param.accept(this);
        }
        println("body:");
        node.getBody().accept(this);
        subIndent();
    }


    @Override
    public void visit(IdentifierExprNode node) {
        addIndent();
        println("<IdentifierExprNode>");
        println("val: " + node.getName());
        subIndent();
    }

    @Override
    public void visit(StringLiteralNode node) {
        addIndent();
        println("<StringLiteralNode>");
        println("val: " + node.getVal());
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
        println("val: " + String.valueOf(node.getVal()));
        subIndent();
    }

    @Override
    public void visit(BlockNode node) {
        addIndent();
        println("<<BlockNode>>");
        subIndent();
        for (Node statement : node.statements) {
            statement.accept(this);
        }
    }


    @Override
    public void visit(BinaryExprNode node) {
        addIndent();
        println("<BinaryExprNode>");
        println("op: " + node.getOp().toString());
        println("left: ");
        node.getLeft().accept(this);
        println("right: ");
        node.getRight().accept(this);
        subIndent();

    }

    @Override
    public void visit(UnaryExprNode node) {
        addIndent();
        println("<UnaryExprNode>");
        println("op: " + node.getOp().toString());
        println("expr: ");
        node.getExpr().accept(this);
        subIndent();
    }

    @Override
    public void visit(NewExprNode node) {
        addIndent();
        println("<NewExprNode>");
        println("type: " + node.createType.getName());
        println("dim: " + String.valueOf(node.dim));
        if (node.dimArgs.size() != 0) {
            println("dimArgs: ");
            for (Node expr : node.dimArgs) {
                expr.accept(this);
            }
        }
        subIndent();
    }

    @Override
    public void visit(NullLiteralNode node) {
        println("null");
    }
}
