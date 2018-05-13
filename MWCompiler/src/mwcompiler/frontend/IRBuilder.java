package mwcompiler.frontend;


import mwcompiler.ast.nodes.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ir.nodes.*;
import mwcompiler.symbols.*;
import mwcompiler.utility.ExprOps;

public class IRBuilder implements AstVisitor<RegOrImm> {
    private BasicBlock currentBasicBlock;
    private SymbolTable currentSymbolTable;
    private ProgramIR programIR = new ProgramIR();

    private Boolean getReg = false;

    public ProgramIR build(Node node) {
        visit(node);
        return programIR;
    }

    private RegOrImm visit(Node node) {
        return node.accept(this);
    }

    private void getCurrentSymbolTable(BlockNode block) {
        currentSymbolTable = block.getCurrentSymbolTable();
    }

    private void getCurrentBasicBlock(Function function) {
        currentBasicBlock = function.getStartBasicBlock();
    }

    @Override
    public RegOrImm visit(ProgramNode node) {
        visit(node.getBlock());
        return null;
    }

    @Override
    public RegOrImm visit(BlockNode node) {
        currentSymbolTable = node.getCurrentSymbolTable();
        node.getStatements().forEach(this::visit);
        currentSymbolTable = currentSymbolTable.getOuterSymbolTable();
        return null;
    }

    @Override
    public RegOrImm visit(VariableDeclNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getVarSymbol());
        VirtualRegister reg = VirtualRegister.builder(node.getVarSymbol());
        symbolInfo.setReg(reg);
        AssignInst init;
        if (node.getInit() != null) {
            RegOrImm value = visit(node.getInit());
            if (value instanceof IntLiteral) {
                init = new MoveInst(reg, value);
                currentBasicBlock.addKnownReg(reg, (IntLiteral) value);
                currentBasicBlock.pushBack(init);
            } else {
                assert currentBasicBlock.back() instanceof AssignInst;
                init = (AssignInst) currentBasicBlock.back();
                init.setDst(reg);
            }
        } else {
            IntLiteral val = new IntLiteral(0);
            currentBasicBlock.addKnownReg(reg, val);
            currentBasicBlock.pushBack(new MoveInst(reg, val));
        }
        return reg;
    }

    private Function getFunction(InstanceSymbol functionSymbol, FunctionTypeSymbol functionTypeSymbol) {
        Function function = programIR.getFunction(functionSymbol);
        if (function == null) {
            function = new Function(functionSymbol, functionTypeSymbol);
            programIR.putFunction(functionSymbol, function);
        }
        return function;
    }

    @Override
    public RegOrImm visit(FunctionDeclNode node) {
        InstanceSymbol functionSymbol = node.getInstanceSymbol();
        FunctionTypeSymbol functionTypeSymbol = node.getFunctionTypeSymbol();
        Function function = getFunction(functionSymbol, functionTypeSymbol);
        getCurrentSymbolTable(node.getBody());
        getCurrentBasicBlock(function);
        node.getParamList().forEach(param -> function.AddParam((VirtualRegister) visit(param)));
        visit(node.getBody());
        if (!(currentBasicBlock.back() instanceof Return)) {
            if (function.getFunctionTypeSymbol().getReturnType() == NonArrayTypeSymbol.VOID_TYPE_SYMBOL) {
                currentBasicBlock.pushBack(new Return(null));
            } else {
                currentBasicBlock.pushBack(new Return(new IntLiteral(0)));
            }
        }
        return null;
    }

    @Override
    public RegOrImm visit(ClassDeclNode node) {
        return null;
    }

    private IntLiteral immCalc(IntLiteral x_, IntLiteral y_, ExprOps op) {
        Integer x = x_.getVal();
        Integer y = y_.getVal();
        switch (op) {
            case ADD: return new IntLiteral(x + y);
            case SUB: return new IntLiteral(x - y);
            case MUL: return new IntLiteral(x * y);
            case DIV: return new IntLiteral(x / y);
            case MOD: return new IntLiteral(x % y);
            default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for IntLiteral");
        }
    }

    private RegOrImm visitAssign(BinaryExprNode node) {
        getReg = true;
        RegOrImm left = visit(node.getLeft());
        getReg = false;
        RegOrImm right = visit(node.getRight());
        if (right instanceof IntLiteral) {
            currentBasicBlock.pushBack(new MoveInst((Register) left, right));
            currentBasicBlock.addKnownReg((Register) left, (IntLiteral) right);
        } else {
            ((AssignInst) currentBasicBlock.back()).setDst((Register) left);
        }
        return left;
    }

    @Override
    public RegOrImm visit(BinaryExprNode node) {
        ExprOps op = node.getOp();
        switch (op) {
            case ASSIGN: return visitAssign(node);
        }
        RegOrImm left = visit(node.getLeft());
        RegOrImm right = visit(node.getRight());
        if (left instanceof IntLiteral && right instanceof IntLiteral) {
            return immCalc((IntLiteral) left, (IntLiteral) right, op);
        }
        VirtualRegister dst = VirtualRegister.builder(op.toString() + "tmp");
        currentBasicBlock.pushBack(new BinaryExprInst(dst, left, op, right));
        return dst;
    }

    @Override
    public RegOrImm visit(UnaryExprNode node) {
        return null;
    }

    @Override
    public RegOrImm visit(IdentifierExprNode node) {
        SymbolInfo symbolInfo = currentSymbolTable.findIn(node.getInstanceSymbol());
        Register reg = symbolInfo.getReg();
        IntLiteral val = currentBasicBlock.getKnownReg(reg);
        return (getReg || val == null) ? reg : val;
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
        ExprNode caller = node.getCaller();
        Function function;
        if (caller instanceof IdentifierExprNode) {
            InstanceSymbol functionSymbol = ((IdentifierExprNode) caller).getInstanceSymbol();
            SymbolInfo functionInfo = currentSymbolTable.findAll(functionSymbol);
            FunctionTypeSymbol functionTypeSymbol = (FunctionTypeSymbol) functionInfo.getTypeSymbol();
            function = getFunction(functionSymbol, functionTypeSymbol);
        } else {
            //TODO
        }
        //TODO
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
        RegOrImm retVal = null;
        if (node.getReturnVal() != null) {
            retVal = visit(node.getReturnVal());
        }
        currentBasicBlock.pushBack(new Return(retVal));
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
