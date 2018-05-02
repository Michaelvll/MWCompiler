package mwcompiler.utility;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ErrorSet {
    static List<CompileError> errorList = new ArrayList<>();

    public static void add(CompileError error) {
        errorList.add(error);
    }

    public static void print(PrintStream out) {
        errorList.forEach(x -> out.println(x.getMessage()));
    }

    public static Boolean empty() {
        return errorList.isEmpty();
    }
}
