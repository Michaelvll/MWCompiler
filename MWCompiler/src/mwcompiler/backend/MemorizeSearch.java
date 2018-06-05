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
import mwcompiler.ir.operands.*;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.ExprOps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class MemorizeSearch {
    private ProgramIR programIR;
    private CompilerOptions options;

    public MemorizeSearch(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;

        for (Function function : programIR.functionMap().values()) {
            if (function.notUserFunc()) continue;
            identifyMemorizeSearch(function);
            if (function.memorizeable()) {
                initBase(function);
                LinkedList<BasicBlock> newBlocks = new LinkedList<>();
                for (BasicBlock block : function.basicBlocks()) {
                    newBlocks.addAll(processInsts(block));
                }
                function.setBasicBlocks(newBlocks);
            }
        }

    }

    private void initBase(Function function) {
        Function mainFunc = programIR.getFunction(FunctionSymbol.MAIN);
        BasicBlock mainFirstBlock = mainFunc.basicBlocks().get(0);
        BasicBlock memorizeSearchInit = new BasicBlock(mainFunc, mainFirstBlock.getCurrentSymbolTable(), "memorizeSearchInit", mainFirstBlock.valTag());
        memorizeSearchInit.pushBack(new FunctionCallInst(Function.MALLOC, new ArrayList<>(Collections.singleton(new IntLiteral(options.MEMORIZE_SEARCH_LEVEL))), function.memorizeSearchMemBase()));
        memorizeSearchInit.pushBack(new DirectJumpInst(mainFirstBlock));
        LinkedList<BasicBlock> newBlocks = new LinkedList<>(mainFunc.basicBlocks());
        newBlocks.addFirst(memorizeSearchInit);
        mainFunc.setBasicBlocks(newBlocks);
    }

    private LinkedList<BasicBlock> processInsts(BasicBlock block) {
        LinkedList<BasicBlock> newBlocks = new LinkedList<>();
        newBlocks.add(block);
        Function function = block.parentFunction();
        for (Instruction inst = block.front(); inst != null; inst = inst.next) {
            if (inst instanceof FunctionCallInst) {
                FunctionCallInst functionCallInst = (FunctionCallInst) inst;
                Operand arg = functionCallInst.args().get(0);
                if (arg instanceof IntLiteral && ((IntLiteral) arg).val() > options.MEMORIZE_SEARCH_LEVEL) continue;

                BasicBlock getRes = new BasicBlock(function, block.getCurrentSymbolTable(), function.name() + "_memorize_get_res", block.valTag());
                BasicBlock callSetBlock = new BasicBlock(function, block.getCurrentSymbolTable(), function.name() + "_memorize_call_set", block.valTag());
                BasicBlock setRes = new BasicBlock(function, block.getCurrentSymbolTable(), function.name() + "_memorize_set_res", block.valTag());
                BasicBlock callBlock = new BasicBlock(function, block.getCurrentSymbolTable(), function.name() + "_memorize_call", block.valTag());
                BasicBlock nextBlock = new BasicBlock(function, block.getCurrentSymbolTable(), function.name() + "_memorize_next", block.valTag());
                newBlocks.addAll(Arrays.asList(callBlock, getRes, callSetBlock, setRes));
                nextBlock.setFront(inst.next);
                nextBlock.setEnd(block.back());
                block.setEnd(inst.prev);
                inst.prev.next = null;

                // Index validation test
                Var cmpRes = Var.tmpBuilder("memorize_cmp", true);
                BinaryExprInst cmp = new BinaryExprInst(cmpRes, arg, ExprOps.LT, new IntLiteral(options.MEMORIZE_SEARCH_LEVEL));
                block.pushBack(new CondJumpInst(cmpRes, cmp, getRes, callBlock));

                // Result fetch
                Var res = Var.tmpBuilder("memorize_res");
                getRes.pushBack(new MoveInst(res, new Memory(function.memorizeSearchMemBase(), arg, 1, 0)));
                Var validCmpRes = Var.tmpBuilder("memorize_valid", true);
                BinaryExprInst validCmp = new BinaryExprInst(validCmpRes, res, ExprOps.NEQ, IntLiteral.ZERO_LITERAL);
                getRes.pushBack(new CondJumpInst(validCmpRes, validCmp, setRes, callSetBlock)); // Result validation test

                // call and set
                callSetBlock.pushBack(new FunctionCallInst(function, functionCallInst.args(), (Register) functionCallInst.dst()));
                callBlock.pushBack(new MoveInst(new Memory(function.memorizeSearchMemBase(), arg, 1, 0), functionCallInst.dst()));
                callSetBlock.pushBack(new DirectJumpInst(nextBlock));

                // Result set
                setRes.pushBack(new MoveInst(functionCallInst.dst(), res));
                setRes.pushBack(new DirectJumpInst(nextBlock));

                // Normal call
                callBlock.pushBack(functionCallInst);
                callBlock.pushBack(new DirectJumpInst(nextBlock));

                newBlocks.addAll(processInsts(nextBlock));
            }
        }
        return newBlocks;
    }

    private void identifyMemorizeSearch(Function function) {
        if (function.notUserFunc()) return;
        boolean needMemorize = false;
        if (function.paramVars().size() != 1 || !function.needReturn()) return;
        for (BasicBlock block : function.basicBlocks()) {
            for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                if (inst instanceof FunctionCallInst) {
                    Function callee = ((FunctionCallInst) inst).callee();
                    if (callee == function) needMemorize = true;
                    else {
                        needMemorize = false;
                        break;
                    }
                }
                for (Var var : inst.usedVar()) {
                    if (var.isGlobal()) {
                        needMemorize = false;
                        break;
                    }
                }
                for (Var var : inst.dstVar()) {
                    if (var.isGlobal()) {
                        needMemorize = false;
                        break;
                    }
                }
            }
        }
        function.setMemorizeable(needMemorize);
        if (needMemorize) {
            Var memBase = Var.tmpBuilder(function.name() + "_mem_search_base");
            memBase.setGlobal();
            programIR.addGlobal(memBase, null);
            function.setMemorizeSearchMemBase(memBase);
        }
    }


}
