package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.VirtualRegister;
import mwcompiler.symbols.SymbolInfo;
import mwcompiler.symbols.SymbolTable;

public class IRBuilder implements AstVisitor<Instruction> {
    private BasicBlock startBasicBlock;
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;

    public BasicBlock build(Node node) {
        visit(node);
        return startBasicBlock;
    }

    private Instruction visit(Node node) {
        return node.accept(this);
    }

    @Override
    public Instruction visit(ProgramNode node) {
        currentBasicBlock = new BasicBlock();
        startBasicBlock = currentBasicBlock;
        visit(node.getBlock());
        return null;
    }

    @Override
    public Instruction visit(BlockNode node) {
        currentSymbolTable = node.getCurrentSymbolTable();
        for (Node statement : node.getStatements()) {
            visit(statement);
        }
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
        return null;
    }

    @Override
    public Instruction visit(VariableDeclNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getVarSymbol());
        VirtualRegister reg = VirtualRegister.builder(node.getVarSymbol());
        symbolInfo.setReg(reg);
        Instruction init = null;
        if (node.getInit() != null) {
            init = visit(node.getInit());
            init.setTarget(reg);
        }
        return init;
    }

    @Override
    public Instruction visit(FunctionDeclNode node) {
        return null;
    }

    @Override
    public Instruction visit(ClassDeclNode node) {
        return null;
    }

    @Override
    public Instruction visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public Instruction visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public Instruction visit(IdentifierExprNode node) {

        return null;
    }

    @Override
    public Instruction visit(NewExprNode node) {
        return null;
    }

    @Override
    public Instruction visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public Instruction visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public Instruction visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public Instruction visit(IntLiteralNode node) {
         node.getVal();
        return null;
    }

    @Override
    public Instruction visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public Instruction visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public Instruction visit(DotMemberNode node) {
        return null;
    }

    @Override
    public Instruction visit(BrackMemberNode node) {
        return null;
    }

    @Override
    public Instruction visit(IfNode node) {
        return null;
    }

    @Override
    public Instruction visit(LoopNode node) {
        return null;
    }

    @Override
    public Instruction visit(BreakNode node) {
        return null;
    }

    @Override
    public Instruction visit(ReturnNode node) {
        return null;
    }

    @Override
    public Instruction visit(ContinueNode node) {
        return null;
    }

    @Override
    public Instruction visit(ConstructorCallNode node) {
        return null;
    }
}
