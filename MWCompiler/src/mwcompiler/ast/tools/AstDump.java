package mwcompiler.ast.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;

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
        List<Node> declarators = node.getDeclarators().getStatements();
        for (Node declarator : declarators) {
            declarator.accept(this);
        }
    }

    @Override
    public void visit(NonArrayTypeNode node) {
        println("typename: " + node.getTypename());
    }

    @Override
    public void visit(ArrayTypeNode node) {
        println("typename: " + node.getTypename());
        println("dim: " + node.getDim());
    }

    @Override
    public void visit(VoidTypeNode node) {
        println("typename: void");
    }

    @Override
    public void visit(ClassDeclNode node) {
        addIndent();
        println("<<ClassDeclNode>>");
        println("name: <" + node.getClassSymbol() + ", " + node.getClassSymbol().getName() +">");
        println("body:");
        for (Node declarator : node.getBody().getStatements()) {
            declarator.accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(NullTypeNode node) {
        println("Constructor function");
    }

    @Override
    public void visit(EmptyExprNode node) {
        addIndent();
        println("<EmptyExprNode>");
        subIndent();
    }

    @Override
    public void visit(FunctionCallNode node) {
        addIndent();
        println("<FunctionCallNode>");
        println("caller:");
        node.getCaller().accept(this);
        println("args: ");
        for (ExprNode arg : node.getArgs()) {
            arg.accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(DotMemberNode node) {
        addIndent();
        println("<DotMemberNode>");
        println("container: ");
        node.getContainer().accept(this);
        println("member: " + node.getMember());
        subIndent();
    }

    @Override
    public void visit(BrackMemberNode node) {
        addIndent();
        println("<BrackMemberNode>");
        println("container: ");
        node.getContainer().accept(this);
        println("subscript: ");
        node.getSubscript().accept(this);
        subIndent();
    }

    @Override
    public void visit(IfNode node) {
        addIndent();
        println("<IfNode>");
        if (node.getCondition() != null) {
            println("cond: ");
            node.getCondition().accept(this);
        }
        println("body: ");
        node.getBody().accept(this);
        if (node.getElseCondition() != null) {
            println("else: ");
            node.getElseCondition().accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(LoopNode node) {
        addIndent();
        println("<LoopNode>");
        if (node.getVarDecl() != null) {
            println("varDecl: ");
            node.getVarDecl().accept(this);
        }
        if (node.getCondition() != null) {
            println("cond: ");
            node.getCondition().accept(this);
        }
        if (node.getStep() != null) {
            println("step: ");
            node.getStep().accept(this);
        }
        node.getBody().accept(this);
        subIndent();
    }

    @Override
    public void visit(BreakNode node) {
        addIndent();
        println("<BreakNode>");
        subIndent();
    }

    @Override
    public void visit(ReturnNode node) {
        addIndent();
        println("<ReturnNode>");
        if (node.getReturnVal() != null) {
            println("returnVal: ");
            node.getReturnVal().accept(this);
        }
        subIndent();
    }

    @Override
    public void visit(ContinueNode node) {
        addIndent();
        println("<ContinueNode>");
        subIndent();
    }


    @Override
    public void visit(VariableDeclNode node) {
        addIndent();
        println("<VariableDeclNode>");
        node.getType().accept(this);
        println("var: <" + node.getVar()+", "+node.getVar().getName() +">");
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
        FunctionTypeSymbol functionTypeSymbol = node.getFunctionTypeSymbol();
        println("type: <" + functionTypeSymbol.getReturnType() + ", " +functionTypeSymbol.getReturnType().getName() +">");
        println("name: <" + node.getInstanceSymbol()+", "+node.getInstanceSymbol().getName()+">");
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
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        println("val: <" + instanceSymbol + ", " + instanceSymbol.getName() + ">");
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
        for (Node statement : node.getStatements()) {
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
        TypeNode createType = node.getCreateType();
        println("typename: " + createType.getTypename());
        if (createType instanceof ArrayTypeNode) {
            println("dim: "+String.valueOf(((ArrayTypeNode) createType).getDim()));
        }
        if (node.getDimArgs().size() != 0) {
            println("dimArgs: ");
            for (Node expr : node.getDimArgs()) {
                expr.accept(this);
            }
        }
        subIndent();
    }

    @Override
    public void visit(NullLiteralNode node) {
        addIndent();
        println("<NullLiteralNode>");
        subIndent();
    }
}
