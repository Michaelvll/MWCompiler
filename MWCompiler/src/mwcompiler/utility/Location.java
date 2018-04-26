
package mwcompiler.utility;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Location.java
 * The class indicate the location of the token in the source code
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class Location {
    private final int line;
    private final int col;
    private final Interval interval;

    Location(int line, int col) {
        this.line = line;
        this.col = col;
        interval = null;
    }

    public Location(int line, int col, Interval interval) {
        this.line = line;
        this.col = col;
        this.interval = interval;
    }

    public Location(Token token) {
        this.line = token.getLine();
        this.col = token.getCharPositionInLine();
        this.interval = new Interval(token.getTokenIndex(), token.getTokenIndex());
    }

    public Location(ParserRuleContext ctx) {
        this.line = ctx.start.getLine();
        this.col = ctx.start.getCharPositionInLine();
        this.interval = ctx.getSourceInterval();
    }

    public Location(TerminalNode terminalNode) {
        this(terminalNode.getSymbol());
    }

    public String toString() {
        return "(line: " + String.valueOf(line) + ", col: " + String.valueOf(col) + ")";
    }

    public Interval getInterval() {
        return interval;
    }
}
