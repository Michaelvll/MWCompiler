package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.*;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.SymbolInfo;
import mwcompiler.symbols.SymbolTable;

public class IRBuilder implements AstVisitor<RegOrImm> {
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

    private RegOrImm visit(Node node) {
        return node.accept(this);
    }

    @Override
    public RegOrImm visit(ProgramNode node) {
        visit(node.getBlock());
        return null;
    }

    @Override
    public RegOrImm visit(BlockNode node) {
        currentSymbolTable = node.getCurrentSymbolTable();
        for (Node statement : node.getStatements()) {
            visit(statement);
        }
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
        return null;
    }

    @Override
    public RegOrImm visit(VariableDeclNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getVarSymbol());
        VirtualRegister reg = VirtualRegister.builder(node.getVarSymbol());
        symbolInfo.setReg(reg);
        Instruction init;
        if (node.getInit() != null) {
            RegOrImm value = visit(node.getInit());
            if (value instanceof IntLiteral) {
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
    public RegOrImm visit(FunctionDeclNode node) {
        FunctionTypeSymbol functionTypeSymbol = node.getFunctionTypeSymbol();
        InstanceSymbol instanceSymbol = node.getInstanceSymbol();
        SymbolInfo symbolInfo = currentSymbolTable.findIn(instanceSymbol);
        Function function = new Function(functionTypeSymbol, instanceSymbol);

        //TODO: add function new
        return null;
    }

    @Override
    public RegOrImm visit(ClassDeclNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(BinaryExprNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(IdentifierExprNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(NewExprNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(NullLiteralNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(StringLiteralNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(BoolLiteralNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(IntLiteralNode node) {
        return new IntLiteral(node.getVal());
    }

    @Override
    public RegOrImm visit(EmptyExprNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(FunctionCallNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(DotMemberNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(BrackMemberNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(IfNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(LoopNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(BreakNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(ReturnNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(ContinueNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(ConstructorCallNode node) {
        return null;
    }
}
