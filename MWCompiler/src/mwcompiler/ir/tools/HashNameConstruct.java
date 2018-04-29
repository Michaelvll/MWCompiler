package mwcompiler.ir.tools;

public class HashNameConstruct {
    public static String getHashName(String name) {
        return (new HashNameConstruct()).hashCode() + name;
    }
}
