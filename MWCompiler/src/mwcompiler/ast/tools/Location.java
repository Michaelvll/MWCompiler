
package mwcompiler.ast.tools;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
/**
 * Location.java
 * The class indicate the location of the token in the source code
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class Location {
    public final int line;
    public final int col;

    public  Location(int line, int col) {
        this.line = line;
        this.col = col;
    }

    public Location(Token token) {
        this.line = token.getLine();
        this.col = token.getCharPositionInLine();
    }

    public Location(ParserRuleContext ctx) {
        this(ctx.start);
    }

    public  Location(TerminalNode terminalNode){
        this(terminalNode.getSymbol());
    }

    public String getLocation(){
        return "(line: " + String.valueOf(line) + ", col: " + String.valueOf(col) +")";
    }
}
