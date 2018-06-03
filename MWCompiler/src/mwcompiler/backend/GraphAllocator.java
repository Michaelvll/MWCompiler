package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.operands.Memory;
import mwcompiler.ir.operands.PhysicalRegister;
import mwcompiler.ir.operands.Register;
import mwcompiler.ir.operands.Var;
import mwcompiler.utility.CompilerOptions;

import java.util.*;

import static mwcompiler.ir.operands.PhysicalRegister.*;

public class GraphAllocator extends Allocator {
    private ProgramIR programIR;
    private CompilerOptions options;

    private List<PhysicalRegister> registers = new ArrayList<>(Arrays.asList(RBX, R12, R13, R14, R15));
    private Set<PhysicalRegister> usedCalleeRegs = new HashSet<>();
    private InterfereGraph graph = new InterfereGraph();
    private int stackTop;

    public GraphAllocator(CompilerOptions options) {
        this.options = options;
    }

    public void apply(ProgramIR programIR) {
        this.programIR = programIR;

        for (Function function : programIR.functionMap().values()) {
            if (function.notUserFunc()) continue;
            usedCalleeRegs.clear();
            stackTop = 0;
            graph.init(function);
            buildGraph(function);
            graph.simplify();
            assignRegister();

            allocateParamStack(function);
            function.usedCalleeRegs().addAll(usedCalleeRegs);
            function.usedCalleeRegs().add(RBP);
            function.setVarStackSize(stackTop);
        }
    }

    private void allocateParamStack(Function function) {
        int paramStackTop = usedCalleeRegs.size() * options.PTR_SIZE + options.PTR_SIZE; // callee-save + return address
        List<Var> params = function.paramVars();
        for (int index = PhysicalRegister.paramRegs.size(); index < params.size(); ++index) {
            params.get(index).setStackPos(new Memory(RBP, null, 0, paramStackTop));
            paramStackTop += options.PTR_SIZE;
        }
    }

    private void buildGraph(Function function) {
        for (Var var : function.paramVars()) {
            for (Var var1 : function.paramVars()) {
                graph.addEdge(var, var1);
            }
        }

        for (BasicBlock block : function.basicBlocks()) {
            for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                if (!(inst instanceof AssignInst)) continue;
                AssignInst assignInst = (AssignInst) inst;
                if (assignInst.dst() instanceof Memory || assignInst.dst() == null) continue;
                Var dst = (Var) assignInst.dst();
                if (dst.isGlobal()) continue;
                if (assignInst instanceof MoveInst) {
                    assignInst.liveOut().forEach(var -> {
                        if (var != ((MoveInst) assignInst).val()) graph.addEdge(dst, var);
                    });
                } else assignInst.liveOut().forEach(var -> graph.addEdge(dst, var));

            }
        }
        graph.setDegree();
    }

    private void assignRegister() {
        Set<PhysicalRegister> neighborReg = new HashSet<>();
        while (!graph.varStack.isEmpty()) {
            neighborReg.clear();
            Register reg = graph.varStack.pop();
            reg.deleted = false;
            for (Register neighbor : reg.neighbors())
                if (!neighbor.deleted && neighbor.isAssigned()) neighborReg.add(neighbor.physicalRegister());
            for (PhysicalRegister preg : registers) {
                if (!neighborReg.contains(preg)) {
                    reg.setPhysicalRegister(preg);
                    usedCalleeRegs.add(preg);
//                    System.err.println("set var: " + var.irName() + " -> " + preg.nasmName());
                    break;
                }
            }
            if (!reg.isAssigned()) {
                stackTop = alignStack(stackTop + options.PTR_SIZE, options.PTR_SIZE);
                ((Var) reg).setStackPos(new Memory(RBP, null, 0, -stackTop));
            }

        }
    }


    private class InterfereGraph {
        private Set<Var> varGraph = new HashSet<>();
        private Set<Var> toDeleteVars = new HashSet<>();
        private Stack<Register> varStack = new Stack<>();

        private void addEdge(Register a, Register b) {
            if (a == b) return;
            a.neighbors().add(b);
            b.neighbors().add(a);
//            System.err.println(a.irName() + "<->" + b.irName());
        }

        private void setDegree() {
            for (Register reg : varGraph) {
                reg.setDegree();
                if (reg.degree < registers.size() && reg instanceof Var) toDeleteVars.add((Var) reg);
            }
        }

        private void init(Function function) {
            varGraph.clear();
            varStack.clear();
            for (BasicBlock block : function.basicBlocks()) {
                for (Instruction inst = block.front(); inst != null; inst = inst.next) {
                    varGraph.addAll(inst.usedLocalVar());
                    varGraph.addAll(inst.dstLocalVar());
                }
            }
        }

        private void removeVar(Var var) {
            var.neighbors().forEach(neighbor -> {
                if (!neighbor.deleted && neighbor instanceof Var) {
                    --neighbor.degree;
                    if (neighbor.degree < registers.size()) toDeleteVars.add((Var) neighbor);
                }
            });
            var.deleted = true;
            varStack.push(var);
            varGraph.remove(var);
        }

        private void simplify() {
            while (!varGraph.isEmpty()) {
                while (!toDeleteVars.isEmpty()) {
                    Iterator<Var> it = toDeleteVars.iterator();
                    Var var = it.next();
                    removeVar(var);
                    toDeleteVars.remove(var);
                }
                Iterator<Var> it = varGraph.iterator();
                if (it.hasNext()) removeVar(it.next());
                else break;
            }
        }

    }


}
