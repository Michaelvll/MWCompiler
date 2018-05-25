package mwcompiler.utility;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;

public class StringProcess {
    static CommonTokenStream tokens;

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";


    public static String getRefString(String name) {
        return "<" + RED + name + BLACK + "> ";
    }


    private static String getBefore(Location location) {
        int start = location.getInterval().a;
        if (start == 0 || tokens.get(start - 1).getText().contains("\n")) return "";
        int lineStart = start - 1;

        while (lineStart >= 0 && !tokens.get(lineStart).getText().contains("\n")) {
            --lineStart;
        }
        return BLACK + tokens.getText(new Interval(lineStart + 1, start - 1));
    }

    private static String getMiddle(Location location) {
        return RED + tokens.getText(location.getInterval());
    }

    private static String getAfter(Location location) {
        int end = location.getInterval().b;
        if (end == tokens.size() || tokens.get(end + 1).getText().contains("\n")) return "";
        int lineEnd = end + 1;
        while (lineEnd < tokens.size() && !tokens.get(lineEnd).getText().contains("\n")) {
            ++lineEnd;
        }
        return BLACK + tokens.getText(new Interval(end + 1, lineEnd));
    }

    static String getErrWarning(String errType, String stage, String msg, Location location) {
        if (location == null) {
            return RED + errType + ": " + BLACK + "(" + GREEN + stage + BLACK + ") " + msg;
        }
        return RED + errType + ": " + BLACK + "(" + GREEN + stage + BLACK + ") " + msg + " " + BLUE
                + location.toString() + "\n\t" + getBefore(location) + getMiddle(location) + getAfter(location);
    }

    public static String getErrWarning(String errType, String stage, String msg, int line, int charPosInline) {
        return RED + errType + ": " + BLACK + "(" + GREEN + stage + BLACK + ") " + msg + " " + BLUE
                + (new Location(line, charPosInline)).toString();
    }

    public static void setTokens(CommonTokenStream tokens) {
        StringProcess.tokens = tokens;
    }

}
