package mwcompiler.symbols;

public class IdentifierType extends Type {
    private String typeName;
    public IdentifierType(String s) {
        this.typeName = s;
    }

    @Override
    public String getName() {
        return this.typeName;
    }
}
