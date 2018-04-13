// Generated from Mx.g4 by ANTLR 4.7
package mx_gram.tools;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BOOL=1, INT=2, STRING=3, VOID=4, NULL=5, IF=6, ELSE=7, FOR=8, WHILE=9, 
		BREAK=10, CONTINUE=11, RETURN=12, NEW=13, CLASS=14, THIS=15, LPAREN=16, 
		RPAREN=17, LBRACE=18, RBRACE=19, LBRACK=20, RBRACK=21, SEMI=22, COMMA=23, 
		ADD=24, SUB=25, MUL=26, DIV=27, MOD=28, GT=29, LT=30, EQ=31, NEQ=32, LTE=33, 
		GTE=34, NOT=35, AND=36, OR=37, LSFT=38, RSFT=39, BITNOT=40, BITAND=41, 
		BITOR=42, BITXOR=43, ASSIGN=44, INC=45, DEC=46, DOT=47, BoolLiteral=48, 
		IntLiteral=49, StringLiteral=50, Identifier=51, LINE_COMMENT=52, MULTILINE_COMMENT=53, 
		WS=54;
	public static final int
		RULE_program = 0, RULE_declarator = 1, RULE_variableDeclField = 2, RULE_functionDeclField = 3, 
		RULE_classDeclField = 4, RULE_type = 5, RULE_nonArrayType = 6, RULE_primitiveType = 7, 
		RULE_classType = 8, RULE_variableField = 9, RULE_functionField = 10, RULE_variableInitializer = 11, 
		RULE_paramExprField = 12, RULE_paramExpr = 13, RULE_functionBody = 14, 
		RULE_block = 15, RULE_statement = 16, RULE_body = 17, RULE_conditionField = 18, 
		RULE_elseifConditionField = 19, RULE_elseConditionField = 20, RULE_loopField = 21, 
		RULE_jumpField = 22, RULE_jump = 23, RULE_forField = 24, RULE_whileField = 25, 
		RULE_exprField = 26, RULE_classField = 27, RULE_classBody = 28, RULE_classConstructField = 29, 
		RULE_expr = 30, RULE_literal = 31, RULE_arguments = 32, RULE_exprList = 33, 
		RULE_creator = 34, RULE_createdName = 35, RULE_arrayCreatorRest = 36;
	public static final String[] ruleNames = {
		"program", "declarator", "variableDeclField", "functionDeclField", "classDeclField", 
		"type", "nonArrayType", "primitiveType", "classType", "variableField", 
		"functionField", "variableInitializer", "paramExprField", "paramExpr", 
		"functionBody", "block", "statement", "body", "conditionField", "elseifConditionField", 
		"elseConditionField", "loopField", "jumpField", "jump", "forField", "whileField", 
		"exprField", "classField", "classBody", "classConstructField", "expr", 
		"literal", "arguments", "exprList", "creator", "createdName", "arrayCreatorRest"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'bool'", "'int'", "'string'", "'void'", "'null'", "'if'", "'else'", 
		"'for'", "'while'", "'break'", "'continue'", "'return'", "'new'", "'class'", 
		"'this'", "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", "','", "'+'", 
		"'-'", "'*'", "'/'", "'%'", "'>'", "'<'", "'=='", "'!='", "'<='", "'>='", 
		"'!'", "'&&'", "'||'", "'<<'", "'>>'", "'~'", "'&'", "'|'", "'^'", "'='", 
		"'++'", "'--'", "'.'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "BOOL", "INT", "STRING", "VOID", "NULL", "IF", "ELSE", "FOR", "WHILE", 
		"BREAK", "CONTINUE", "RETURN", "NEW", "CLASS", "THIS", "LPAREN", "RPAREN", 
		"LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", "ADD", "SUB", 
		"MUL", "DIV", "MOD", "GT", "LT", "EQ", "NEQ", "LTE", "GTE", "NOT", "AND", 
		"OR", "LSFT", "RSFT", "BITNOT", "BITAND", "BITOR", "BITXOR", "ASSIGN", 
		"INC", "DEC", "DOT", "BoolLiteral", "IntLiteral", "StringLiteral", "Identifier", 
		"LINE_COMMENT", "MULTILINE_COMMENT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Mx.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxParser.EOF, 0); }
		public List<DeclaratorContext> declarator() {
			return getRuleContexts(DeclaratorContext.class);
		}
		public DeclaratorContext declarator(int i) {
			return getRuleContext(DeclaratorContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << STRING) | (1L << VOID) | (1L << CLASS) | (1L << Identifier))) != 0)) {
				{
				{
				setState(74);
				declarator();
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(80);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaratorContext extends ParserRuleContext {
		public VariableDeclFieldContext variableDeclField() {
			return getRuleContext(VariableDeclFieldContext.class,0);
		}
		public FunctionDeclFieldContext functionDeclField() {
			return getRuleContext(FunctionDeclFieldContext.class,0);
		}
		public ClassDeclFieldContext classDeclField() {
			return getRuleContext(ClassDeclFieldContext.class,0);
		}
		public DeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaratorContext declarator() throws RecognitionException {
		DeclaratorContext _localctx = new DeclaratorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declarator);
		try {
			setState(85);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(82);
				variableDeclField();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(83);
				functionDeclField();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(84);
				classDeclField();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableDeclFieldContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public VariableFieldContext variableField() {
			return getRuleContext(VariableFieldContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public VariableDeclFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableDeclField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclFieldContext variableDeclField() throws RecognitionException {
		VariableDeclFieldContext _localctx = new VariableDeclFieldContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_variableDeclField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			type();
			setState(88);
			variableField();
			setState(89);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionDeclFieldContext extends ParserRuleContext {
		public FunctionDeclFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDeclField; }
	 
		public FunctionDeclFieldContext() { }
		public void copyFrom(FunctionDeclFieldContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class TypeFunction_Context extends FunctionDeclFieldContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionFieldContext functionField() {
			return getRuleContext(FunctionFieldContext.class,0);
		}
		public TypeFunction_Context(FunctionDeclFieldContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitTypeFunction_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VoidFunction_Context extends FunctionDeclFieldContext {
		public TerminalNode VOID() { return getToken(MxParser.VOID, 0); }
		public FunctionFieldContext functionField() {
			return getRuleContext(FunctionFieldContext.class,0);
		}
		public VoidFunction_Context(FunctionDeclFieldContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVoidFunction_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclFieldContext functionDeclField() throws RecognitionException {
		FunctionDeclFieldContext _localctx = new FunctionDeclFieldContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_functionDeclField);
		try {
			setState(96);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case INT:
			case STRING:
			case Identifier:
				_localctx = new TypeFunction_Context(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(91);
				type();
				setState(92);
				functionField();
				}
				break;
			case VOID:
				_localctx = new VoidFunction_Context(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				match(VOID);
				setState(95);
				functionField();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclFieldContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(MxParser.CLASS, 0); }
		public ClassFieldContext classField() {
			return getRuleContext(ClassFieldContext.class,0);
		}
		public ClassDeclFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassDeclField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclFieldContext classDeclField() throws RecognitionException {
		ClassDeclFieldContext _localctx = new ClassDeclFieldContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_classDeclField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(CLASS);
			setState(99);
			classField();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public NonArrayTypeContext nonArrayType() {
			return getRuleContext(NonArrayTypeContext.class,0);
		}
		public List<TerminalNode> LBRACK() { return getTokens(MxParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(MxParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(MxParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(MxParser.RBRACK, i);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			nonArrayType();
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LBRACK) {
				{
				{
				setState(102);
				match(LBRACK);
				setState(103);
				match(RBRACK);
				}
				}
				setState(108);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonArrayTypeContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public NonArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonArrayType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNonArrayType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonArrayTypeContext nonArrayType() throws RecognitionException {
		NonArrayTypeContext _localctx = new NonArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_nonArrayType);
		try {
			setState(111);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOL:
			case INT:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(109);
				primitiveType();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(110);
				classType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode BOOL() { return getToken(MxParser.BOOL, 0); }
		public TerminalNode INT() { return getToken(MxParser.INT, 0); }
		public TerminalNode STRING() { return getToken(MxParser.STRING, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << STRING))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassTypeContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ClassTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassTypeContext classType() throws RecognitionException {
		ClassTypeContext _localctx = new ClassTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_classType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableFieldContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode ASSIGN() { return getToken(MxParser.ASSIGN, 0); }
		public VariableInitializerContext variableInitializer() {
			return getRuleContext(VariableInitializerContext.class,0);
		}
		public VariableFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableFieldContext variableField() throws RecognitionException {
		VariableFieldContext _localctx = new VariableFieldContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_variableField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(Identifier);
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(118);
				match(ASSIGN);
				setState(119);
				variableInitializer();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionFieldContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParamExprFieldContext paramExprField() {
			return getRuleContext(ParamExprFieldContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public FunctionFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionFieldContext functionField() throws RecognitionException {
		FunctionFieldContext _localctx = new FunctionFieldContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_functionField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(Identifier);
			setState(123);
			paramExprField();
			setState(124);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableInitializerContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VariableInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableInitializer; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitVariableInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableInitializerContext variableInitializer() throws RecognitionException {
		VariableInitializerContext _localctx = new VariableInitializerContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_variableInitializer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamExprFieldContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public List<ParamExprContext> paramExpr() {
			return getRuleContexts(ParamExprContext.class);
		}
		public ParamExprContext paramExpr(int i) {
			return getRuleContext(ParamExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxParser.COMMA, i);
		}
		public ParamExprFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramExprField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParamExprField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamExprFieldContext paramExprField() throws RecognitionException {
		ParamExprFieldContext _localctx = new ParamExprFieldContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_paramExprField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(LPAREN);
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << STRING) | (1L << Identifier))) != 0)) {
				{
				setState(129);
				paramExpr();
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(130);
					match(COMMA);
					setState(131);
					paramExpr();
					}
					}
					setState(136);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(139);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParamExprContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParamExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParamExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamExprContext paramExpr() throws RecognitionException {
		ParamExprContext _localctx = new ParamExprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_paramExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			type();
			setState(142);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionBodyContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_functionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(MxParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxParser.RBRACE, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(LBRACE);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << STRING) | (1L << NULL) | (1L << IF) | (1L << FOR) | (1L << WHILE) | (1L << BREAK) | (1L << CONTINUE) | (1L << RETURN) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << LBRACE) | (1L << SEMI) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
				{
				{
				setState(147);
				statement();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(153);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public VariableDeclFieldContext variableDeclField() {
			return getRuleContext(VariableDeclFieldContext.class,0);
		}
		public ExprFieldContext exprField() {
			return getRuleContext(ExprFieldContext.class,0);
		}
		public ConditionFieldContext conditionField() {
			return getRuleContext(ConditionFieldContext.class,0);
		}
		public LoopFieldContext loopField() {
			return getRuleContext(LoopFieldContext.class,0);
		}
		public JumpFieldContext jumpField() {
			return getRuleContext(JumpFieldContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_statement);
		try {
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(155);
				block();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(156);
				variableDeclField();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(157);
				exprField();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(158);
				conditionField();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(159);
				loopField();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(160);
				jumpField();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BodyContext extends ParserRuleContext {
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_body);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			statement();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionFieldContext extends ParserRuleContext {
		public ExprContext cond;
		public TerminalNode IF() { return getToken(MxParser.IF, 0); }
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<ElseifConditionFieldContext> elseifConditionField() {
			return getRuleContexts(ElseifConditionFieldContext.class);
		}
		public ElseifConditionFieldContext elseifConditionField(int i) {
			return getRuleContext(ElseifConditionFieldContext.class,i);
		}
		public ElseConditionFieldContext elseConditionField() {
			return getRuleContext(ElseConditionFieldContext.class,0);
		}
		public ConditionFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitConditionField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionFieldContext conditionField() throws RecognitionException {
		ConditionFieldContext _localctx = new ConditionFieldContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_conditionField);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			match(IF);
			setState(166);
			match(LPAREN);
			setState(167);
			((ConditionFieldContext)_localctx).cond = expr(0);
			setState(168);
			match(RPAREN);
			setState(169);
			body();
			setState(173);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(170);
					elseifConditionField();
					}
					} 
				}
				setState(175);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(176);
				elseConditionField();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseifConditionFieldContext extends ParserRuleContext {
		public ExprContext cond;
		public TerminalNode ELSE() { return getToken(MxParser.ELSE, 0); }
		public TerminalNode IF() { return getToken(MxParser.IF, 0); }
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ElseifConditionFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseifConditionField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitElseifConditionField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseifConditionFieldContext elseifConditionField() throws RecognitionException {
		ElseifConditionFieldContext _localctx = new ElseifConditionFieldContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_elseifConditionField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(ELSE);
			setState(180);
			match(IF);
			setState(181);
			match(LPAREN);
			setState(182);
			((ElseifConditionFieldContext)_localctx).cond = expr(0);
			setState(183);
			match(RPAREN);
			setState(184);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ElseConditionFieldContext extends ParserRuleContext {
		public TerminalNode ELSE() { return getToken(MxParser.ELSE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ElseConditionFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseConditionField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitElseConditionField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseConditionFieldContext elseConditionField() throws RecognitionException {
		ElseConditionFieldContext _localctx = new ElseConditionFieldContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_elseConditionField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(ELSE);
			setState(187);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoopFieldContext extends ParserRuleContext {
		public ForFieldContext forField() {
			return getRuleContext(ForFieldContext.class,0);
		}
		public WhileFieldContext whileField() {
			return getRuleContext(WhileFieldContext.class,0);
		}
		public LoopFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_loopField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLoopField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LoopFieldContext loopField() throws RecognitionException {
		LoopFieldContext _localctx = new LoopFieldContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_loopField);
		try {
			setState(191);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(189);
				forField();
				}
				break;
			case WHILE:
				enterOuterAlt(_localctx, 2);
				{
				setState(190);
				whileField();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JumpFieldContext extends ParserRuleContext {
		public JumpContext jump() {
			return getRuleContext(JumpContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public JumpFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitJumpField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpFieldContext jumpField() throws RecognitionException {
		JumpFieldContext _localctx = new JumpFieldContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_jumpField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			jump();
			setState(194);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JumpContext extends ParserRuleContext {
		public JumpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jump; }
	 
		public JumpContext() { }
		public void copyFrom(JumpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ContinueJump_Context extends JumpContext {
		public TerminalNode CONTINUE() { return getToken(MxParser.CONTINUE, 0); }
		public ContinueJump_Context(JumpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitContinueJump_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BreakJump_Context extends JumpContext {
		public TerminalNode BREAK() { return getToken(MxParser.BREAK, 0); }
		public BreakJump_Context(JumpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBreakJump_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ReturnJump_Context extends JumpContext {
		public TerminalNode RETURN() { return getToken(MxParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnJump_Context(JumpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitReturnJump_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpContext jump() throws RecognitionException {
		JumpContext _localctx = new JumpContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_jump);
		int _la;
		try {
			setState(202);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RETURN:
				_localctx = new ReturnJump_Context(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(196);
				match(RETURN);
				setState(198);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
					{
					setState(197);
					expr(0);
					}
				}

				}
				break;
			case BREAK:
				_localctx = new BreakJump_Context(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(200);
				match(BREAK);
				}
				break;
			case CONTINUE:
				_localctx = new ContinueJump_Context(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(201);
				match(CONTINUE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ForFieldContext extends ParserRuleContext {
		public ExprContext vardecl;
		public ExprContext cond;
		public ExprContext step;
		public TerminalNode FOR() { return getToken(MxParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public List<TerminalNode> SEMI() { return getTokens(MxParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(MxParser.SEMI, i);
		}
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitForField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForFieldContext forField() throws RecognitionException {
		ForFieldContext _localctx = new ForFieldContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_forField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			match(FOR);
			setState(205);
			match(LPAREN);
			setState(207);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
				{
				setState(206);
				((ForFieldContext)_localctx).vardecl = expr(0);
				}
			}

			setState(209);
			match(SEMI);
			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
				{
				setState(210);
				((ForFieldContext)_localctx).cond = expr(0);
				}
			}

			setState(213);
			match(SEMI);
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
				{
				setState(214);
				((ForFieldContext)_localctx).step = expr(0);
				}
			}

			setState(217);
			match(RPAREN);
			setState(218);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WhileFieldContext extends ParserRuleContext {
		public ExprContext cond;
		public TerminalNode WHILE() { return getToken(MxParser.WHILE, 0); }
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public WhileFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitWhileField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileFieldContext whileField() throws RecognitionException {
		WhileFieldContext _localctx = new WhileFieldContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_whileField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(WHILE);
			setState(221);
			match(LPAREN);
			setState(222);
			((WhileFieldContext)_localctx).cond = expr(0);
			setState(223);
			match(RPAREN);
			setState(224);
			body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprFieldContext extends ParserRuleContext {
		public TerminalNode SEMI() { return getToken(MxParser.SEMI, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExprField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprFieldContext exprField() throws RecognitionException {
		ExprFieldContext _localctx = new ExprFieldContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_exprField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
				{
				setState(226);
				expr(0);
				}
			}

			setState(229);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassFieldContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode LBRACE() { return getToken(MxParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MxParser.RBRACE, 0); }
		public List<ClassBodyContext> classBody() {
			return getRuleContexts(ClassBodyContext.class);
		}
		public ClassBodyContext classBody(int i) {
			return getRuleContext(ClassBodyContext.class,i);
		}
		public ClassFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassFieldContext classField() throws RecognitionException {
		ClassFieldContext _localctx = new ClassFieldContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_classField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(Identifier);
			setState(232);
			match(LBRACE);
			setState(236);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << INT) | (1L << STRING) | (1L << VOID) | (1L << Identifier))) != 0)) {
				{
				{
				setState(233);
				classBody();
				}
				}
				setState(238);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(239);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassBodyContext extends ParserRuleContext {
		public VariableDeclFieldContext variableDeclField() {
			return getRuleContext(VariableDeclFieldContext.class,0);
		}
		public FunctionDeclFieldContext functionDeclField() {
			return getRuleContext(FunctionDeclFieldContext.class,0);
		}
		public ClassConstructFieldContext classConstructField() {
			return getRuleContext(ClassConstructFieldContext.class,0);
		}
		public ClassBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classBody; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassBodyContext classBody() throws RecognitionException {
		ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_classBody);
		try {
			setState(244);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(241);
				variableDeclField();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(242);
				functionDeclField();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(243);
				classConstructField();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassConstructFieldContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public ParamExprFieldContext paramExprField() {
			return getRuleContext(ParamExprFieldContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public ClassConstructFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classConstructField; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitClassConstructField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassConstructFieldContext classConstructField() throws RecognitionException {
		ClassConstructFieldContext _localctx = new ClassConstructFieldContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_classConstructField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(Identifier);
			setState(247);
			paramExprField();
			setState(248);
			functionBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NewCreator_Context extends ExprContext {
		public TerminalNode NEW() { return getToken(MxParser.NEW, 0); }
		public CreatorContext creator() {
			return getRuleContext(CreatorContext.class,0);
		}
		public NewCreator_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitNewCreator_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Identifier_Context extends ExprContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public Identifier_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitIdentifier_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class This_Context extends ExprContext {
		public TerminalNode THIS() { return getToken(MxParser.THIS, 0); }
		public This_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitThis_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DotMember_Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DOT() { return getToken(MxParser.DOT, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public DotMember_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitDotMember_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenExpr_Context extends ExprContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public ParenExpr_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitParenExpr_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SuffixIncDec_Context extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxParser.DEC, 0); }
		public SuffixIncDec_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitSuffixIncDec_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BrackMember_Context extends ExprContext {
		public ExprContext mom;
		public ExprContext subscript;
		public TerminalNode LBRACK() { return getToken(MxParser.LBRACK, 0); }
		public TerminalNode RBRACK() { return getToken(MxParser.RBRACK, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public BrackMember_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBrackMember_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryExpr_Context extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode INC() { return getToken(MxParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MxParser.DEC, 0); }
		public TerminalNode ADD() { return getToken(MxParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxParser.SUB, 0); }
		public TerminalNode NOT() { return getToken(MxParser.NOT, 0); }
		public TerminalNode BITNOT() { return getToken(MxParser.BITNOT, 0); }
		public UnaryExpr_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitUnaryExpr_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BinaryExpr_Context extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MUL() { return getToken(MxParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MxParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MxParser.MOD, 0); }
		public TerminalNode ADD() { return getToken(MxParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MxParser.SUB, 0); }
		public TerminalNode LSFT() { return getToken(MxParser.LSFT, 0); }
		public TerminalNode RSFT() { return getToken(MxParser.RSFT, 0); }
		public TerminalNode LT() { return getToken(MxParser.LT, 0); }
		public TerminalNode GT() { return getToken(MxParser.GT, 0); }
		public TerminalNode LTE() { return getToken(MxParser.LTE, 0); }
		public TerminalNode GTE() { return getToken(MxParser.GTE, 0); }
		public TerminalNode EQ() { return getToken(MxParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(MxParser.NEQ, 0); }
		public TerminalNode BITAND() { return getToken(MxParser.BITAND, 0); }
		public TerminalNode BITXOR() { return getToken(MxParser.BITXOR, 0); }
		public TerminalNode BITOR() { return getToken(MxParser.BITOR, 0); }
		public TerminalNode AND() { return getToken(MxParser.AND, 0); }
		public TerminalNode OR() { return getToken(MxParser.OR, 0); }
		public TerminalNode ASSIGN() { return getToken(MxParser.ASSIGN, 0); }
		public BinaryExpr_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitBinaryExpr_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionCall_Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public FunctionCall_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitFunctionCall_(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Literal_Context extends ExprContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public Literal_Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLiteral_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 60;
		enterRecursionRule(_localctx, 60, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INC:
			case DEC:
				{
				_localctx = new UnaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(251);
				((UnaryExpr_Context)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==INC || _la==DEC) ) {
					((UnaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(252);
				expr(20);
				}
				break;
			case ADD:
			case SUB:
				{
				_localctx = new UnaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(253);
				((UnaryExpr_Context)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
					((UnaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(254);
				expr(19);
				}
				break;
			case NOT:
				{
				_localctx = new UnaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(255);
				((UnaryExpr_Context)_localctx).op = match(NOT);
				setState(256);
				expr(18);
				}
				break;
			case BITNOT:
				{
				_localctx = new UnaryExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(257);
				((UnaryExpr_Context)_localctx).op = match(BITNOT);
				setState(258);
				expr(17);
				}
				break;
			case NEW:
				{
				_localctx = new NewCreator_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(259);
				match(NEW);
				setState(260);
				creator();
				}
				break;
			case NULL:
			case BoolLiteral:
			case IntLiteral:
			case StringLiteral:
				{
				_localctx = new Literal_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(261);
				literal();
				}
				break;
			case THIS:
				{
				_localctx = new This_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(262);
				match(THIS);
				}
				break;
			case Identifier:
				{
				_localctx = new Identifier_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(263);
				match(Identifier);
				}
				break;
			case LPAREN:
				{
				_localctx = new ParenExpr_Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(264);
				match(LPAREN);
				setState(265);
				expr(0);
				setState(266);
				match(RPAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(317);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(315);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(270);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(271);
						((BinaryExpr_Context)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((BinaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(272);
						((BinaryExpr_Context)_localctx).right = expr(16);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(273);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(274);
						((BinaryExpr_Context)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((BinaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(275);
						((BinaryExpr_Context)_localctx).right = expr(15);
						}
						break;
					case 3:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(276);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(277);
						((BinaryExpr_Context)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==LSFT || _la==RSFT) ) {
							((BinaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(278);
						((BinaryExpr_Context)_localctx).right = expr(14);
						}
						break;
					case 4:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(279);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(280);
						((BinaryExpr_Context)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << LT) | (1L << LTE) | (1L << GTE))) != 0)) ) {
							((BinaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(281);
						((BinaryExpr_Context)_localctx).right = expr(13);
						}
						break;
					case 5:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(282);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(283);
						((BinaryExpr_Context)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQ || _la==NEQ) ) {
							((BinaryExpr_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(284);
						((BinaryExpr_Context)_localctx).right = expr(12);
						}
						break;
					case 6:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(285);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(286);
						((BinaryExpr_Context)_localctx).op = match(BITAND);
						setState(287);
						((BinaryExpr_Context)_localctx).right = expr(11);
						}
						break;
					case 7:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(288);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(289);
						((BinaryExpr_Context)_localctx).op = match(BITXOR);
						setState(290);
						((BinaryExpr_Context)_localctx).right = expr(10);
						}
						break;
					case 8:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(291);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(292);
						((BinaryExpr_Context)_localctx).op = match(BITOR);
						setState(293);
						((BinaryExpr_Context)_localctx).right = expr(9);
						}
						break;
					case 9:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(294);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(295);
						((BinaryExpr_Context)_localctx).op = match(AND);
						setState(296);
						((BinaryExpr_Context)_localctx).right = expr(8);
						}
						break;
					case 10:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(297);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(298);
						((BinaryExpr_Context)_localctx).op = match(OR);
						setState(299);
						((BinaryExpr_Context)_localctx).right = expr(7);
						}
						break;
					case 11:
						{
						_localctx = new BinaryExpr_Context(new ExprContext(_parentctx, _parentState));
						((BinaryExpr_Context)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(300);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(301);
						((BinaryExpr_Context)_localctx).op = match(ASSIGN);
						setState(302);
						((BinaryExpr_Context)_localctx).right = expr(5);
						}
						break;
					case 12:
						{
						_localctx = new SuffixIncDec_Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(303);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(304);
						((SuffixIncDec_Context)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((SuffixIncDec_Context)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 13:
						{
						_localctx = new FunctionCall_Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(305);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(306);
						arguments();
						}
						break;
					case 14:
						{
						_localctx = new DotMember_Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(307);
						if (!(precpred(_ctx, 22))) throw new FailedPredicateException(this, "precpred(_ctx, 22)");
						setState(308);
						match(DOT);
						setState(309);
						match(Identifier);
						}
						break;
					case 15:
						{
						_localctx = new BrackMember_Context(new ExprContext(_parentctx, _parentState));
						((BrackMember_Context)_localctx).mom = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(310);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(311);
						match(LBRACK);
						setState(312);
						((BrackMember_Context)_localctx).subscript = expr(0);
						setState(313);
						match(RBRACK);
						}
						break;
					}
					} 
				}
				setState(319);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public Token literalType;
		public TerminalNode BoolLiteral() { return getToken(MxParser.BoolLiteral, 0); }
		public TerminalNode IntLiteral() { return getToken(MxParser.IntLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(MxParser.StringLiteral, 0); }
		public TerminalNode NULL() { return getToken(MxParser.NULL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_literal);
		try {
			setState(324);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BoolLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(320);
				((LiteralContext)_localctx).literalType = match(BoolLiteral);
				}
				break;
			case IntLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(321);
				((LiteralContext)_localctx).literalType = match(IntLiteral);
				}
				break;
			case StringLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(322);
				((LiteralContext)_localctx).literalType = match(StringLiteral);
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 4);
				{
				setState(323);
				((LiteralContext)_localctx).literalType = match(NULL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MxParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(MxParser.RPAREN, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			match(LPAREN);
			setState(328);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NULL) | (1L << NEW) | (1L << THIS) | (1L << LPAREN) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BITNOT) | (1L << INC) | (1L << DEC) | (1L << BoolLiteral) | (1L << IntLiteral) | (1L << StringLiteral) | (1L << Identifier))) != 0)) {
				{
				setState(327);
				exprList();
				}
			}

			setState(330);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MxParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MxParser.COMMA, i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			expr(0);
			setState(337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(333);
				match(COMMA);
				setState(334);
				expr(0);
				}
				}
				setState(339);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatorContext extends ParserRuleContext {
		public CreatedNameContext createdName() {
			return getRuleContext(CreatedNameContext.class,0);
		}
		public ArrayCreatorRestContext arrayCreatorRest() {
			return getRuleContext(ArrayCreatorRestContext.class,0);
		}
		public CreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_creator; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCreator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatorContext creator() throws RecognitionException {
		CreatorContext _localctx = new CreatorContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_creator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			createdName();
			setState(342);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(341);
				arrayCreatorRest();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CreatedNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public CreatedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createdName; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitCreatedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreatedNameContext createdName() throws RecognitionException {
		CreatedNameContext _localctx = new CreatedNameContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_createdName);
		try {
			setState(346);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(344);
				match(Identifier);
				}
				break;
			case BOOL:
			case INT:
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(345);
				primitiveType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayCreatorRestContext extends ParserRuleContext {
		public List<TerminalNode> LBRACK() { return getTokens(MxParser.LBRACK); }
		public TerminalNode LBRACK(int i) {
			return getToken(MxParser.LBRACK, i);
		}
		public List<TerminalNode> RBRACK() { return getTokens(MxParser.RBRACK); }
		public TerminalNode RBRACK(int i) {
			return getToken(MxParser.RBRACK, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ArrayCreatorRestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreatorRest; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MxVisitor ) return ((MxVisitor<? extends T>)visitor).visitArrayCreatorRest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreatorRestContext arrayCreatorRest() throws RecognitionException {
		ArrayCreatorRestContext _localctx = new ArrayCreatorRestContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_arrayCreatorRest);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			match(LBRACK);
			setState(368);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RBRACK:
				{
				setState(349);
				match(RBRACK);
				}
				break;
			case NULL:
			case NEW:
			case THIS:
			case LPAREN:
			case ADD:
			case SUB:
			case NOT:
			case BITNOT:
			case INC:
			case DEC:
			case BoolLiteral:
			case IntLiteral:
			case StringLiteral:
			case Identifier:
				{
				setState(350);
				expr(0);
				setState(351);
				match(RBRACK);
				setState(358);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(352);
						match(LBRACK);
						setState(353);
						expr(0);
						setState(354);
						match(RBRACK);
						}
						} 
					}
					setState(360);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,29,_ctx);
				}
				setState(365);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(361);
						match(LBRACK);
						setState(362);
						match(RBRACK);
						}
						} 
					}
					setState(367);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 30:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 15);
		case 1:
			return precpred(_ctx, 14);
		case 2:
			return precpred(_ctx, 13);
		case 3:
			return precpred(_ctx, 12);
		case 4:
			return precpred(_ctx, 11);
		case 5:
			return precpred(_ctx, 10);
		case 6:
			return precpred(_ctx, 9);
		case 7:
			return precpred(_ctx, 8);
		case 8:
			return precpred(_ctx, 7);
		case 9:
			return precpred(_ctx, 6);
		case 10:
			return precpred(_ctx, 5);
		case 11:
			return precpred(_ctx, 24);
		case 12:
			return precpred(_ctx, 23);
		case 13:
			return precpred(_ctx, 22);
		case 14:
			return precpred(_ctx, 21);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\38\u0175\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\3\2\7\2N\n\2\f\2\16\2Q\13\2\3\2\3"+
		"\2\3\3\3\3\3\3\5\3X\n\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\5\5c\n\5\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\7\7k\n\7\f\7\16\7n\13\7\3\b\3\b\5\br\n\b\3\t\3"+
		"\t\3\n\3\n\3\13\3\13\3\13\5\13{\n\13\3\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\7\16\u0087\n\16\f\16\16\16\u008a\13\16\5\16\u008c\n\16\3\16"+
		"\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\7\21\u0097\n\21\f\21\16\21\u009a"+
		"\13\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00a4\n\22\3\23\3"+
		"\23\3\24\3\24\3\24\3\24\3\24\3\24\7\24\u00ae\n\24\f\24\16\24\u00b1\13"+
		"\24\3\24\5\24\u00b4\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\27\3\27\5\27\u00c2\n\27\3\30\3\30\3\30\3\31\3\31\5\31\u00c9\n"+
		"\31\3\31\3\31\5\31\u00cd\n\31\3\32\3\32\3\32\5\32\u00d2\n\32\3\32\3\32"+
		"\5\32\u00d6\n\32\3\32\3\32\5\32\u00da\n\32\3\32\3\32\3\32\3\33\3\33\3"+
		"\33\3\33\3\33\3\33\3\34\5\34\u00e6\n\34\3\34\3\34\3\35\3\35\3\35\7\35"+
		"\u00ed\n\35\f\35\16\35\u00f0\13\35\3\35\3\35\3\36\3\36\3\36\5\36\u00f7"+
		"\n\36\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \5 \u010f\n \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3 \3 \3 \7 \u013e\n \f \16 \u0141\13 \3!\3!\3!\3!\5!\u0147"+
		"\n!\3\"\3\"\5\"\u014b\n\"\3\"\3\"\3#\3#\3#\7#\u0152\n#\f#\16#\u0155\13"+
		"#\3$\3$\5$\u0159\n$\3%\3%\5%\u015d\n%\3&\3&\3&\3&\3&\3&\3&\3&\7&\u0167"+
		"\n&\f&\16&\u016a\13&\3&\3&\7&\u016e\n&\f&\16&\u0171\13&\5&\u0173\n&\3"+
		"&\2\3>\'\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668"+
		":<>@BDFHJ\2\t\3\2\3\5\3\2/\60\3\2\32\33\3\2\34\36\3\2()\4\2\37 #$\3\2"+
		"!\"\2\u018c\2O\3\2\2\2\4W\3\2\2\2\6Y\3\2\2\2\bb\3\2\2\2\nd\3\2\2\2\fg"+
		"\3\2\2\2\16q\3\2\2\2\20s\3\2\2\2\22u\3\2\2\2\24w\3\2\2\2\26|\3\2\2\2\30"+
		"\u0080\3\2\2\2\32\u0082\3\2\2\2\34\u008f\3\2\2\2\36\u0092\3\2\2\2 \u0094"+
		"\3\2\2\2\"\u00a3\3\2\2\2$\u00a5\3\2\2\2&\u00a7\3\2\2\2(\u00b5\3\2\2\2"+
		"*\u00bc\3\2\2\2,\u00c1\3\2\2\2.\u00c3\3\2\2\2\60\u00cc\3\2\2\2\62\u00ce"+
		"\3\2\2\2\64\u00de\3\2\2\2\66\u00e5\3\2\2\28\u00e9\3\2\2\2:\u00f6\3\2\2"+
		"\2<\u00f8\3\2\2\2>\u010e\3\2\2\2@\u0146\3\2\2\2B\u0148\3\2\2\2D\u014e"+
		"\3\2\2\2F\u0156\3\2\2\2H\u015c\3\2\2\2J\u015e\3\2\2\2LN\5\4\3\2ML\3\2"+
		"\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3\2\2\2QO\3\2\2\2RS\7\2\2\3S\3\3"+
		"\2\2\2TX\5\6\4\2UX\5\b\5\2VX\5\n\6\2WT\3\2\2\2WU\3\2\2\2WV\3\2\2\2X\5"+
		"\3\2\2\2YZ\5\f\7\2Z[\5\24\13\2[\\\7\30\2\2\\\7\3\2\2\2]^\5\f\7\2^_\5\26"+
		"\f\2_c\3\2\2\2`a\7\6\2\2ac\5\26\f\2b]\3\2\2\2b`\3\2\2\2c\t\3\2\2\2de\7"+
		"\20\2\2ef\58\35\2f\13\3\2\2\2gl\5\16\b\2hi\7\26\2\2ik\7\27\2\2jh\3\2\2"+
		"\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2m\r\3\2\2\2nl\3\2\2\2or\5\20\t\2pr\5\22"+
		"\n\2qo\3\2\2\2qp\3\2\2\2r\17\3\2\2\2st\t\2\2\2t\21\3\2\2\2uv\7\65\2\2"+
		"v\23\3\2\2\2wz\7\65\2\2xy\7.\2\2y{\5\30\r\2zx\3\2\2\2z{\3\2\2\2{\25\3"+
		"\2\2\2|}\7\65\2\2}~\5\32\16\2~\177\5\36\20\2\177\27\3\2\2\2\u0080\u0081"+
		"\5> \2\u0081\31\3\2\2\2\u0082\u008b\7\22\2\2\u0083\u0088\5\34\17\2\u0084"+
		"\u0085\7\31\2\2\u0085\u0087\5\34\17\2\u0086\u0084\3\2\2\2\u0087\u008a"+
		"\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008c\3\2\2\2\u008a"+
		"\u0088\3\2\2\2\u008b\u0083\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\u008e\7\23\2\2\u008e\33\3\2\2\2\u008f\u0090\5\f\7\2\u0090\u0091"+
		"\7\65\2\2\u0091\35\3\2\2\2\u0092\u0093\5 \21\2\u0093\37\3\2\2\2\u0094"+
		"\u0098\7\24\2\2\u0095\u0097\5\"\22\2\u0096\u0095\3\2\2\2\u0097\u009a\3"+
		"\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009b\3\2\2\2\u009a"+
		"\u0098\3\2\2\2\u009b\u009c\7\25\2\2\u009c!\3\2\2\2\u009d\u00a4\5 \21\2"+
		"\u009e\u00a4\5\6\4\2\u009f\u00a4\5\66\34\2\u00a0\u00a4\5&\24\2\u00a1\u00a4"+
		"\5,\27\2\u00a2\u00a4\5.\30\2\u00a3\u009d\3\2\2\2\u00a3\u009e\3\2\2\2\u00a3"+
		"\u009f\3\2\2\2\u00a3\u00a0\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a2\3\2"+
		"\2\2\u00a4#\3\2\2\2\u00a5\u00a6\5\"\22\2\u00a6%\3\2\2\2\u00a7\u00a8\7"+
		"\b\2\2\u00a8\u00a9\7\22\2\2\u00a9\u00aa\5> \2\u00aa\u00ab\7\23\2\2\u00ab"+
		"\u00af\5$\23\2\u00ac\u00ae\5(\25\2\u00ad\u00ac\3\2\2\2\u00ae\u00b1\3\2"+
		"\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1"+
		"\u00af\3\2\2\2\u00b2\u00b4\5*\26\2\u00b3\u00b2\3\2\2\2\u00b3\u00b4\3\2"+
		"\2\2\u00b4\'\3\2\2\2\u00b5\u00b6\7\t\2\2\u00b6\u00b7\7\b\2\2\u00b7\u00b8"+
		"\7\22\2\2\u00b8\u00b9\5> \2\u00b9\u00ba\7\23\2\2\u00ba\u00bb\5$\23\2\u00bb"+
		")\3\2\2\2\u00bc\u00bd\7\t\2\2\u00bd\u00be\5$\23\2\u00be+\3\2\2\2\u00bf"+
		"\u00c2\5\62\32\2\u00c0\u00c2\5\64\33\2\u00c1\u00bf\3\2\2\2\u00c1\u00c0"+
		"\3\2\2\2\u00c2-\3\2\2\2\u00c3\u00c4\5\60\31\2\u00c4\u00c5\7\30\2\2\u00c5"+
		"/\3\2\2\2\u00c6\u00c8\7\16\2\2\u00c7\u00c9\5> \2\u00c8\u00c7\3\2\2\2\u00c8"+
		"\u00c9\3\2\2\2\u00c9\u00cd\3\2\2\2\u00ca\u00cd\7\f\2\2\u00cb\u00cd\7\r"+
		"\2\2\u00cc\u00c6\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cc\u00cb\3\2\2\2\u00cd"+
		"\61\3\2\2\2\u00ce\u00cf\7\n\2\2\u00cf\u00d1\7\22\2\2\u00d0\u00d2\5> \2"+
		"\u00d1\u00d0\3\2\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d5"+
		"\7\30\2\2\u00d4\u00d6\5> \2\u00d5\u00d4\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6"+
		"\u00d7\3\2\2\2\u00d7\u00d9\7\30\2\2\u00d8\u00da\5> \2\u00d9\u00d8\3\2"+
		"\2\2\u00d9\u00da\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dc\7\23\2\2\u00dc"+
		"\u00dd\5$\23\2\u00dd\63\3\2\2\2\u00de\u00df\7\13\2\2\u00df\u00e0\7\22"+
		"\2\2\u00e0\u00e1\5> \2\u00e1\u00e2\7\23\2\2\u00e2\u00e3\5$\23\2\u00e3"+
		"\65\3\2\2\2\u00e4\u00e6\5> \2\u00e5\u00e4\3\2\2\2\u00e5\u00e6\3\2\2\2"+
		"\u00e6\u00e7\3\2\2\2\u00e7\u00e8\7\30\2\2\u00e8\67\3\2\2\2\u00e9\u00ea"+
		"\7\65\2\2\u00ea\u00ee\7\24\2\2\u00eb\u00ed\5:\36\2\u00ec\u00eb\3\2\2\2"+
		"\u00ed\u00f0\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\u00f1"+
		"\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f1\u00f2\7\25\2\2\u00f29\3\2\2\2\u00f3"+
		"\u00f7\5\6\4\2\u00f4\u00f7\5\b\5\2\u00f5\u00f7\5<\37\2\u00f6\u00f3\3\2"+
		"\2\2\u00f6\u00f4\3\2\2\2\u00f6\u00f5\3\2\2\2\u00f7;\3\2\2\2\u00f8\u00f9"+
		"\7\65\2\2\u00f9\u00fa\5\32\16\2\u00fa\u00fb\5\36\20\2\u00fb=\3\2\2\2\u00fc"+
		"\u00fd\b \1\2\u00fd\u00fe\t\3\2\2\u00fe\u010f\5> \26\u00ff\u0100\t\4\2"+
		"\2\u0100\u010f\5> \25\u0101\u0102\7%\2\2\u0102\u010f\5> \24\u0103\u0104"+
		"\7*\2\2\u0104\u010f\5> \23\u0105\u0106\7\17\2\2\u0106\u010f\5F$\2\u0107"+
		"\u010f\5@!\2\u0108\u010f\7\21\2\2\u0109\u010f\7\65\2\2\u010a\u010b\7\22"+
		"\2\2\u010b\u010c\5> \2\u010c\u010d\7\23\2\2\u010d\u010f\3\2\2\2\u010e"+
		"\u00fc\3\2\2\2\u010e\u00ff\3\2\2\2\u010e\u0101\3\2\2\2\u010e\u0103\3\2"+
		"\2\2\u010e\u0105\3\2\2\2\u010e\u0107\3\2\2\2\u010e\u0108\3\2\2\2\u010e"+
		"\u0109\3\2\2\2\u010e\u010a\3\2\2\2\u010f\u013f\3\2\2\2\u0110\u0111\f\21"+
		"\2\2\u0111\u0112\t\5\2\2\u0112\u013e\5> \22\u0113\u0114\f\20\2\2\u0114"+
		"\u0115\t\4\2\2\u0115\u013e\5> \21\u0116\u0117\f\17\2\2\u0117\u0118\t\6"+
		"\2\2\u0118\u013e\5> \20\u0119\u011a\f\16\2\2\u011a\u011b\t\7\2\2\u011b"+
		"\u013e\5> \17\u011c\u011d\f\r\2\2\u011d\u011e\t\b\2\2\u011e\u013e\5> "+
		"\16\u011f\u0120\f\f\2\2\u0120\u0121\7+\2\2\u0121\u013e\5> \r\u0122\u0123"+
		"\f\13\2\2\u0123\u0124\7-\2\2\u0124\u013e\5> \f\u0125\u0126\f\n\2\2\u0126"+
		"\u0127\7,\2\2\u0127\u013e\5> \13\u0128\u0129\f\t\2\2\u0129\u012a\7&\2"+
		"\2\u012a\u013e\5> \n\u012b\u012c\f\b\2\2\u012c\u012d\7\'\2\2\u012d\u013e"+
		"\5> \t\u012e\u012f\f\7\2\2\u012f\u0130\7.\2\2\u0130\u013e\5> \7\u0131"+
		"\u0132\f\32\2\2\u0132\u013e\t\3\2\2\u0133\u0134\f\31\2\2\u0134\u013e\5"+
		"B\"\2\u0135\u0136\f\30\2\2\u0136\u0137\7\61\2\2\u0137\u013e\7\65\2\2\u0138"+
		"\u0139\f\27\2\2\u0139\u013a\7\26\2\2\u013a\u013b\5> \2\u013b\u013c\7\27"+
		"\2\2\u013c\u013e\3\2\2\2\u013d\u0110\3\2\2\2\u013d\u0113\3\2\2\2\u013d"+
		"\u0116\3\2\2\2\u013d\u0119\3\2\2\2\u013d\u011c\3\2\2\2\u013d\u011f\3\2"+
		"\2\2\u013d\u0122\3\2\2\2\u013d\u0125\3\2\2\2\u013d\u0128\3\2\2\2\u013d"+
		"\u012b\3\2\2\2\u013d\u012e\3\2\2\2\u013d\u0131\3\2\2\2\u013d\u0133\3\2"+
		"\2\2\u013d\u0135\3\2\2\2\u013d\u0138\3\2\2\2\u013e\u0141\3\2\2\2\u013f"+
		"\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140?\3\2\2\2\u0141\u013f\3\2\2\2"+
		"\u0142\u0147\7\62\2\2\u0143\u0147\7\63\2\2\u0144\u0147\7\64\2\2\u0145"+
		"\u0147\7\7\2\2\u0146\u0142\3\2\2\2\u0146\u0143\3\2\2\2\u0146\u0144\3\2"+
		"\2\2\u0146\u0145\3\2\2\2\u0147A\3\2\2\2\u0148\u014a\7\22\2\2\u0149\u014b"+
		"\5D#\2\u014a\u0149\3\2\2\2\u014a\u014b\3\2\2\2\u014b\u014c\3\2\2\2\u014c"+
		"\u014d\7\23\2\2\u014dC\3\2\2\2\u014e\u0153\5> \2\u014f\u0150\7\31\2\2"+
		"\u0150\u0152\5> \2\u0151\u014f\3\2\2\2\u0152\u0155\3\2\2\2\u0153\u0151"+
		"\3\2\2\2\u0153\u0154\3\2\2\2\u0154E\3\2\2\2\u0155\u0153\3\2\2\2\u0156"+
		"\u0158\5H%\2\u0157\u0159\5J&\2\u0158\u0157\3\2\2\2\u0158\u0159\3\2\2\2"+
		"\u0159G\3\2\2\2\u015a\u015d\7\65\2\2\u015b\u015d\5\20\t\2\u015c\u015a"+
		"\3\2\2\2\u015c\u015b\3\2\2\2\u015dI\3\2\2\2\u015e\u0172\7\26\2\2\u015f"+
		"\u0173\7\27\2\2\u0160\u0161\5> \2\u0161\u0168\7\27\2\2\u0162\u0163\7\26"+
		"\2\2\u0163\u0164\5> \2\u0164\u0165\7\27\2\2\u0165\u0167\3\2\2\2\u0166"+
		"\u0162\3\2\2\2\u0167\u016a\3\2\2\2\u0168\u0166\3\2\2\2\u0168\u0169\3\2"+
		"\2\2\u0169\u016f\3\2\2\2\u016a\u0168\3\2\2\2\u016b\u016c\7\26\2\2\u016c"+
		"\u016e\7\27\2\2\u016d\u016b\3\2\2\2\u016e\u0171\3\2\2\2\u016f\u016d\3"+
		"\2\2\2\u016f\u0170\3\2\2\2\u0170\u0173\3\2\2\2\u0171\u016f\3\2\2\2\u0172"+
		"\u015f\3\2\2\2\u0172\u0160\3\2\2\2\u0173K\3\2\2\2\"OWblqz\u0088\u008b"+
		"\u0098\u00a3\u00af\u00b3\u00c1\u00c8\u00cc\u00d1\u00d5\u00d9\u00e5\u00ee"+
		"\u00f6\u010e\u013d\u013f\u0146\u014a\u0153\u0158\u015c\u0168\u016f\u0172";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}