package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.nodes.assign.UnaryExprInst;
import mwcompiler.ir.nodes.jump.CondJumpInst;
import mwcompiler.ir.nodes.jump.DirectJumpInst;
import mwcompiler.ir.nodes.jump.ReturnInst;
import mwcompiler.ir.operands.*;
import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.ExprOps;
import mwcompiler.utility.Pair;
import mwcompiler.utility.StringProcess;

import java.util.List;
import java.util.Map;

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

        if (options.nasmLibIncludeCMD) assembly.append("%include \"lib/lib.asm\"\n");
        assembly.append("global main\n");
        assembly.append("extern printf, scanf, malloc, strlen, strcmp, sscanf, puts\n");

        assembly.append("\nSECTION .text\n");
        indent = "\t\t";
        programIR.functionMap().values().forEach(this::visit);

        assembly.append("\nSECTION .data\talign=8\n");
        for (StringLiteral s : programIR.getStringPool().values()) {
            assembly.append(s.getLabel()).append(":\n\t\t").append("db ").append(s.hexVal()).append("\n");
        }
        for (Map.Entry<Var, IntLiteral> entry : programIR.getGlobalPool().entrySet()) {
            IntLiteral val = entry.getValue();
            if (val != null)
                assembly.append(entry.getKey().nasmName()).append(":\n\t\t").append("dq ")
                        .append(val.val()).append("\n");
        }
        assembly.append("\nSECTION .bss\talign=8\n");
        for (Map.Entry<Var, IntLiteral> entry : programIR.getGlobalPool().entrySet()) {
            IntLiteral val = entry.getValue();
            if (val == null)
                assembly.append(entry.getKey().nasmName()).append(":\n\t\t").append("resq ")
                        .append(1).append("\n");
        }
        options.out.print(assembly.toString());
//        System.err.print(assembly.toString());
    }


    private BasicBlock nextBlock = null;
    private Function currentFunction = null;

    @Override
    public String visit(Function function) {
        currentFunction = function;
        if (function.notUserFunc()) return null;
        assembly.append(function.nasmName()).append(":\n");

        // ====== prologue ==========
        /* stack frame
         * ---------
         * function stack
         * --------- rbp
         * origin rbp
         * callee-save regs
         * return address
         * param n
         * param n-1
         * ... param 7
         * former function stack
         */
        if (!function.isMain()) {
            // Save used callee-save registers
            for (PhysicalRegister reg : function.usedCalleeRegs()) {
                if (calleeSaveRegs.contains(reg)) {
                    append("push", reg);
                }
            }
        } else append("push", RBP);
        append("mov", RBP, RSP);
        append("sub", RSP, function.getVarStackSize() + options.FUNC_CALL_STACK_ALIGN_SIZE);
        append("and", RSP.nasmName(), "-0x10");
        // Get params
        List<Var> params = function.paramVars();
        for (int index = 0; index < params.size(); ++index) {
            if (params.get(index).isUnused()) {
                System.err.println("Param " + StringProcess.getRefString(params.get(index).irName())
                        + "is unused in function " + StringProcess.getRefString(function.name()));
                continue;
            }
            if (index < paramRegs.size()) {
                if (params.get(index).physicalRegister() != paramRegs.get(index))
                    append("mov", params.get(index), paramRegs.get(index));
            } else if (!isMem(params.get(index))) {
                append("mov", params.get(index), params.get(index).stackPos());
            }
        }

        List<BasicBlock> basicBlocks = function.basicBlocks();
        int blockSize = basicBlocks.size();
        for (int index = 0; index < blockSize; ++index) {
            BasicBlock block = basicBlocks.get(index);
            // Merge jump
            assembly.append(block.name()).append(":\n");
            append("","");

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
            append("movzx", RAX.irName(), RAX.lowByte());
        } else {
            Operand dst = binaryExprInst.dst();
            Pair<Operand, Operand> newOperand = varToReg(left, right, dst.varEquals(left));
            if (dst == left) dst = newOperand.first;
            left = newOperand.first;
            right = newOperand.second;
            if (op == ExprOps.LSFT || op == ExprOps.RSFT) {
                if (left != RAX) {
                    append("mov", RAX, left);
                }
                append("mov", RCX, right);
                append(op.nasmOp(), RAX.nasmName(), RCX.lowByte());
                append("mov", dst, RAX);
            } else if (op == ExprOps.DIV || op == ExprOps.MOD) {
                if (right instanceof IntLiteral) {
                    append("mov", RDI, right);
                    right = RDI;
                }
                append("mov", RAX, left);
                append("cqo");
                append("idiv", right);
                append("mov", dst, (op == ExprOps.DIV) ? RAX : RDX);
            } else if ((isMem(dst) && (dst != left || op == ExprOps.MUL)) || dst.varEquals(right)) {
                append("mov", RAX, left);
                append(op.nasmOp(), RAX, right);
                append("mov", dst, RAX);
            } else {
                if (dst != left) append("mov", dst, left);
                append(op.nasmOp(), dst, right);
                if (!binaryExprInst.dst().varEquals(dst)) append("mov", binaryExprInst.dst(), dst);

            }
        }
        return null;
    }


    @Override
    public String visit(BasicBlock block) {
        preMovOp = null;
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
        for (int index = 0; index < Math.min(paramRegSize, argSize); ++index) {
            Operand arg = args.get(index);
            arg = visitMemory(arg, RAX, RCX);
            if (arg instanceof StringLiteral) {
                String label = ((StringLiteral) arg).getLabel();
                append("lea", paramRegs.get(index).irName(), "[rel " + label + "]");
            } else append("mov", paramRegs.get(index), arg);
        }
        if (argSize > paramRegSize) argStackSize = (argSize - paramRegSize) * options.PTR_SIZE;
        if (argStackSize % 16 != 0) {
            append("sub", RSP, options.PTR_SIZE);
            argStackSize += options.PTR_SIZE;
        }
        for (int index = argSize - 1; index >= paramRegSize; --index) {
            Operand arg = args.get(index);
            arg = visitMemory(arg, RAX, RCX);
            append("push", arg);
        }
        if (inst.function() == Function.SCANF_INT) {
            visitScanfInt(inst, RSI);
            return null;
        }
        if (inst.function() == Function.STR_PARSE_INT) {
            visitScanfInt(inst, RDX);
            return null;
        }
        if (inst.function() == Function.STR_ORD) {
            Operand dst = inst.dst();
            if (isMem(dst)) dst = RAX;
            append("movsx", visit(dst), "byte [" + RSI.nasmName() + " + " + RDI.nasmName() + "]");
            if (isMem(inst.dst())) append("mov", inst.dst(), RAX);
            return null;
        }

        //        if (inst.function() == Function.PRINTF || inst.function() == Function.PRINT_STR) append("xor", RAX, RAX);
        // TODO: Align stack to 16
        append("call", inst.function().nasmName());
        if (inst.dst() != null)
            append("mov", inst.dst(), RAX); // dst must be register(Var) which is guaranteed by the basic block push back
        if (argStackSize > 0) append("add", RSP, argStackSize);
        return null;
    }


    @Override
    public String visit(ReturnInst inst) {
        Operand val = inst.retVal();
        val = visitMemory(val, RAX, RSI);
        if (val != null) append("mov", RAX, val);

        // ====== epilogue ==========
        if (!currentFunction.isMain()) {
            assembly.append("\n");
            append("mov", RSP, RBP);
            List<PhysicalRegister> usedRegs = currentFunction.usedCalleeRegs();
            for (int index = usedRegs.size() - 1; index >= 0; --index) {
                PhysicalRegister reg = usedRegs.get(index);
                if (calleeSaveRegs.contains(reg)) append("pop", reg);
            }
        } else append("leave");
        append("ret");
        return null;
    }

    @Override
    public String visit(CondJumpInst inst) {
        BinaryExprInst cmp = inst.getCmp();
        String nasmOp = visitCmp(cmp.left(), cmp.right(), cmp.op());
        append("j" + nasmOp, inst.ifTrue().name());
        if (inst.ifFalse() != nextBlock) append("jmp", inst.ifFalse().name());
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
        return "qword " + memory.nasmName();
    }


    @Override
    public String visit(IntLiteral intLiteral) {
        return String.valueOf(intLiteral.val());
    }


    @Override
    public String visit(StringLiteral inst) {
        return inst.getLabel();
    }

    @Override
    public String visit(Register reg) {
        if (reg.isGlobal()) return "qword [rel " + reg.nasmName() + "]";
        else if (reg.physicalRegister() == null) return visit(((Var) reg).stackPos());
        return reg.physicalRegister().nasmName();
    }

    @Override
    public String visit(UnaryExprInst unaryExprInst) {
        MutableOperand dst = unaryExprInst.dst();
        Operand src = visitMemory(unaryExprInst.src(), RAX, RSI);
        ExprOps op = unaryExprInst.op();
        String opName = op.nasmOp();
        if (op == ExprOps.SUB) opName = "neg";
        if (src == dst) append(opName, src);
        else {
            append("mov", RAX, src);
            append(opName, RAX);
            append("mov", dst, RAX);
        }
        return null;
    }

    private String visit(Instruction inst) {
        return inst.accept(this);

    }

    private String visit(Operand operand) {
        return operand.accept(this);
    }

    private Operand visitMemory(Operand operand, PhysicalRegister reg1, PhysicalRegister reg2) {
        if (!(operand instanceof Memory)) return operand;
        Memory memory = (Memory) operand;
        Register baseReg = memory.baseReg();
        Register indexReg = memory.indexReg();

        Var baseVar = (Var) baseReg;
        if (isMem(baseVar)) {
            append("mov", reg1, baseVar);
            baseReg = reg1;
        } else baseReg = baseVar.physicalRegister();
        if (indexReg != null) {
            Var indexVar = (Var) indexReg;
            if (isMem(indexVar)) {
                append("mov", reg2, indexVar);
                indexReg = reg2;
            } else indexReg = indexVar.physicalRegister();
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
            left = visitMemory(left, RCX, RSI);
            if (!rightTmp) {
                append("mov", RSI, left);
                if (left == right) right = RSI;
                left = RSI;
            }
            right = visitMemory(right, RCX, RDI);
            if (rightTmp) {
                append("mov", RDI, right);
                if (left == right) left = RDI;
                right = RDI;
            }
        } else {
            left = visitMemory(left, RCX, RSI);
            right = visitMemory(right, RCX, RSI);
        }
        return new Pair<>(left, right);
    }

    private String visitCmp(Operand left, Operand right, ExprOps op) {
        if (left instanceof IntLiteral) {
            op = op.exchange();
            Operand tmp = left;
            left = right;
            right = tmp;
        }
        append("cmp", varToReg(left, right));
        return op.nasmOp();
    }

    private void visitScanfInt(FunctionCallInst inst, PhysicalRegister paramReg) {
        append("");
        if (isMem(inst.dst())) append("lea", paramReg, visitMemory(inst.dst(), RAX, RCX));
        else {
            append("sub", RSP, options.FUNC_CALL_STACK_ALIGN_SIZE);
            append("mov", paramReg, RSP);
        }
        append("call", inst.functionName());
        if (!isMem(inst.dst())) {
            Operand src = new Memory(RSP, null, 0, 0);
            append("mov", RAX, src);
            append("mov", inst.dst(), RAX);
            append("add", RSP, options.FUNC_CALL_STACK_ALIGN_SIZE);
        }
        append("");
    }


    private void append(String s, String dst, String val) {
        append(s, dst + ", " + val);
    }

    private Pair<String, String> preMovOp = null;

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
        if (s.equals("mov")) {
            String[] dstVal = target.split(", ");
            if (dstVal[0].equals(dstVal[1])) return;
            if (preMovOp != null && !dstVal[1].contains("[") && preMovOp.first.equals(dstVal[0]) && preMovOp.second.equals(dstVal[1]))
                return;
            if (preMovOp != null && !dstVal[0].contains("[") && preMovOp.first.equals(dstVal[1]) && preMovOp.second.equals(dstVal[0]))
                return;
            preMovOp = new Pair<>(dstVal[0], dstVal[1]);
        } else {
            preMovOp = null;
        }
        if (s.equals("lea")) target = target.replaceAll("qword ", "");

        String delimiter = "\t\t";
        if (s.length() >= 4) delimiter = "\t";
        assembly.append(indent).append(s).append(delimiter).append(target).append("\n");
    }

    private void append(String s, Operand target) {
        append(s, visit(target));
    }


    private void append(String s) {
        preMovOp = null;
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
