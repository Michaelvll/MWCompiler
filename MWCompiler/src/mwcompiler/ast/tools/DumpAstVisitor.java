package mwcompiler.ast.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.nodes.declarations.ClassDeclNode;
import mwcompiler.ast.nodes.declarations.FunctionDeclNode;
import mwcompiler.ast.nodes.declarations.VariableDeclNode;
import mwcompiler.ast.nodes.expressions.*;
import mwcompiler.ast.nodes.literals.BoolLiteralNode;
import mwcompiler.ast.nodes.literals.IntLiteralNode;
import mwcompiler.ast.nodes.literals.NullLiteralNode;
import mwcompiler.ast.nodes.literals.StringLiteralNode;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.TypeSymbol;

import java.io.PrintStream;
import java.util.List;

// A tool for dumping the AST to the stdout
public class DumpAstVisitor implements AstVisitor<Void> {
    private String indent;
    private PrintStream out;

    public void apply(Node node) {
        visit(node);
    }

    private void visit(Node node) {
        node.accept(this);
    }

    public DumpAstVisitor() {
        indent = "";
        this.out = new PrintStream(System.out);
    }

    public DumpAstVisitor(PrintStream out) {
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
    public Void visit(ProgramNode node) {
        println("<<ProgramNode>>");
        List<Node> declarators = node.getBlock().getStatements();
        declarators.forEach(this::visit);
        return null;
    }

    @Override
    public Void visit(ClassDeclNode node) {
        addIndent();
        println("<<ClassDeclNode>>");
        println("name: <" + node.getClassSymbol() + ", " + node.getClassSymbol().getName() + ">");
        println("body:");
        node.getBody().getStatements().forEach(this::visit);
        subIndent();
        return null;
    }


    @Override
    public Void visit(EmptyExprNode node) {
        addIndent();
        println("<EmptyExprNode>");
        subIndent();
        return null;
    }

    @Override
    public Void visit(FunctionCallNode node) {
        addIndent();
        println("<FunctionCallNode>");
        println("caller:");
        visit(node.getCaller());
        if (!node.getArgs().isEmpty()) {
            println("args: ");
            node.getArgs().forEach(this::visit);
        }
        subIndent();
        return null;
    }

    @Override
    public Void visit(DotMemberNode node) {
        addIndent();
        println("<DotMemberNode>");
        println("container: ");
        visit(node.getContainer());
        println("member: <" + node.getMember().getInstanceSymbol() + ", " + node.getMember().getInstanceSymbol().getName() + ">");
        subIndent();
        return null;
    }

    @Override
    public Void visit(BrackMemberNode node) {
        addIndent();
        println("<BrackMemberNode>");
        println("container: ");
        visit(node.getContainer());
        println("subscript: ");
        visit(node.getSubscript());
        subIndent();
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        addIndent();
        println("<IfNode>");
        if (node.getCondition() != null) {
            println("cond: ");
            visit(node.getCondition());
        }
        println("body: ");
        visit(node.getBody());
        if (node.getElseNode() != null) {
            println("else: ");
            visit(node.getElseNode());
        }
        subIndent();
        return null;
    }

    @Override
    public Void visit(LoopNode node) {
        addIndent();
        println("<LoopNode>");
        if (node.getVarInit() != null) {
            println("varDecl: ");
            visit(node.getVarInit());
        }
        if (node.getCondition() != null) {
            println("cond: ");
            visit(node.getCondition());
        }
        if (node.getStep() != null) {
            println("step: ");
            visit(node.getStep());
        }
        visit(node.getBody());
        subIndent();
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        addIndent();
        println("<BreakNode>");
        subIndent();
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        addIndent();
        println("<ReturnNode>");
        if (node.getReturnVal() != null) {
            println("returnVal: ");
            visit(node.getReturnVal());
        }
        subIndent();
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        addIndent();
        println("<ContinueNode>");
        subIndent();
        return null;
    }

    @Override
    public Void visit(ConstructorCallNode node) {
        addIndent();
        println("<ConstructorCallNode>");
        println("type: <" + node.getClassTypeSymbol() + ", " + node.getClassTypeSymbol().getName() + ">");
        if (!node.getArgs().isEmpty()) {
            println("args: ");
            node.getArgs().forEach(this::visit);
        }
        subIndent();
        return null;
    }


    @Override
    public Void visit(VariableDeclNode node) {
        addIndent();
        println("<VariableDeclNode>");
        println("type: <" + node.getTypeSymbol() + ", " + node.getTypeSymbol().getName() + ">");
        println("var: <" + node.getVarSymbol() + ", " + node.getVarSymbol().getName() + ">");
        if (node.getInit() != null) {
            println("init:");
            visit(node.getInit());
        }
        subIndent();
        return null;
    }

    @Override
    public Void visit(FunctionDeclNode node) {
        addIndent();
        println("<<FunctionDeclNode>>");
        FunctionSymbol functionSymbol = node.getFunctionSymbol();
        println("type: <" + functionSymbol.getReturnType() + ", " + functionSymbol.getReturnType().getName() + ">");
        println("name: <" + node.getInstanceSymbol() + ", " + node.getInstanceSymbol().getName() + ">");
        println("params:");
        node.getParamList().forEach(this::visit);
        println("body:");
        visit(node.getBody());
        subIndent();
        return null;
    }


    @Override
    public Void visit(IdentifierExprNode node) {
        addIndent();
        println("<IdentifierExprNode>");
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        println("val: <" + instanceSymbol + ", " + instanceSymbol.getName() + ">");
        subIndent();
        return null;
    }

    @Override
    public Void visit(StringLiteralNode node) {
        addIndent();
        println("<StringLiteralNode>");
        println("val: " + node.getVal());
        subIndent();
        return null;
    }

    @Override
    public Void visit(BoolLiteralNode node) {
        addIndent();
        println("<BoolLiteralNode>");
        println("Val: " + String.valueOf(node.getVal()));
        subIndent();
        return null;
    }

    @Override
    public Void visit(IntLiteralNode node) {
        addIndent();
        println("<IntLiteralNode>");
        println("val: " + String.valueOf(node.getVal()));
        subIndent();
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        addIndent();
        println("<<BlockNode>>");
        subIndent();
        node.getStatements().forEach(this::visit);
        return null;
    }


    @Override
    public Void visit(BinaryExprNode node) {
        addIndent();
        println("<BinaryExprNode>");
        println("op: " + node.getOp().toString());
        println("left: ");
        visit(node.getLeft());
        println("right: ");
        visit(node.getRight());
        subIndent();
        return null;
    }

    @Override
    public Void visit(UnaryExprNode node) {
        addIndent();
        println("<UnaryExprNode>");
        println("op: " + node.getOp().toString());
        println("expr: ");
        visit(node.getExpr());
        subIndent();
        return null;
    }

    @Override
    public Void visit(NewExprNode node) {
        addIndent();
        println("<NewExprNode>");
        TypeSymbol createType = node.getCreateType();
        println("type: <" + createType + ", " + createType.getName() + ">");
        if (node.getDimArgs().size() != 0) {
            println("dimArgs: ");
            node.getDimArgs().forEach(this::visit);
        }
        subIndent();
        return null;
    }

    @Override
    public Void visit(NullLiteralNode node) {
        addIndent();
        println("<NullLiteralNode>");
        subIndent();
        return null;
    }
}
