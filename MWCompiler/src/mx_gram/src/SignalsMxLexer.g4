lexer grammar SignalsMxLexer;
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
// Relation
GT		: '>';
LT		: '<';
EQ		: '==';
NOTEQ	: '!=';
LTE		: '<=';
GTE		: '>=';
// Logic
NOT	: '!';
AND	: '&&';
OR	: '||';
// Bit
LSFT	: '<<';
RSFT	: '>>';
TILDE	: '~';
BITAND	: '&';
BITOR	: '|';
XOR		: '^';
// Assign
ASSIGN : '=';
// Inc&dec
INC	: '++';
DEC	: '--';
// Member
DOT	: '.';
MOD	: '%';