package mwcompiler.symbols;

public abstract class Symbol {
    public abstract String getName();

    public abstract void checkLegal();

    public abstract SymbolInfo findIn(Instance instance);

}
