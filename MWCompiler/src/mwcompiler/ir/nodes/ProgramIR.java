package mwcompiler.ir.nodes;

import mwcompiler.symbols.InstanceSymbol;

import java.util.HashMap;
import java.util.Map;

public class ProgramIR {
    private Map<InstanceSymbol, Function> functionMap = new HashMap<>();

    public void putFunction(InstanceSymbol functionSymbol,Function function) {
        functionMap.put(functionSymbol, function);
    }

    public Function getFunction(InstanceSymbol functionSymbol) {
        return functionMap.get(functionSymbol);
    }

    public Map<InstanceSymbol, Function> getFunctionMap() {
        return functionMap;
    }
}
