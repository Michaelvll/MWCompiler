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
	variableDeclField	# VariableDecl_
	| functionDeclField	# FunctionDecl_
	| classDeclField	# ClassDecl_
	;

// Declarators for the program
variableDeclField : type variableField SEMI;
functionDeclField:
	type functionField
	| VOID functionField
	;
classDeclField : CLASS classField;

type:
	primitiveType (LBRACK RBRACK)*	# PrimitiveType_
	| classType (LBRACK RBRACK)*	# ClassType_
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
	block				# BlockField_
	| variableDeclField	# VariableField_
	| exprField			# ExprField_
	| conditionField	# ConditionField_
	| loopField			# LoopField_
	| jumpField			# JumpField_
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
	FOR LPAREN vardecl = variableField? SEMI cond = expr? SEMI step = expr?
		RPAREN body
	;
whileField : WHILE LPAREN cond = expr RPAREN body;

exprField : expr? SEMI;

classField : Identifier classBody;

classBody : LBRACE declarator* RBRACE;

variableInitializer : expr;

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
	| Identifier							# Identifier_
	| LPAREN expr RPAREN					# ParenExpr_
	;

selector:
	DOT Identifier arguments?	# DotMember_
	| LBRACK expr RBRACK		# BrackMember_
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
