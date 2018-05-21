package mwcompiler.ir.nodes;

import mwcompiler.ir.operands.StringLiteral;
import mwcompiler.symbols.FunctionSymbol;

import java.util.*;

public class ProgramIR {
    private Map<FunctionSymbol, Function> functionMap = new HashMap<>();
    public Map<String, StringLiteral> stringPool = new HashMap<>();

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

    public Map<FunctionSymbol, Function> getFunctionMap() {
        return functionMap;
    }

    public Map<String, StringLiteral> getStringPool() {
        return stringPool;
    }
}
