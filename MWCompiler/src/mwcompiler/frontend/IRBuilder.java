package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.*;
import mwcompiler.symbols.SymbolInfo;
import mwcompiler.symbols.SymbolTable;

public class IRBuilder implements AstVisitor<SSA> {
    private BasicBlock startBasicBlock;
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;

    public BasicBlock build(Node node) {
        visit(node);
        return startBasicBlock;
    }

    public BasicBlock getStartBasicBlock() {
        return startBasicBlock;
    }

    private SSA visit(Node node) {
        return node.accept(this);
    }

    @Override
    public SSA visit(ProgramNode node) {
        currentBasicBlock = new BasicBlock();
        startBasicBlock = currentBasicBlock;
        visit(node.getBlock());
        return null;
    }

    @Override
    public SSA visit(BlockNode node) {
        currentSymbolTable = node.getCurrentSymbolTable();
        for (Node statement : node.getStatements()) {
            visit(statement);
        }
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
        return null;
    }

    @Override
    public SSA visit(VariableDeclNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getVarSymbol());
        VirtualRegisterSSA reg = VirtualRegisterSSA.builder(node.getVarSymbol());
        symbolInfo.setReg(reg);
        Instruction init;
        if (node.getInit() != null) {
            SSA value = visit(node.getInit());
            if (value instanceof IntLiteralSSA) {
                init = new MoveInst(reg, value);
                currentBasicBlock.push_back(init);
            } else {
                init = currentBasicBlock.getEnd();
                init.setTarget(reg);
            }
        }
        return null;
    }

    @Override
    public SSA visit(FunctionDeclNode node) {
        return null;
    }

    @Override
    public SSA visit(ClassDeclNode node) {
        return null;
    }

    @Override
    public SSA visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public SSA visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public SSA visit(IdentifierExprNode node) {

        return null;
    }

    @Override
    public SSA visit(NewExprNode node) {
        return null;
    }

    @Override
    public SSA visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public SSA visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public SSA visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public SSA visit(IntLiteralNode node) {
        return new IntLiteralSSA(node.getVal());
    }

    @Override
    public SSA visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public SSA visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public SSA visit(DotMemberNode node) {
        return null;
    }

    @Override
    public SSA visit(BrackMemberNode node) {
        return null;
    }

    @Override
    public SSA visit(IfNode node) {
        return null;
    }

    @Override
    public SSA visit(LoopNode node) {
        return null;
    }

    @Override
    public SSA visit(BreakNode node) {
        return null;
    }

    @Override
    public SSA visit(ReturnNode node) {
        return null;
    }

    @Override
    public SSA visit(ContinueNode node) {
        return null;
    }

    @Override
    public SSA visit(ConstructorCallNode node) {
        return null;
    }
}
