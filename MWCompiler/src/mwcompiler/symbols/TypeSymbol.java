package mwcompiler.symbols;

import java.util.Dictionary;
import java.util.Hashtable;

public class TypeSymbol extends Symbol {

    private static Dictionary<String, TypeSymbol> typeMap = new Hashtable<>();
    static {
        typeMap.put("int", new TypeSymbol("int"));
        typeMap.put("string", new TypeSymbol("string"));
        typeMap.put("bool", new TypeSymbol("bool"));
        typeMap.put("void", new TypeSymbol("void"));
    }
    private String typename;

    private TypeSymbol(String typename) {
        this.typename = typename;
    }


    public String getName() {
        return this.typename;
    }

    public static TypeSymbol builder(String typename) {
        if (typename == null || typename.equals("")){
            throw new RuntimeException("Get empty or null typename when building TypeSymbol");
        }
        String intern = typename.intern();
        TypeSymbol search = typeMap.get(intern);
        if (search == null) {
            search = new TypeSymbol(typename);
            typeMap.put(typename.intern(), search);
        }
        return search;
    }

    public static TypeSymbol builder(TypeSymbol type) {
        return builder(type.getName());
    }
}
