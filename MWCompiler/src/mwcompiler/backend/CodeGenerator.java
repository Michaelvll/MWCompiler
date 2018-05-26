package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.ExprOps;
import mwcompiler.utility.Pair;

import java.util.List;

import static mwcompiler.ir.operands.PhysicalRegister.*;

public class CodeGenerator implements IRVisitor<String> {
    private CompilerOptions options;
    private StringBuilder assembly;

    private String indent = "\t\t";

    public CodeGenerator(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        assembly = new StringBuilder();
        assembly.append("\tglobal main\n");
        assembly.append("\textern printf, scanf, malloc\n\n");

        assembly.append("\tsection .data\n");
        for (StringLiteral s : programIR.getStringPool().values()) {
            assembly.append(s.getLabel()).append(":\n\t\t").append(" db ").append(s.getVal()).append(", 0\n");
        }

        assembly.append("\n\tsection .text");
        programIR.getFunctionMap().values().forEach(this::visit);
        System.out.print(assembly.toString());
    }


    private BasicBlock nextBlock = null;

    @Override
    public String visit(Function function) {
        if (function.isLib()) return null;
        assembly.append("\n").append(function.name()).append(":\n");
        if (!function.isMain()) {
            append("push", "rbp");
            append("mov", RBP, RSP);
        }
        List<BasicBlock> basicBlocks = function.getBasicBlocks();
        int blockSize = basicBlocks.size();
        for (int index = 0; index < blockSize; ++index) {
            BasicBlock block = basicBlocks.get(index);
            // Merge jump
            if (nextBlock != null)
                assembly.append(block.name()).append(":\n");

            if (index < blockSize - 1) nextBlock = basicBlocks.get(index + 1);
            visit(block);
        }
        // TODO add prologue
        return null;
    }

    private boolean isMem(Operand x) {
        return x.physicalRegister() == null && !(x instanceof Literal);
    }

    private Pair<Operand, Operand> memToReg(Operand left, Operand right) {
        if (isMem(left) && isMem(right)) {
            if (left instanceof Memory) left = visitMemory((Memory) left, RAX, RSI);
            append("mov", RSI, left);
            left = RSI;
            if (right instanceof Memory) right = visitMemory((Memory) right, RAX, RDI);
        } else {
            if (left instanceof Memory) left = visitMemory((Memory) left, RAX, RSI);
            if (right instanceof Memory) right = visitMemory((Memory) right, RAX, RSI);
        }
        return new Pair<>(left, right);
    }

    @Override
    public String visit(BinaryExprInst binaryExprInst) {
        Operand left = binaryExprInst.left();
        Operand right = binaryExprInst.right();
        MutableOperand dst = binaryExprInst.dst();
        ExprOps op = binaryExprInst.op();
        if (op.isCompare()) {
            append("cmp", memToReg(left, right));
            append("set" + op.nasmOp(), RAX.lowByte());
            append("movzx", RAX.toString(), RAX.lowByte());
        } else {
            Pair<Operand, Operand> newOperand = memToReg(left, right);
            if (isMem(dst)) {
                append("mov", RDI, newOperand.first);
                append(op.nasmOp(), RDI, newOperand.second);
                append("mov", dst, RDI);
            } else {
                append("mov", dst, newOperand.first);
                append(op.nasmOp(), dst, newOperand.second);
            }
        }
        return null;
    }


    @Override
    public String visit(BasicBlock block) {
        for (Instruction inst = block.front(); inst != null; inst = inst.next) {
            visit(inst);
        }
        return null;
    }

    @Override
    public String visit(MoveInst moveInst) {
        MutableOperand dst = moveInst.dst();
        Operand val = moveInst.val();
        if (val instanceof Var && ((Var) val).isCompareTmp()) val = RAX;
        append("mov", memToReg(dst, val));
        return null;
    }

    @Override
    public String visit(FunctionCallInst inst) {
        //TODO
        return null;
    }

    @Override
    public String visit(ReturnInst inst) {
        Operand val = inst.getRetVal();
        if (val instanceof Memory) val = visitMemory((Memory) val, RAX, RSI);
        if (val != null) append("mov", RAX, val);
        append("ret");
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
        BinaryExprInst cmp = inst.getCmp();
        append("cmp", memToReg(cmp.left(), cmp.right()));
        append(inst.cmdName(), inst.getIfTrue().name());
        assert inst.getIfFalse() == nextBlock;
        return null;
    }

    @Override
    public String visit(DirectJumpInst inst) {
        if (inst.target() != nextBlock)
            append("jmp", inst.target().name());
        else nextBlock = null;
        return null;
    }

    @Override
    public String visit(Memory memory) {
        return memory.toString();
    }


    @Override
    public String visit(IntLiteral intLiteral) {
        return String.valueOf(intLiteral.getVal());
    }


    @Override
    public String visit(StringLiteral inst) {
        return inst.getLabel();
    }

    @Override
    public String visit(Register reg) {
        if (reg.physicalRegister() == null) return visit(((Var) reg).stackPos());
        return reg.physicalRegister().toString();
    }

    private String visit(Instruction inst) {
        return inst.accept(this);

    }

    private String visit(Operand operand) {
        return operand.accept(this);
    }

    private Memory visitMemory(Memory memory, PhysicalRegister reg1, PhysicalRegister reg2) {
        Register baseReg = memory.baseReg();
        Register indexReg = memory.indexReg();

        Var baseVar = (Var) baseReg;
        append("mov", reg1, baseVar);
        baseReg = reg1;
        if (indexReg != null) {
            Var indexVar = (Var) indexReg;
            append("mov", reg2, indexVar);
            indexReg = reg2;
        }

        return new Memory(baseReg, indexReg, memory.scale(), memory.disp());
    }

    private void append(String s, String dst, String val) {
        append(s, dst + ", " + val);
    }

    private void append(String s, Operand dst, Operand val) {
        append(s, visit(dst), visit(val));
    }

    private void append(String s, Pair<Operand, Operand> pair) {
        append(s, visit(pair.first), visit(pair.second));
    }

    private void append(String s, String target) {
        String delimiter = "\t\t";
        if (s.length() >= 4) delimiter = "\t";
        assembly.append("\t\t").append(s).append(delimiter).append(target).append("\n");
    }

    private void append(String s) {
        assembly.append("\t\t").append(s).append("\n");
    }

    //    // LRU
//    private LinkedList<PhysicalRegister> usage = new LinkedList<>(PhysicalRegister.regs.subList(6, 16));
//    private Var[] regMap = new Var[PhysicalRegister.REG_NUM];
//
//    private int timeStamp = 0;
//
//    private PhysicalRegister getReg(PhysicalRegister reg, Var var) {
//        if (reg == null) {
//            PhysicalRegister lru = usage.getLast();
//            usage.removeLast();
//            usage.addFirst(lru);
//            return lru;
//        } else if (regMap[reg.id()] == var) {
//            if (reg.id() >= 6) {
//                usage.removeFirstOccurrence(reg);
//                usage.addFirst(reg);
//            }
//            return reg;
//        }
//    }

}
