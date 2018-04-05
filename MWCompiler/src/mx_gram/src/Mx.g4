/**
 * MxGram
 * The grammar for Mx Language which is descriped in the manual file
 * 
 * @author Michael Wu
 * @version 1.0 
 * @since 2018-03-30
 */

grammar Mx;

import CommonMxLexer;
// options {
// 	tokenVocab = CommonMxLexer;
// }

program : declarator* EOF;

declarator:
	variableDeclField	# VARIABLEDECL
	| functionDeclField	# FUNCTIONDECL
	| classDeclField	# CLASSDECL
	;

// Declarators for the program
variableDeclField : type variableField SEMI;
functionDeclField:
	type functionField
	| VOID functionField
	;
classDeclField : CLASS classField;

type:
	primitiveType (LBRACK RBRACK)*	# PRIMITIVETYPE
	| classType (LBRACK RBRACK)*	# CLASSTYPE
	;

primitiveType	: BOOL | INT | STRING;
classType		: Identifier;

variableField:
	Identifier (ASSIGN variableInitializer)?
	;

functionField:
	Identifier paramExprField functionBody
	;

paramExprField:
	LPAREN (paramExpr (COMMA paramExpr)*)? RPAREN
	;
paramExpr : type Identifier;

functionBody	: block;
block			: LBRACE statement* RBRACE;
statement:
	block				# BLOCKFIELD
	| variableDeclField	# VARIABLEDECLField
	| exprField			# EXPRFIELD
	| conditionField	# CONDITIONFIELD
	| loopField			# LOOPFIELD
	| jumpField			# JUMPFIELD
	;

body : statement;
conditionField:
	IF LPAREN cond = expr RPAREN body elseifConditionField* elseConditionField*
	;
elseifConditionField:
	ELSE IF LPAREN cond = expr RPAREN body
	;
elseConditionField	: ELSE body;
loopField			: forField | whileField;
jumpField			: jump SEMI;
jump				: RETURN expr? | BREAK | CONTINUE;

forField:
	FOR LPAREN vardecl = variableField? SEMI cond = expr? SEMI step = expr?
		RPAREN body
	;
whileField : WHILE LPAREN cond = expr RPAREN body;

exprField : expr? SEMI;

classField : Identifier classBody;

classBody : LBRACE declarator* RBRACE;

variableInitializer : expr;

expr:
	expr op = (INC | DEC)					# SUFFIXINCDEC
	| expr arguments						# FUNCTIONCALL
	| expr selector							# SELECTOR
	| <assoc = right> op = (INC | DEC) expr	# PREFFIXINCDEC
	| <assoc = right> op = (ADD | SUB) expr	# PREFFIXADDSUB
	| <assoc = right> NOT expr				# NOTEXPR
	| <assoc = right> BITNOT expr			# BITNOTEXPR
	| NEW creator							# NEWCREATOR
	| expr op = (MUL | DIV | MOD) expr		# MULDIVMOD
	| expr op = (ADD | SUB) expr			# ADDSUB
	| expr op = (LSFT | RSFT) expr			# SHIFT
	| expr op = (LT | GT | LTE | GTE) expr	# RELATION
	| expr op = (EQ | NEQ) expr				# EQNEQ
	| expr BITAND expr						# BITANDEXPR
	| expr BITXOR expr						# BITXOREXPR
	| expr BITOR expr						# BITOREXPR
	| expr AND expr							# ANDEXPR
	| expr OR expr							# OREXPR
	| <assoc = right> expr ASSIGN expr		# ASSIGNEXPR
	| literal								# LITERAL
	| Identifier							# ID
	| LPAREN expr RPAREN					# PARENEXPR
	;

selector:
	DOT Identifier arguments?	# DOTMEM
	| LBRACK expr RBRACK		# BRACKMEM
	;

literal:
	BoolLiteral
	| IntegerLiteral
	| StringLiteral
	| NULL
	;

arguments	: LPAREN exprList? RPAREN;
exprList	: expr (COMMA expr)*;
creator		: createdName arrayCreatorRest?;
createdName:
	Identifier (DOT Identifier)*
	| primitiveType
	;
arrayCreatorRest:
	LBRACK (
		RBRACK
		| expr RBRACK (LBRACK expr RBRACK)* (
			LBRACK RBRACK
		)*
	)
	;
