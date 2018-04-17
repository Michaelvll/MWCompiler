// Generated from Mx.g4 by ANTLR 4.7
package mx_gram.tools;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MxParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarator(MxParser.DeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableDeclField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclField(MxParser.VariableDeclFieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeFunction_}
	 * labeled alternative in {@link MxParser#functionDeclField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFunction_(MxParser.TypeFunction_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code VoidFunction_}
	 * labeled alternative in {@link MxParser#functionDeclField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidFunction_(MxParser.VoidFunction_Context ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classDeclField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclField(MxParser.ClassDeclFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(MxParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#nonArrayType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonArrayType(MxParser.NonArrayTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(MxParser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassType(MxParser.ClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableField(MxParser.VariableFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#functionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionField(MxParser.FunctionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(MxParser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#paramExprField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamExprField(MxParser.ParamExprFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#paramExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamExpr(MxParser.ParamExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(MxParser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(MxParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MxParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(MxParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#conditionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionField(MxParser.ConditionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#elseifConditionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseifConditionField(MxParser.ElseifConditionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#elseConditionField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseConditionField(MxParser.ElseConditionFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#loopField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopField(MxParser.LoopFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#jumpField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpField(MxParser.JumpFieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ReturnJump_}
	 * labeled alternative in {@link MxParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnJump_(MxParser.ReturnJump_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code BreakJump_}
	 * labeled alternative in {@link MxParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakJump_(MxParser.BreakJump_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code ContinueJump_}
	 * labeled alternative in {@link MxParser#jump}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinueJump_(MxParser.ContinueJump_Context ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#forField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForField(MxParser.ForFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#whileField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileField(MxParser.WhileFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#exprField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprField(MxParser.ExprFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassField(MxParser.ClassFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(MxParser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#classConstructField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassConstructField(MxParser.ClassConstructFieldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewCreator_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewCreator_(MxParser.NewCreator_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code Identifier_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier_(MxParser.Identifier_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code This_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThis_(MxParser.This_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code DotMember_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotMember_(MxParser.DotMember_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr_(MxParser.ParenExpr_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code SuffixIncDec_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuffixIncDec_(MxParser.SuffixIncDec_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code BrackMember_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBrackMember_(MxParser.BrackMember_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpr_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr_(MxParser.UnaryExpr_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExpr_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr_(MxParser.BinaryExpr_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionCall_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall_(MxParser.FunctionCall_Context ctx);
	/**
	 * Visit a parse tree produced by the {@code Literal_}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_(MxParser.Literal_Context ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(MxParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(MxParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(MxParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(MxParser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#createdName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedName(MxParser.CreatedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorRest(MxParser.ArrayCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#creatorInner}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatorInner(MxParser.CreatorInnerContext ctx);
}