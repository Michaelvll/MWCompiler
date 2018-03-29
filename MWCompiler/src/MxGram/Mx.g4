/**
 * MxGram
 * The grammar for Mx Language which is descriped in the manual file
 * 
 * @author Michael Wu
 * @version 1.0 
 * @since 2018-03-30
 */

grammar Mx;

// Generate a header for the generated file indicating the package it belongs to.
@header {
package MxGram;
}

prog : stat+;

stat : ID;

ID : [a-zA-Z][0-9a-zA-Z_]*;

WS : [ \t\r\n]+ -> skip;