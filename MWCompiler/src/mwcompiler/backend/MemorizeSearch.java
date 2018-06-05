package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.operands.Var;
import mwcompiler.symbols.Instance;
import mwcompiler.utility.CompilerOptions;

public class MemorizeSearch {
    private ProgramIR programIR;
    private CompilerOptions options;

    public MemorizeSearch(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;

        for (Function function : programIR.functionMap().values()) {
            identifyMemorizeSearch(function);
            if (function.memorizeable()) {

            }
        }

    }

    private void identifyMemorizeSearch(Function function) {
        if (function.notUserFunc()) return;
        boolean memorizeable = false;
        for (BasicBlock block : function.basicBlocks()) {
            for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                if (inst instanceof FunctionCallInst) {
                    Function callee = ((FunctionCallInst) inst).function();
                    if (callee == function) memorizeable = true;
                    else {
                        memorizeable = false;
                        break;
                    }
                }
                for (Var var : inst.usedVar()) {
                    if (var.isGlobal()) {
                        memorizeable = false;
                        break;
                    }
                }
                for (Var var : inst.dstVar()) {
                    if (var.isGlobal()) {
                        memorizeable = false;
                        break;
                    }
                }
            }
        }
        function.setMemorizeable(memorizeable);
        if (memorizeable) {
            Var memBase = Var.tmpBuilder(function.name() + "_mem_search_base");
            memBase.setGlobal();
            programIR.addGlobal(memBase, null);
            function.setMemorizeSearchMemBase(memBase);
        }
    }


}
