package mwcompiler.symbols;

public abstract class Type {
    public abstract String getName();
    public static Type builder(String type) {
        switch (type) {
            case "int" :
                return new IntType();
            case "string":
                return new StringType();
            case "bool":
                return new BoolType();
            default:
                return new ClassType(type);
        }
    }
    public static Type builder(Type type) {
        return builder(type.getName());
    }
}
