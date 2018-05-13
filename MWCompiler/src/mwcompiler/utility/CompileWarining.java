package mwcompiler.utility;

import java.util.ArrayList;
import java.util.List;

public class CompileWarining {
    private static List<String> messages = new ArrayList<>();

    public static void add(String stage, String msg, Location location) {
        String message = StringProcess.getErrWarning("Warning", stage, msg, location);
        messages.add(message);
    }

    public static void printWarings() {
        messages.forEach(System.err::println);
    }
}
