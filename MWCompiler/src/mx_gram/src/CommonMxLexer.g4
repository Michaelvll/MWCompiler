/**
 * CommonMxLexer
 * The lexer grammar for Mx Language used by Mx.g4
 * 
 * @author Michael Wu
 * @version 1.0 
 * @since 2018-03-30
 */

lexer grammar CommonMxLexer;
import KeywordsMxLexer, SignalsMxLexer;
channels {
	COMMENTS,
	WHITESPACE
}

// Name of variables(including name of classes and functions)
Identifier:
	Alpha (Alpha | Digit | '_')*
	;
LINE_COMMENT:
	'//' .*? '\n' -> channel(COMMENTS)
	;
MULTILINE_COMMENT:
	'/*' .*? '*/' -> channel(COMMENTS)
	;
WS:
	[ \t\r\n]+ -> channel(WHITESPACE)
	;

PrimitiveType:
	BOOL
	| INT
	| STRING
	;
ClassType : Identifier;

BoolLiteral : TRUE | FALSE;

IntegerLiteral:
	DecimalInteger
	| HexInteger
	| OctInteger
	| BinInteger
	;

StringLiteral:
	'"' StringCharactors? '"'
	;

fragment DecimalInteger:
	'0'
	| NonZeroDigit Digit*
	;
fragment HexInteger:
	'0' [xX] HexDigit+
	;
fragment OctInteger:
	'0' OctDigit+
	;
fragment BinInteger:
	'0' [bB] BinDigit+
	;
fragment Digit			: [0-9];
fragment Alpha			: [a-zA-Z];
fragment NonZeroDigit	: [1-9];
fragment HexDigit		: [0-9a-fA-F];
fragment OctDigit		: [0-7];
fragment BinDigit		: [01];

fragment StringCharactors:
	StringCharactor+
	;
fragment StringCharactor:
	~["\\]
	| EscapeSequence
	;
fragment EscapeSequence:
	'\\' [btnfr"'\\]
	;