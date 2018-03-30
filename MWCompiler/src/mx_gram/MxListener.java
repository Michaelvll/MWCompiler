// Generated from Mx.g4 by ANTLR 4.7

package MxGram;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(MxParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(MxParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(MxParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(MxParser.StatContext ctx);
}