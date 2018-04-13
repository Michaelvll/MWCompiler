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
	variableDeclField
	| functionDeclField
	| classDeclField
	;

// Declarators for the program
variableDeclField : typename variableField SEMI;
functionDeclField:
	typename functionField		# TypeFunction_
	| VOID functionField	# VoidFunction_
	;
classDeclField : CLASS classField;

typename : nonArrayType (LBRACK RBRACK)*;

nonArrayType	: primitiveType | classType;
primitiveType	: BOOL | INT | STRING;
classType		: Identifier;

variableField:
	Identifier (ASSIGN variableInitializer)?
	;

functionField:
	Identifier paramExprField functionBody
	;

variableInitializer : expr;

paramExprField:
	LPAREN (paramExpr (COMMA paramExpr)*)? RPAREN
	;
paramExpr : typename Identifier;

functionBody	: block;
block			: LBRACE statement* RBRACE;
statement:
	block
	| variableDeclField
	| exprField
	| conditionField
	| loopField
	| jumpField
	;

body : statement;
conditionField:
	IF LPAREN cond = expr RPAREN body elseifConditionField* elseConditionField?
	;
elseifConditionField:
	ELSE IF LPAREN cond = expr RPAREN body
	;
elseConditionField	: ELSE body;
loopField			: forField | whileField;
jumpField			: jump SEMI;
jump:
	RETURN expr?	# ReturnJump_
	| BREAK			# BreakJump_
	| CONTINUE		# ContinueJump_
	;
forField:
	FOR LPAREN vardecl = expr? SEMI cond = expr? SEMI step = expr? RPAREN body
	;
whileField : WHILE LPAREN cond = expr RPAREN body;

exprField : expr? SEMI;

classField : Identifier LBRACE classBody* RBRACE;

classBody:
	variableDeclField
	| functionDeclField
	| classConstructField
	;

classConstructField:
	Identifier paramExprField functionBody
	;

expr:
	expr op = (INC | DEC)						# SuffixIncDec_
	| expr arguments							# FunctionCall_
	| expr DOT Identifier						# DotMember_
	| mom = expr LBRACK subscript = expr RBRACK	# BrackMember_
	// Unary Expr
	| <assoc = right> op = (INC | DEC) expr	# UnaryExpr_
	| <assoc = right> op = (ADD | SUB) expr	# UnaryExpr_
	| <assoc = right> op = NOT expr			# UnaryExpr_
	| <assoc = right> op = BITNOT expr		# UnaryExpr_
	// New Expr
	| NEW creator # NewCreator_
	// Binary Expr
	| left = expr op = (MUL | DIV | MOD) right = expr		# BinaryExpr_
	| left = expr op = (ADD | SUB) right = expr				# BinaryExpr_
	| left = expr op = (LSFT | RSFT) right = expr			# BinaryExpr_
	| left = expr op = (LT | GT | LTE | GTE) right = expr	# BinaryExpr_
	| left = expr op = (EQ | NEQ) right = expr				# BinaryExpr_
	| left = expr op = BITAND right = expr					# BinaryExpr_
	| left = expr op = BITXOR right = expr					# BinaryExpr_
	| left = expr op = BITOR right = expr					# BinaryExpr_
	| left = expr op = AND right = expr						# BinaryExpr_
	| left = expr op = OR right = expr						# BinaryExpr_
	| <assoc = right> left = expr op = ASSIGN right = expr	# BinaryExpr_
	// Others
	| literal				# Literal_
	| THIS					# This_
	| Identifier			# Identifier_
	| LPAREN expr RPAREN	# ParenExpr_
	;

literal:
	literalType = BoolLiteral
	| literalType = IntLiteral
	| literalType = StringLiteral
	| literalType = NULL
	;

arguments	: LPAREN exprList? RPAREN;
exprList	: expr (COMMA expr)*;
creator		: createdName arrayCreatorRest?;
createdName	: Identifier | primitiveType;
arrayCreatorRest:
	LBRACK (
		RBRACK
		| expr RBRACK (LBRACK expr RBRACK)* (
			LBRACK RBRACK
		)*
	)
	;
