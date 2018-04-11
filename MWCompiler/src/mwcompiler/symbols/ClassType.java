package mwcompiler.symbols;

public class ClassType extends Type {
    private String typeName;
    public ClassType(String s) {
        this.typeName = s;
    }

    @Override
    public String getName() {
        return this.typeName;
    }
}
