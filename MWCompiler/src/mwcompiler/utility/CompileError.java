package mwcompiler.utility;

import mwcompiler.ast.tools.Location;

public class CompileError extends RuntimeException {
    private static class Colors {
        public static final String RESET = "\u001B[0m";
        public static final String BLACK = "\u001B[30m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";
    }

    private String message;

    public CompileError(String stage, String msg, Location location, String expr) {
        this.message = Colors.RED + "ERROR: " + Colors.BLACK + "(" + Colors.GREEN + stage + Colors.BLACK + ") " + msg
                + " " + Colors.BLUE + (location != null ? location.getLocation() : "")
                + Colors.BLACK + (expr != null ? "\n\t" + expr : "");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
