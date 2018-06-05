package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.*;
import mwcompiler.symbols.FunctionSymbol;

import java.util.*;

public class ProgramIR {
    private Map<FunctionSymbol, Function> functionMap = new HashMap<>();
    private Map<String, StringLiteral> stringPool = new HashMap<String, StringLiteral>() {{
        put("%ld", StringLiteral.intFormat);
        put("%ld\n", StringLiteral.intlnFormat);
        put("%s", StringLiteral.stringFormat);
        put("%s\n", StringLiteral.stringlnFormat);
    }};
    private Map<Var, IntLiteral> globalPool = new HashMap<>();

    public void addGlobal(Var var, Operand init) {
        if (init instanceof IntLiteral) globalPool.put(var, (IntLiteral) init);
        else globalPool.put(var, null);
    }



    public Map<Var, IntLiteral> getGlobalPool() {
        return globalPool;
    }

    public void putFunction(Function function) {
        functionMap.put(function.getFunctionSymbol(), function);
    }

    public Function getFunction(FunctionSymbol functionSymbol) {
        return functionMap.get(functionSymbol);
    }

    public void putStringLiteral(String val, StringLiteral stringLiteral) {
        stringPool.put(val, stringLiteral);
    }

    public StringLiteral getStringLiteral(String val) {
        return stringPool.get(val);
    }

    public Map<FunctionSymbol, Function> functionMap() {
        return functionMap;
    }

    public Map<String, StringLiteral> getStringPool() {
        return stringPool;
    }

//    public void updateRecursiveCall() {
//        functionMap.values().forEach(callee->callee.recursiveCalleeSet().clear());
//        Set<Function> recursiveCalleeSet = new HashSet<>();
//        boolean change = true;
//        while (change) {
//            change = false;
//            for (Function callee : functionMap().values()) {
//                if (callee.notUserFunc() && callee.isMain()) continue;
//                Set<Function> calleeSet = callee.calleeSet();
//                recursiveCalleeSet.clear();
//                recursiveCalleeSet.addAll(calleeSet);
//                calleeSet.forEach(callee -> recursiveCalleeSet.addAll(callee.calleeSet()));
//                if (!recursiveCalleeSet.equals(callee.recursiveCalleeSet())) {
//                    callee.recursiveCalleeSet().clear();
//                    callee.recursiveCalleeSet().addAll(recursiveCalleeSet);
//                    change = true;
//                }
//            }
//        }
//    }
}
