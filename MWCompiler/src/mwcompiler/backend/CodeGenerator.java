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


    @Override
    public String visit(Function function) {
        if (function.isLib()) return null;
        assembly.append("\n").append(function.name()).append(":\n");
        if (!function.isMain()) {
            assembly.append(indent).append("push\trbp\n");
            appendMov(RBP, RSP);
        }
        List<BasicBlock> basicBlocks = function.getBasicBlocks();
        int blockSize = basicBlocks.size();
        for (int index = 0; index < blockSize; ++index) {
            BasicBlock block = basicBlocks.get(index);
            if (index != 0) assembly.append(block.name()).append(":\n");
            visit(block);
        }
        // TODO add prologue
        return null;
    }


    @Override
    public String visit(BinaryExprInst binaryExprInst) {
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
        boolean memDst = dst.physicalRegister() == null;
        boolean memVal = val.physicalRegister() == null && !(val instanceof Literal);
        if (memDst && memVal) {
            if (val instanceof Memory) val = visitMemory((Memory) val, RAX, RSI);
            appendMov(RSI, val);
            val = RSI;
            if (dst instanceof Memory) dst = visitMemory((Memory) dst, RAX, RDI);
        } else {
            if (val instanceof Memory) val = visitMemory((Memory) val, RAX, RSI);
            if (dst instanceof Memory) dst = visitMemory((Memory) dst, RAX, RSI);
        }
        appendMov(dst, val);
        return null;
    }

    @Override
    public String visit(FunctionCallInst inst) {
        return null;
    }

    @Override
    public String visit(ReturnInst inst) {
        Operand val = inst.getRetVal();
        if (val instanceof Memory) val = visitMemory((Memory) val, RAX, RSI);
        appendMov(RAX, val);
        assembly.append("\t\tret\n");
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
//        visitCompare(inst.getCmp());

        return null;
    }

    @Override
    public String visit(DirectJumpInst inst) {
        assembly.append("\t\tjmp\t").append(inst.getTarget().name()).append("\n");
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

        if (baseReg.physicalRegister() != null) baseReg = baseReg.physicalRegister();
        else {
            Var baseVar = (Var) baseReg;
            appendMov(reg1, baseVar.stackPos());
            baseReg = reg1;
        }
        if (indexReg != null) {
            if (indexReg.physicalRegister() != null) indexReg = indexReg.physicalRegister();
            else {
                Var indexVar = (Var) indexReg;
                appendMov(reg2, indexVar.stackPos());
                indexReg = reg2;
            }
        }

        return new Memory(baseReg, indexReg, memory.scale(), memory.disp());
    }

    private void appendMov(Operand dst, Operand val) {
        assembly.append("\t\tmov\t\t").append(visit(dst)).append(", ").append(visit(val)).append("\n");
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
