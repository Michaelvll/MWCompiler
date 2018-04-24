package mwcompiler.utility;

import mwcompiler.ast.tools.Location;

public class CompileError extends RuntimeException {
    private String message;

    public CompileError(String stage, String msg, Location location, String expr) {
        this.message = Colors.RED + "ERROR: " + Colors.BLACK + "(" + Colors.GREEN + stage + Colors.BLACK + ") " + msg
                + " " + Colors.BLUE + (location != null ? location.getLocation() : "") + "\n\t"
                + Colors.BLACK + expr;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
