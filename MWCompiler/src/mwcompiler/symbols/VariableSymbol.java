package mwcompiler.symbols;

public class VariableSymbol extends InstanceSymbol {
    static {
        symbolMap.put("this", new VariableSymbol("this"));
    }

    private String variableName;

    private VariableSymbol(String variableName) {
        this.variableName = variableName;
    }

    public  static VariableSymbol getSymbol(String variableName) {
        String intern = variableName.intern();
        Symbol search = symbolMap.get(intern);
        if (search == null) {
            search = new VariableSymbol(intern);
            symbolMap.put(intern, search);
        }
        return (VariableSymbol) search;
    }

    @Override
    public String getName() {
        return variableName;
    }
}
