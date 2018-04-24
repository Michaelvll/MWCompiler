package mwcompiler.utility;

import mwcompiler.ast.tools.Location;

import java.util.ArrayList;
import java.util.List;

public class CompileWarining {
    private static List<String> messages = new ArrayList<>();

    public static void add(String stage, String msg, Location location, String expr) {
        String message = Colors.RED + "WARNING: " + Colors.BLACK + "(" + Colors.GREEN + stage + Colors.BLACK + ") " + msg
                + " " + Colors.BLUE + (location != null ? location.getLocation() : "") + "\n\t"
                + Colors.BLACK + expr;
        messages.add(message);
    }

    public static void printWarings() {
        for (String warning : messages) {
            System.err.println(warning);
        }
    }
}
