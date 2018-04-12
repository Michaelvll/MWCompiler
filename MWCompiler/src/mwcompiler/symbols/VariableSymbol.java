package mwcompiler.symbols;

import java.util.Dictionary;
import java.util.Hashtable;

public class VariableSymbol extends Symbol {
    private static Dictionary<String, VariableSymbol> variableDict = new Hashtable<>();
    private String variableName;

    private VariableSymbol(String variableName) {
        this.variableName = variableName;
    }

    public static VariableSymbol builder(String variableName) {
        String intern = variableName.intern();
        VariableSymbol search = variableDict.get(intern);
        if (search == null) {
            search = new VariableSymbol(variableName);
            variableDict.put(intern, search);
        }
        return search;
    }

    public static VariableSymbol builder(VariableSymbol var){
        return VariableSymbol.builder(var.getName());
    }

    @Override
    public String getName() {
        return variableName;
    }
}
