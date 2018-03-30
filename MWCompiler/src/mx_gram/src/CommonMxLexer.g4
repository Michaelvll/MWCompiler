/**
 * CommonMxLexer
 * The lexer grammar for Mx Language used by Mx.g4
 * 
 * @author Michael Wu
 * @version 1.0 
 * @since 2018-03-30
 */

lexer grammar CommonMxLexer;

ID : [a-zA-Z][0-9a-zA-Z_]*;

WS : [ \t\r\n]+ -> skip;