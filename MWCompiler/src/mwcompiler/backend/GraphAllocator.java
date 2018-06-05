package mwcompiler.backend;

import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.Function;
import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.nodes.assign.AssignInst;
import mwcompiler.ir.nodes.assign.BinaryExprInst;
import mwcompiler.ir.nodes.assign.FunctionCallInst;
import mwcompiler.ir.nodes.assign.MoveInst;
import mwcompiler.ir.operands.*;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.ExprOps;

import java.util.*;

import static mwcompiler.ir.operands.PhysicalRegister.*;

public class GraphAllocator extends Allocator {
    private ProgramIR programIR;
    private CompilerOptions options;

    private List<PhysicalRegister> registers = new ArrayList<>(Arrays.asList(RBX, R12, R13, R14, R15));
    private List<PhysicalRegister> callerSave = new ArrayList<>(Arrays.asList(RDX, R8, R9, R10, R11));
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
                if (assignInst instanceof FunctionCallInst) {
                    for (PhysicalRegister preg : callerSave) {
                        assignInst.liveOut().forEach(var -> graph.addEdge(var, preg));
//                        if (assignInst.dst() != null) graph.addEdge((Register) assignInst.dst(), preg);
                    }
                    FunctionCallInst functionCallInst = (FunctionCallInst) assignInst;
                    setArgSuggestReg(functionCallInst.args());

                } else if (assignInst instanceof BinaryExprInst) {
                    ExprOps op = ((BinaryExprInst) assignInst).op();
                    if (op == ExprOps.MOD || op == ExprOps.DIV) {
                        assignInst.liveOut().forEach(var -> graph.addEdge(var, RDX));
                        assignInst.usedLocalVar().forEach(var -> graph.addEdge(var, RDX));
                    }
                }
                if (assignInst.dst() instanceof Memory || assignInst.dst() == null) continue;
                Var dst = (Var) assignInst.dst();
                if (dst.isGlobal()) continue;

                if (assignInst instanceof MoveInst) {
                    Operand val = ((MoveInst) assignInst).val();
                    assignInst.liveOut().forEach(var -> {
                        if (var != val) graph.addEdge(dst, var);
                    });
                    if (val instanceof Register) graph.addMovNeighbor(dst, (Register) val);
                } else assignInst.liveOut().forEach(var -> graph.addEdge(dst, var));

            }
        }
        graph.setDegree();
    }

    private void setArgSuggestReg(List<Operand> args) {
        if (args.size() >= 3)
            if (args.get(2) instanceof Register)
                graph.addMovNeighbor((Register) args.get(2), RDX);

        if (args.size() >= 5)
            if (args.get(4) instanceof Register)
                graph.addMovNeighbor((Register) args.get(4), R8);

        if (args.size() >= 6)
            if (args.get(5) instanceof Register)
                graph.addMovNeighbor((Register) args.get(5), R9);

    }

    private void assignRegister() {
        Set<PhysicalRegister> neighborReg = new HashSet<>();
        while (!graph.varStack.isEmpty()) {
            neighborReg.clear();
            Register reg = graph.varStack.pop();
            reg.deleted = false;
            for (Register neighbor : reg.neighbors())
                if (neighbor.isAssigned()) neighborReg.add(neighbor.physicalRegister());

            // try to assign same register for mov
            if (!reg.isAssigned())
                for (Register movNeighbor : reg.movNeighors()) {
                    if (movNeighbor.isAssigned() && !neighborReg.contains(movNeighbor.physicalRegister())) {
                        reg.setPhysicalRegister(movNeighbor.physicalRegister());
//                        System.err.println("Allocate suggested register for var: " + reg.irName() + " -> " + movNeighbor.physicalRegister().nasmName());
                        break;
                    }
                }

            // try to assign caller save registers
            if (!reg.isAssigned())
                for (PhysicalRegister preg : callerSave) {
                    if (!neighborReg.contains(preg)) {
                        reg.setPhysicalRegister(preg);
//                        System.err.println("set var: " + reg.irName() + " -> " + preg.nasmName());
                        break;
                    }
                }

            // try to assign callee save registers
            if (!reg.isAssigned())
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
                System.err.println("set var: " + reg.irName() + " -> stackTop: " + stackTop);
            }

        }
    }


    private class InterfereGraph {
        private Set<Var> varGraph = new HashSet<>();
        private Set<Var> deletionWorkList = new HashSet<>();
        private Stack<Register> varStack = new Stack<>();

        private void addEdge(Register a, Register b) {
            if (a == b) return;
            a.neighbors().add(b);
            b.neighbors().add(a);
//            System.err.println(a.irName() + "<->" + b.irName());
        }

        private void addMovNeighbor(Register a, Register b) {
            if (a == b) return;
            a.movNeighors().add(b);
            b.movNeighors().add(a);
        }

        private void setDegree() {
            for (Register reg : varGraph) {
                reg.setDegree();
                if (reg.degree < registers.size() && reg instanceof Var) deletionWorkList.add((Var) reg);
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
                    if (neighbor.degree < registers.size()) deletionWorkList.add((Var) neighbor);
                }
            });
            var.deleted = true;
            varStack.push(var);
            varGraph.remove(var);
        }

        private void simplify() {
            while (!varGraph.isEmpty()) {
                while (!deletionWorkList.isEmpty()) {
                    Iterator<Var> it = deletionWorkList.iterator();
                    Var var = it.next();
                    removeVar(var);
                    deletionWorkList.remove(var);
                }
                Iterator<Var> it = varGraph.iterator();
                if (it.hasNext()) removeVar(it.next());
                else break;
            }
        }

    }


}
