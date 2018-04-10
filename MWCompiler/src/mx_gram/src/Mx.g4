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
variableDeclField : type variableField SEMI;
functionDeclField:
	type functionField		# TypeFunction_
	| VOID functionField	# VoidFunction_
	;
classDeclField : CLASS classField;

type : nonArrayType (LBRACK RBRACK)*;

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
paramExpr : type Identifier;

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
jump				: RETURN expr? | BREAK | CONTINUE;

forField:
	FOR LPAREN vardecl = expr? SEMI cond = expr? SEMI step = expr? RPAREN body
	;
whileField : WHILE LPAREN cond = expr RPAREN body;

exprField : expr? SEMI;

classField : Identifier LBRACE declarator* RBRACE;

expr:
	expr op = (INC | DEC)									# SuffixIncDec_
	| expr arguments										# FunctionCall_
	| expr selector											# Selector_
	| <assoc = right> op = (INC | DEC) expr					# PreffixIncDec_
	| <assoc = right> op = (ADD | SUB) expr					# PreffixAddSub_
	| <assoc = right> NOT expr								# NotExpr_
	| <assoc = right> BITNOT expr							# BitNotExpr_
	| NEW creator											# NewCreator_
	| left = expr op = (MUL | DIV | MOD) right = expr		# MulDivExpr_
	| left = expr op = (ADD | SUB) right = expr				# AddSubExpr_
	| left = expr op = (LSFT | RSFT) right = expr			# ShiftExpr_
	| left = expr op = (LT | GT | LTE | GTE) right = expr	# CompareExpr_
	| left = expr op = (EQ | NEQ) right = expr				# EqNeqExpr_
	| left = expr BITAND right = expr						# BitAndExpr_
	| left = expr BITXOR right = expr						# BitXorExpr_
	| left = expr BITOR right = expr						# BitOrExpr_
	| left = expr AND right = expr							# AndExpr_
	| left = expr OR right = expr							# OrExpr_
	| <assoc = right> left = expr ASSIGN right = expr		# AssignExpr_
	| literal												# Literal_
	| THIS													# This_
	| Identifier											# Identifier_
	| LPAREN expr RPAREN									# ParenExpr_
	;

selector:
	DOT Identifier arguments?	# DotMember_
	| LBRACK expr RBRACK		# BrackMember_
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
