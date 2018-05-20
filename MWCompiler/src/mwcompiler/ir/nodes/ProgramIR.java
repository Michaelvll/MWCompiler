package mwcompiler.ir.nodes;

import mwcompiler.symbols.FunctionSymbol;

import java.util.HashMap;
import java.util.Map;

public class ProgramIR {
    private Map<FunctionSymbol, Function> functionMap = new HashMap<>();

    public void putFunction(Function function) {
        functionMap.put(function.getFunctionSymbol(), function);
    }

    public Function getFunction(FunctionSymbol functionSymbol) {
        return functionMap.get(functionSymbol);
    }

    public Map<FunctionSymbol, Function> getFunctionMap() {
        return functionMap;
    }
}
