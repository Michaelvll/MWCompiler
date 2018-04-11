/**
 * CommonMxLexer
 * The lexer grammar for Mx Language used by Mx.g4
 * 
 * @author Michael Wu
 * @version 1.0 
 * @since 2018-03-30
 */

lexer grammar CommonMxLexer;
// channels {
// 	COMMENTS,
// 	WHITESPACE
// }

// Keywords
BOOL		: 'bool';
INT			: 'int';
STRING		: 'string';
VOID		: 'void';
NULL		: 'null';
IF			: 'if';
ELSE		: 'else';
FOR			: 'for';
WHILE		: 'while';
BREAK		: 'break';
CONTINUE	: 'continue';
RETURN		: 'return';
NEW			: 'new';
CLASS		: 'class';
THIS		: 'this';

fragment TRUE	: 'true';
fragment FALSE	: 'false';

// Seperator
LPAREN	: '(';
RPAREN	: ')';
LBRACE	: '{';
RBRACE	: '}';
LBRACK	: '[';
RBRACK	: ']';
SEMI	: ';';
COMMA	: ',';

// Operators
// Arithmatic
ADD	: '+';
SUB	: '-';
MUL	: '*';
DIV	: '/';
MOD	: '%';
// Relation
GT	: '>';
LT	: '<';
EQ	: '==';
NEQ	: '!=';
LTE	: '<=';
GTE	: '>=';
// Logic
NOT	: '!';
AND	: '&&';
OR	: '||';
// Bit
LSFT	: '<<';
RSFT	: '>>';
BITNOT	: '~';
BITAND	: '&';
BITOR	: '|';
BITXOR	: '^';
// Assign
ASSIGN : '=';
// Inc&dec
INC	: '++';
DEC	: '--';
// Member
DOT : '.';

BoolLiteral : TRUE | FALSE;

IntLiteral : DecimalInt | HexInt | OctInt | BinInt;

// Name of variables(including name of classes and functions)
StringLiteral : '"' StringCharactors? '"';

Identifier : [a-zA-Z][0-9a-zA-Z_]*;

fragment DecimalInt			: Zero | NonZeroDigit Digit*;
fragment HexInt				: Zero [xX] HexDigit+;
fragment OctInt				: Zero OctDigit+;
fragment BinInt				: Zero [bB] BinDigit+;
fragment Digit				: [0-9];
fragment Alpha				: [a-zA-Z];
fragment NonZeroDigit		: [1-9];
fragment HexDigit			: [0-9a-fA-F];
fragment OctDigit			: [0-7];
fragment BinDigit			: [01];
fragment Zero				: '0';
fragment StringCharactors	: StringCharactor+;
fragment StringCharactor	: ~["\\] | EscapeSequence;
fragment EscapeSequence		: '\\' [btnfr"'\\];

LINE_COMMENT		: '//' .*? '\n' -> skip;
MULTILINE_COMMENT	: '/*' .*? '*/' -> skip;
WS					: [ \t\r\n]+ -> skip;