package mwcompiler.symbols;

public abstract class InstanceSymbol extends Symbol {
    public static InstanceSymbol getSymbol(String name){
        String intern = name.intern();
        Symbol search = symbolMap.get(intern);
        if (search == null) {
            throw new RuntimeException("Can not resolve "+name);
        }
        if (!(search instanceof InstanceSymbol)){
            throw new RuntimeException("Class name can not be used as instance name "+ name);
        }
        return (InstanceSymbol) search;
    }
}
