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

type:
	type LBRACK RBRACK	# ArrayType_
	| primitiveType		# PrimitiveType_
	| classType			# ClassType_
	;

primitiveType	: BOOL | INT | STRING;
classType		: Identifier;

variableField:
	Identifier (ASSIGN variableInitializer)?
	;

variableInitializer : expr;

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

classField : Identifier classBody;

classBody : LBRACE declarator* RBRACE;

expr:
	expr op = (INC | DEC)					# SuffixIncDec_
	| expr arguments						# FunctionCall_
	| expr selector							# Selector_
	| <assoc = right> op = (INC | DEC) expr	# PreffixIncDec_
	| <assoc = right> op = (ADD | SUB) expr	# PreffixAddSub_
	| <assoc = right> NOT expr				# NotExpr_
	| <assoc = right> BITNOT expr			# BitNotExpr_
	| NEW creator							# NewCreator_
	| expr op = (MUL | DIV | MOD) expr		# MulDivExpr_
	| expr op = (ADD | SUB) expr			# AddSubExpr_
	| expr op = (LSFT | RSFT) expr			# ShiftExpr_
	| expr op = (LT | GT | LTE | GTE) expr	# CompareExpr_
	| expr op = (EQ | NEQ) expr				# EqNeqExpr_
	| expr BITAND expr						# BitAndExpr_
	| expr BITXOR expr						# BitXorExpr_
	| expr BITOR expr						# BitOrExpr_
	| expr AND expr							# AndExpr_
	| expr OR expr							# OrExpr_
	| <assoc = right> expr ASSIGN expr		# AssignExpr_
	| literal								# Literal_
	| THIS									# This_
	| Identifier							# Identifier_
	| LPAREN expr RPAREN					# ParenExpr_
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
