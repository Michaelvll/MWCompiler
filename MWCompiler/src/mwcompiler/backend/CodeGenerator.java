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

    private String indent = "";

    public CodeGenerator(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        assembly = new StringBuilder();

        programIR.getFunctionMap().values().forEach(func -> {
            if (func.isUserFunc()) append("global", func.name());
        });
        assembly.append("extern printf, scanf, malloc\n");

        assembly.append("\nSECTION .text\n");
        indent = "\t\t";
        programIR.getFunctionMap().values().forEach(this::visit);

        assembly.append("\nSECTION .data\n");
        for (StringLiteral s : programIR.getStringPool().values()) {
            assembly.append(s.getLabel()).append(":\n\t\t").append(" db ").append(s.getVal()).append(", 0\n");
        }
        options.out.print(assembly.toString());
    }


    private BasicBlock nextBlock = null;
    private Function currentFunction = null;

    @Override
    public String visit(Function function) {
        currentFunction = function;
        if (!function.isUserFunc()) return null;
        assembly.append(function.nasmName()).append(":\n");

        // ====== prologue ==========
        /* stack frame
         * ---------
         * function stack
         * --------- rbp
         * origin rbp
         * callee-save
         * return address
         * param n
         * param n-1
         * ... param 1
         * former function stack
         */
        if (!function.isMain()) {
            // Save used callee-save registers
            for (PhysicalRegister reg : function.usedPRegs()) {
                if (calleeSaveRegs.contains(reg)) {
                    append("push", reg);
                }
            }
        } else append("push", RBP);
        append("mov", RBP, RSP);
        append("sub", RSP, function.getVarStackSize());
        // Get params
        List<Var> params = function.paramVars();
        for (int index = 0; index < params.size(); ++index) {
            if (index < paramRegs.size()) {
                if (params.get(index).physicalRegister() != paramRegs.get(index))
                    append("mov", params.get(index), paramRegs.get(index));
            }
        }

        List<BasicBlock> basicBlocks = function.getBasicBlocks();
        int blockSize = basicBlocks.size();
        for (int index = 0; index < blockSize; ++index) {
            BasicBlock block = basicBlocks.get(index);
            // Merge jump
            assembly.append(block.name()).append(":\n");

            if (index < blockSize - 1) nextBlock = basicBlocks.get(index + 1);
            visit(block);
        }

        assembly.append("\n\n");
        return null;
    }


    @Override
    public String visit(BinaryExprInst binaryExprInst) {
        Operand left = binaryExprInst.left();
        Operand right = binaryExprInst.right();
        ExprOps op = binaryExprInst.op();
        if (op.isCompare()) {
            String nasmOp = visitCmp(left, right, op);
            append("set" + nasmOp, RAX.lowByte());
            append("movzx", RAX.toString(), RAX.lowByte());
        } else {
            Operand dst = binaryExprInst.dst();
            Pair<Operand, Operand> newOperand = varToReg(left, right, dst == left);
            if (dst == left) dst = newOperand.first;
            left = newOperand.first;
            right = newOperand.second;
            if (op == ExprOps.DIV || op == ExprOps.MOD) {
                if (right instanceof IntLiteral) {
                    append("mov", RDI, right);
                    right = RDI;
                }
                append("mov", RAX, left);
                append("cqo");
                append("idiv", right);
                append("mov", dst, (op == ExprOps.DIV) ? RAX : RDX);
            } else if (isMem(dst) && left != dst) {
                append("mov", RAX, left);
                append(op.nasmOp(), RAX, right);
                append("mov", dst, RAX);
            } else {
                if (dst != left) append("mov", dst, left);
                append(op.nasmOp(), dst, right);
            }
        }
        return null;
    }


    @Override
    public String visit(BasicBlock block) {
        for (Instruction inst = block.front(); inst != null; inst = inst.next) {
            visit(inst);
        }
        assembly.append("\n");
        return null;
    }

    @Override
    public String visit(MoveInst moveInst) {
        MutableOperand dst = moveInst.dst();
        Operand val = moveInst.val();
        if (val instanceof Var && ((Var) val).isCompareTmp()) val = RAX;
        append("mov", varToReg(dst, val, true));
        return null;
    }

    @Override
    public String visit(FunctionCallInst inst) {
        List<Operand> args = inst.args();
        int argSize = args.size();
        int paramRegSize = paramRegs.size();
        int argStackSize = 0;
        for (int index = 0; index < argSize; ++index) {
            Operand arg = args.get(index);
            if (index < paramRegSize) {
                if (arg instanceof StringLiteral) {
                    String label = ((StringLiteral) arg).getLabel();
                    append("lea", paramRegs.get(index).toString(), "[rel " + label + "]");
                } else append("mov", paramRegs.get(index), arg);
            } else {
                append("push", arg);
                argStackSize += options.PTR_SIZE; // TODO: Need to be improved when not every arg is size of 8
            }
        }
        append("call", inst.function().nasmName());
        if (inst.dst() != null)
            append("mov", inst.dst(), RAX); // dst must be register which is guaranteed by the basic block push back
        if (argSize > paramRegSize) append("add", RSP, argStackSize);
        return null;
    }

    @Override
    public String visit(ReturnInst inst) {
        Operand val = inst.getRetVal();
        if (val instanceof Memory) val = visitMemory((Memory) val, RAX, RSI);
        if (val != null) append("mov", RAX, val);

        // ====== epilogue ==========
        if (!currentFunction.isMain()) {
            assembly.append("\n");
            append("mov", RSP, RBP);
            for (PhysicalRegister reg : currentFunction.usedPRegs()) {
                append("pop", reg);
            }
        } else {
            append("leave");
        }
        append("ret");
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
        BinaryExprInst cmp = inst.getCmp();
        String nasmOp = visitCmp(cmp.left(), cmp.right(), cmp.op());
        append("j" + nasmOp, inst.getIfTrue().name());
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
        return "qword " + memory.toString();
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

    private boolean isMem(Operand x) {
        return x.physicalRegister() == null && !(x instanceof Literal);
    }

    private Pair<Operand, Operand> varToReg(Operand left, Operand right) {
        return varToReg(left, right, false);
    }

    private Pair<Operand, Operand> varToReg(Operand left, Operand right, boolean rightTmp) {
        if (isMem(left) && isMem(right)) {
            if (left instanceof Memory) left = visitMemory((Memory) left, RAX, RSI);
            if (!rightTmp) {
                append("mov", RSI, left);
                if (left == right) right = RSI;
                left = RSI;
            }
            if (right instanceof Memory) right = visitMemory((Memory) right, RAX, RDI);
            if (rightTmp) {
                append("mov", RDI, right);
                if (left == right) left = RDI;
                right = RDI;
            }
        } else {
            if (left instanceof Memory) left = visitMemory((Memory) left, RAX, RSI);
            if (right instanceof Memory) right = visitMemory((Memory) right, RAX, RSI);
        }
        return new Pair<>(left, right);
    }

    private String visitCmp(Operand left, Operand right, ExprOps op) {
        if (left instanceof IntLiteral) {
            Operand tmp = left;
            left = right;
            right = tmp;
            op = op.revert();
        }
        append("cmp", varToReg(left, right));
        return op.nasmOp();
    }

    private void append(String s, String dst, String val) {
        append(s, dst + ", " + val);
    }

    private void append(String s, Operand dst, Operand val) {
        append(s, visit(dst), visit(val));
    }

    private void append(String s, Operand dst, int val) {
        append(s, visit(dst), String.valueOf(val));
    }

    private void append(String s, Pair<Operand, Operand> pair) {
        append(s, visit(pair.first), visit(pair.second));
    }

    private void append(String s, String target) {
        String delimiter = "\t\t";
        if (s.length() >= 4) delimiter = "\t";
        assembly.append(indent).append(s).append(delimiter).append(target).append("\n");
    }

    private void append(String s, Operand target) {
        String delimiter = "\t\t";
        if (s.length() >= 4) delimiter = "\t";
        assembly.append(indent).append(s).append(delimiter).append(visit(target)).append("\n");
    }


    private void append(String s) {
        assembly.append(indent).append(s).append("\n");
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
