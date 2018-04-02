/**
 * MxGram
 * The grammar for Mx Language which is descriped in the manual file
 * 
 * @author Michael Wu
 * @version 1.0 
 * @since 2018-03-30
 */

grammar Mx;

options {
	tokenVocab = CommonMxLexer;
}

program : declarator+ EOF;

declarator:
	variableDeclarator
	// | functionDeclarator
	// | classDeclarator
	;

// Declarators for the program
variableDeclarator:
	type fieldDeclarators ';'
	;
// functionDeclarator	:;
// classDeclarator		:;

type:
	ClassType ('[' ']')*
	| PrimitiveType ('[' ']')*
	;
fieldDeclarators:
	Identifier (
		'=' variableInitializer
	)?
	;
variableInitializer:
	expression
	// | arrayInitializer
	;

expression:
	conditionalExpression (
		'=' expression
	)?
	;
conditionalExpression:
	conditionalOrExpression
	;

conditionalOrExpression:
	conditionalAndExpression (
		'||' conditionalAndExpression
	)*
	;
conditionalAndExpression:
	bitOrExpression (
		'&&' bitOrExpression
	)*
	;
bitOrExpression:
	bitXorExpression (
		'|' bitXorExpression
	)*
	;

bitXorExpression:
	bitAndExpression (
		'^' bitAndExpression
	)*
	;
bitAndExpression:
	equalityExpression (
		'&' equalityExpression
	)*
	;
equalityExpression:
	relationalExpression (
		('==' | '!=') relationalExpression
	)*
	;
relationalExpression:
	shiftExpression (
		relationalOP shiftExpression
	)*
	;
relationalOP:
	'<'
	| '>'
	| '<='
	| '>='
	;
shiftExpression:
	addSubExpression (
		('<<' | '>>') addSubExpression
	)*
	;
addSubExpression:
	mulDivModExpression (
		('+' | '-') mulDivModExpression
	)*
	;
mulDivModExpression:
	unaryExpression (
		('*' | '/' | '%') unaryExpression
	)*
	;
unaryExpression:
	'++' unaryExpression
	| '--' unaryExpression
	| '+' unaryExpression
	| '-' unaryExpression
	| unaryNotPMExpression
	;
unaryNotPMExpression:
	'~' unaryExpression
	| '!' unaryExpression
	| primary selector* (
		'++'
		| '--'
	)
	;
selector:
	'.' Identifier arguments?
	| '[' expression ']'
	;

primary:
	parenExpression
	| literal
	| 'new' creator
	| Identifier (
		'.' Identifier
	)* identifierSuffix?
	;
parenExpression:
	'(' expression ')'
	;
literal:
	BoolLiteral
	| IntegerLiteral
	| StringLiteral
	| 'null'
	;

arguments:
	'(' expressionList? ')'
	;
expressionList:
	expression (',' expression)
	;
creator:
	createdName arrayCreatorRest
	;
createdName:
	Identifier ('.' Identifier)*
	| PrimitiveType
	;
arrayCreatorRest:
	'[' (
		']'
		| expression ']' (
			'[' expression ']'
		)* ('[' ']')*
	)
	;
identifierSuffix:
	'[' expression ']'
	| arguments
	;