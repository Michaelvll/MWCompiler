// Generated from CommonMxLexer.g4 by ANTLR 4.7
package mx_gram.tools;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CommonMxLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"BOOL", "INT", "STRING", "VOID", "NULL", "IF", "ELSE", "FOR", "WHILE", 
		"BREAK", "CONTINUE", "RETURN", "NEW", "CLASS", "THIS", "TRUE", "FALSE", 
		"LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACK", "RBRACK", "SEMI", "COMMA", 
		"ADD", "SUB", "MUL", "DIV", "MOD", "GT", "LT", "EQ", "NEQ", "LTE", "GTE", 
		"NOT", "AND", "OR", "LSFT", "RSFT", "BITNOT", "BITAND", "BITOR", "BITXOR", 
		"ASSIGN", "INC", "DEC", "DOT", "BoolLiteral", "IntLiteral", "StringLiteral", 
		"Identifier", "DecimalInt", "HexInt", "OctInt", "BinInt", "Digit", "Alpha", 
		"NonZeroDigit", "HexDigit", "OctDigit", "BinDigit", "Zero", "StringCharacters", 
		"StringCharacter", "EscapeSequence", "LINE_COMMENT", "MULTILINE_COMMENT", 
		"WS"
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


	public CommonMxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CommonMxLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\28\u01ac\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\3\2\3\2\3"+
		"\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34"+
		"\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$"+
		"\3$\3%\3%\3%\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3,\3,"+
		"\3-\3-\3.\3.\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\63\3\63\5"+
		"\63\u0138\n\63\3\64\3\64\3\64\3\64\5\64\u013e\n\64\3\65\3\65\5\65\u0142"+
		"\n\65\3\65\3\65\3\65\5\65\u0147\n\65\3\65\5\65\u014a\n\65\3\66\3\66\7"+
		"\66\u014e\n\66\f\66\16\66\u0151\13\66\3\67\3\67\3\67\7\67\u0156\n\67\f"+
		"\67\16\67\u0159\13\67\5\67\u015b\n\67\38\38\38\68\u0160\n8\r8\168\u0161"+
		"\39\39\69\u0166\n9\r9\169\u0167\3:\3:\3:\6:\u016d\n:\r:\16:\u016e\3;\3"+
		";\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\6B\u0180\nB\rB\16B\u0181\3C\3"+
		"C\5C\u0186\nC\3D\3D\3D\3E\3E\3E\3E\7E\u018f\nE\fE\16E\u0192\13E\3E\3E"+
		"\3E\3E\3F\3F\3F\3F\7F\u019c\nF\fF\16F\u019f\13F\3F\3F\3F\3F\3F\3G\6G\u01a7"+
		"\nG\rG\16G\u01a8\3G\3G\4\u0190\u019d\2H\3\3\5\4\7\5\t\6\13\7\r\b\17\t"+
		"\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\2#\2%\22\'\23)\24+\25-"+
		"\26/\27\61\30\63\31\65\32\67\339\34;\35=\36?\37A C!E\"G#I$K%M&O\'Q(S)"+
		"U*W+Y,[-]._/a\60c\61e\62g\63i\64k\65m\2o\2q\2s\2u\2w\2y\2{\2}\2\177\2"+
		"\u0081\2\u0083\2\u0085\2\u0087\2\u0089\66\u008b\67\u008d8\3\2\16\4\2C"+
		"\\c|\6\2\62;C\\aac|\4\2ZZzz\4\2DDdd\3\2\62;\3\2\63;\5\2\62;CHch\3\2\62"+
		"9\3\2\62\63\4\2$$^^\n\2$$))^^ddhhppttvv\5\2\13\f\17\17\"\"\2\u01ad\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2"+
		"\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2"+
		"\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2"+
		"\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M"+
		"\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2"+
		"\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2"+
		"\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d"+
		"\3\2\2\2\3\u008f\3\2\2\2\5\u0094\3\2\2\2\7\u0098\3\2\2\2\t\u009f\3\2\2"+
		"\2\13\u00a4\3\2\2\2\r\u00a9\3\2\2\2\17\u00ac\3\2\2\2\21\u00b1\3\2\2\2"+
		"\23\u00b5\3\2\2\2\25\u00bb\3\2\2\2\27\u00c1\3\2\2\2\31\u00ca\3\2\2\2\33"+
		"\u00d1\3\2\2\2\35\u00d5\3\2\2\2\37\u00db\3\2\2\2!\u00e0\3\2\2\2#\u00e5"+
		"\3\2\2\2%\u00eb\3\2\2\2\'\u00ed\3\2\2\2)\u00ef\3\2\2\2+\u00f1\3\2\2\2"+
		"-\u00f3\3\2\2\2/\u00f5\3\2\2\2\61\u00f7\3\2\2\2\63\u00f9\3\2\2\2\65\u00fb"+
		"\3\2\2\2\67\u00fd\3\2\2\29\u00ff\3\2\2\2;\u0101\3\2\2\2=\u0103\3\2\2\2"+
		"?\u0105\3\2\2\2A\u0107\3\2\2\2C\u0109\3\2\2\2E\u010c\3\2\2\2G\u010f\3"+
		"\2\2\2I\u0112\3\2\2\2K\u0115\3\2\2\2M\u0117\3\2\2\2O\u011a\3\2\2\2Q\u011d"+
		"\3\2\2\2S\u0120\3\2\2\2U\u0123\3\2\2\2W\u0125\3\2\2\2Y\u0127\3\2\2\2["+
		"\u0129\3\2\2\2]\u012b\3\2\2\2_\u012d\3\2\2\2a\u0130\3\2\2\2c\u0133\3\2"+
		"\2\2e\u0137\3\2\2\2g\u013d\3\2\2\2i\u0149\3\2\2\2k\u014b\3\2\2\2m\u015a"+
		"\3\2\2\2o\u015c\3\2\2\2q\u0163\3\2\2\2s\u0169\3\2\2\2u\u0170\3\2\2\2w"+
		"\u0172\3\2\2\2y\u0174\3\2\2\2{\u0176\3\2\2\2}\u0178\3\2\2\2\177\u017a"+
		"\3\2\2\2\u0081\u017c\3\2\2\2\u0083\u017f\3\2\2\2\u0085\u0185\3\2\2\2\u0087"+
		"\u0187\3\2\2\2\u0089\u018a\3\2\2\2\u008b\u0197\3\2\2\2\u008d\u01a6\3\2"+
		"\2\2\u008f\u0090\7d\2\2\u0090\u0091\7q\2\2\u0091\u0092\7q\2\2\u0092\u0093"+
		"\7n\2\2\u0093\4\3\2\2\2\u0094\u0095\7k\2\2\u0095\u0096\7p\2\2\u0096\u0097"+
		"\7v\2\2\u0097\6\3\2\2\2\u0098\u0099\7u\2\2\u0099\u009a\7v\2\2\u009a\u009b"+
		"\7t\2\2\u009b\u009c\7k\2\2\u009c\u009d\7p\2\2\u009d\u009e\7i\2\2\u009e"+
		"\b\3\2\2\2\u009f\u00a0\7x\2\2\u00a0\u00a1\7q\2\2\u00a1\u00a2\7k\2\2\u00a2"+
		"\u00a3\7f\2\2\u00a3\n\3\2\2\2\u00a4\u00a5\7p\2\2\u00a5\u00a6\7w\2\2\u00a6"+
		"\u00a7\7n\2\2\u00a7\u00a8\7n\2\2\u00a8\f\3\2\2\2\u00a9\u00aa\7k\2\2\u00aa"+
		"\u00ab\7h\2\2\u00ab\16\3\2\2\2\u00ac\u00ad\7g\2\2\u00ad\u00ae\7n\2\2\u00ae"+
		"\u00af\7u\2\2\u00af\u00b0\7g\2\2\u00b0\20\3\2\2\2\u00b1\u00b2\7h\2\2\u00b2"+
		"\u00b3\7q\2\2\u00b3\u00b4\7t\2\2\u00b4\22\3\2\2\2\u00b5\u00b6\7y\2\2\u00b6"+
		"\u00b7\7j\2\2\u00b7\u00b8\7k\2\2\u00b8\u00b9\7n\2\2\u00b9\u00ba\7g\2\2"+
		"\u00ba\24\3\2\2\2\u00bb\u00bc\7d\2\2\u00bc\u00bd\7t\2\2\u00bd\u00be\7"+
		"g\2\2\u00be\u00bf\7c\2\2\u00bf\u00c0\7m\2\2\u00c0\26\3\2\2\2\u00c1\u00c2"+
		"\7e\2\2\u00c2\u00c3\7q\2\2\u00c3\u00c4\7p\2\2\u00c4\u00c5\7v\2\2\u00c5"+
		"\u00c6\7k\2\2\u00c6\u00c7\7p\2\2\u00c7\u00c8\7w\2\2\u00c8\u00c9\7g\2\2"+
		"\u00c9\30\3\2\2\2\u00ca\u00cb\7t\2\2\u00cb\u00cc\7g\2\2\u00cc\u00cd\7"+
		"v\2\2\u00cd\u00ce\7w\2\2\u00ce\u00cf\7t\2\2\u00cf\u00d0\7p\2\2\u00d0\32"+
		"\3\2\2\2\u00d1\u00d2\7p\2\2\u00d2\u00d3\7g\2\2\u00d3\u00d4\7y\2\2\u00d4"+
		"\34\3\2\2\2\u00d5\u00d6\7e\2\2\u00d6\u00d7\7n\2\2\u00d7\u00d8\7c\2\2\u00d8"+
		"\u00d9\7u\2\2\u00d9\u00da\7u\2\2\u00da\36\3\2\2\2\u00db\u00dc\7v\2\2\u00dc"+
		"\u00dd\7j\2\2\u00dd\u00de\7k\2\2\u00de\u00df\7u\2\2\u00df \3\2\2\2\u00e0"+
		"\u00e1\7v\2\2\u00e1\u00e2\7t\2\2\u00e2\u00e3\7w\2\2\u00e3\u00e4\7g\2\2"+
		"\u00e4\"\3\2\2\2\u00e5\u00e6\7h\2\2\u00e6\u00e7\7c\2\2\u00e7\u00e8\7n"+
		"\2\2\u00e8\u00e9\7u\2\2\u00e9\u00ea\7g\2\2\u00ea$\3\2\2\2\u00eb\u00ec"+
		"\7*\2\2\u00ec&\3\2\2\2\u00ed\u00ee\7+\2\2\u00ee(\3\2\2\2\u00ef\u00f0\7"+
		"}\2\2\u00f0*\3\2\2\2\u00f1\u00f2\7\177\2\2\u00f2,\3\2\2\2\u00f3\u00f4"+
		"\7]\2\2\u00f4.\3\2\2\2\u00f5\u00f6\7_\2\2\u00f6\60\3\2\2\2\u00f7\u00f8"+
		"\7=\2\2\u00f8\62\3\2\2\2\u00f9\u00fa\7.\2\2\u00fa\64\3\2\2\2\u00fb\u00fc"+
		"\7-\2\2\u00fc\66\3\2\2\2\u00fd\u00fe\7/\2\2\u00fe8\3\2\2\2\u00ff\u0100"+
		"\7,\2\2\u0100:\3\2\2\2\u0101\u0102\7\61\2\2\u0102<\3\2\2\2\u0103\u0104"+
		"\7\'\2\2\u0104>\3\2\2\2\u0105\u0106\7@\2\2\u0106@\3\2\2\2\u0107\u0108"+
		"\7>\2\2\u0108B\3\2\2\2\u0109\u010a\7?\2\2\u010a\u010b\7?\2\2\u010bD\3"+
		"\2\2\2\u010c\u010d\7#\2\2\u010d\u010e\7?\2\2\u010eF\3\2\2\2\u010f\u0110"+
		"\7>\2\2\u0110\u0111\7?\2\2\u0111H\3\2\2\2\u0112\u0113\7@\2\2\u0113\u0114"+
		"\7?\2\2\u0114J\3\2\2\2\u0115\u0116\7#\2\2\u0116L\3\2\2\2\u0117\u0118\7"+
		"(\2\2\u0118\u0119\7(\2\2\u0119N\3\2\2\2\u011a\u011b\7~\2\2\u011b\u011c"+
		"\7~\2\2\u011cP\3\2\2\2\u011d\u011e\7>\2\2\u011e\u011f\7>\2\2\u011fR\3"+
		"\2\2\2\u0120\u0121\7@\2\2\u0121\u0122\7@\2\2\u0122T\3\2\2\2\u0123\u0124"+
		"\7\u0080\2\2\u0124V\3\2\2\2\u0125\u0126\7(\2\2\u0126X\3\2\2\2\u0127\u0128"+
		"\7~\2\2\u0128Z\3\2\2\2\u0129\u012a\7`\2\2\u012a\\\3\2\2\2\u012b\u012c"+
		"\7?\2\2\u012c^\3\2\2\2\u012d\u012e\7-\2\2\u012e\u012f\7-\2\2\u012f`\3"+
		"\2\2\2\u0130\u0131\7/\2\2\u0131\u0132\7/\2\2\u0132b\3\2\2\2\u0133\u0134"+
		"\7\60\2\2\u0134d\3\2\2\2\u0135\u0138\5!\21\2\u0136\u0138\5#\22\2\u0137"+
		"\u0135\3\2\2\2\u0137\u0136\3\2\2\2\u0138f\3\2\2\2\u0139\u013e\5m\67\2"+
		"\u013a\u013e\5o8\2\u013b\u013e\5q9\2\u013c\u013e\5s:\2\u013d\u0139\3\2"+
		"\2\2\u013d\u013a\3\2\2\2\u013d\u013b\3\2\2\2\u013d\u013c\3\2\2\2\u013e"+
		"h\3\2\2\2\u013f\u0141\7$\2\2\u0140\u0142\5\u0083B\2\u0141\u0140\3\2\2"+
		"\2\u0141\u0142\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u014a\7$\2\2\u0144\u0146"+
		"\7)\2\2\u0145\u0147\5\u0083B\2\u0146\u0145\3\2\2\2\u0146\u0147\3\2\2\2"+
		"\u0147\u0148\3\2\2\2\u0148\u014a\7)\2\2\u0149\u013f\3\2\2\2\u0149\u0144"+
		"\3\2\2\2\u014aj\3\2\2\2\u014b\u014f\t\2\2\2\u014c\u014e\t\3\2\2\u014d"+
		"\u014c\3\2\2\2\u014e\u0151\3\2\2\2\u014f\u014d\3\2\2\2\u014f\u0150\3\2"+
		"\2\2\u0150l\3\2\2\2\u0151\u014f\3\2\2\2\u0152\u015b\5\u0081A\2\u0153\u0157"+
		"\5y=\2\u0154\u0156\5u;\2\u0155\u0154\3\2\2\2\u0156\u0159\3\2\2\2\u0157"+
		"\u0155\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u015b\3\2\2\2\u0159\u0157\3\2"+
		"\2\2\u015a\u0152\3\2\2\2\u015a\u0153\3\2\2\2\u015bn\3\2\2\2\u015c\u015d"+
		"\5\u0081A\2\u015d\u015f\t\4\2\2\u015e\u0160\5{>\2\u015f\u015e\3\2\2\2"+
		"\u0160\u0161\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162p\3"+
		"\2\2\2\u0163\u0165\5\u0081A\2\u0164\u0166\5}?\2\u0165\u0164\3\2\2\2\u0166"+
		"\u0167\3\2\2\2\u0167\u0165\3\2\2\2\u0167\u0168\3\2\2\2\u0168r\3\2\2\2"+
		"\u0169\u016a\5\u0081A\2\u016a\u016c\t\5\2\2\u016b\u016d\5\177@\2\u016c"+
		"\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u016c\3\2\2\2\u016e\u016f\3\2"+
		"\2\2\u016ft\3\2\2\2\u0170\u0171\t\6\2\2\u0171v\3\2\2\2\u0172\u0173\t\2"+
		"\2\2\u0173x\3\2\2\2\u0174\u0175\t\7\2\2\u0175z\3\2\2\2\u0176\u0177\t\b"+
		"\2\2\u0177|\3\2\2\2\u0178\u0179\t\t\2\2\u0179~\3\2\2\2\u017a\u017b\t\n"+
		"\2\2\u017b\u0080\3\2\2\2\u017c\u017d\7\62\2\2\u017d\u0082\3\2\2\2\u017e"+
		"\u0180\5\u0085C\2\u017f\u017e\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u017f"+
		"\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0084\3\2\2\2\u0183\u0186\n\13\2\2"+
		"\u0184\u0186\5\u0087D\2\u0185\u0183\3\2\2\2\u0185\u0184\3\2\2\2\u0186"+
		"\u0086\3\2\2\2\u0187\u0188\7^\2\2\u0188\u0189\t\f\2\2\u0189\u0088\3\2"+
		"\2\2\u018a\u018b\7\61\2\2\u018b\u018c\7\61\2\2\u018c\u0190\3\2\2\2\u018d"+
		"\u018f\13\2\2\2\u018e\u018d\3\2\2\2\u018f\u0192\3\2\2\2\u0190\u0191\3"+
		"\2\2\2\u0190\u018e\3\2\2\2\u0191\u0193\3\2\2\2\u0192\u0190\3\2\2\2\u0193"+
		"\u0194\7\f\2\2\u0194\u0195\3\2\2\2\u0195\u0196\bE\2\2\u0196\u008a\3\2"+
		"\2\2\u0197\u0198\7\61\2\2\u0198\u0199\7,\2\2\u0199\u019d\3\2\2\2\u019a"+
		"\u019c\13\2\2\2\u019b\u019a\3\2\2\2\u019c\u019f\3\2\2\2\u019d\u019e\3"+
		"\2\2\2\u019d\u019b\3\2\2\2\u019e\u01a0\3\2\2\2\u019f\u019d\3\2\2\2\u01a0"+
		"\u01a1\7,\2\2\u01a1\u01a2\7\61\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01a4\bF"+
		"\2\2\u01a4\u008c\3\2\2\2\u01a5\u01a7\t\r\2\2\u01a6\u01a5\3\2\2\2\u01a7"+
		"\u01a8\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\3\2"+
		"\2\2\u01aa\u01ab\bG\3\2\u01ab\u008e\3\2\2\2\23\2\u0137\u013d\u0141\u0146"+
		"\u0149\u014f\u0157\u015a\u0161\u0167\u016e\u0181\u0185\u0190\u019d\u01a8"+
		"\4\b\2\2\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}