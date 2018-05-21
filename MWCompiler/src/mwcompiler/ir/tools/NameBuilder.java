package mwcompiler.ir.tools;

import java.util.HashMap;
import java.util.Map;

public class NameBuilder {
    private static Map<String, Integer> sufTextMap = new HashMap<>();

    public static String builder(String preText) {
        if (preText == null)
            throw new RuntimeException("Compiler bug: (IR Building) Can not build a name with null pretext");
        sufTextMap.put(preText, sufTextMap.getOrDefault(preText, -1) + 1);
        return preText + sufTextMap.get(preText);
    }

    public static String builder(Object object) {
        if (object == null)
            throw new RuntimeException("Compiler bug: (IR Building) Can not build a name with null object");
        return object.toString();
    }

    public static String builder(String name, Object object) {
        if (name != null)
            return builder(name);
        if (object != null)
            return builder(object);
        throw new RuntimeException("Compiler bug: (IR Building) Can not build a name with null");
    }
}
