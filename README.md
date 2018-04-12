# MWCompiler

A Compiler for Mx language @ACM Class, SJTU

## Language Feature

The language feature is shown in the [M_language_manual](./MxDescribe/M_language_manual.pdf). And the compiler is for this specific language.

## Prerequest

* Java SDK 1.8.0+ (hasn't been tested for the earilier version)
* Maven 3.5.3+ (Ditto)
* Other dependencies, which is written in [pom.xml](./MWCompiler/pom.xml), will be automatally fetched by maven
* *The test-case can be downloaded from [compiler2017-testcases](https://bitbucket.org/acmcompiler/compiler2017-testcases.git)*

## Components of MWCompiler

### Lexer & Parser

The Lexer and Parser is powered by [antlr.v4](www.antlr.com), which can automatically write codes for generating AST(Abstract Syntax Tree) from the .g4 file. The .g4 file is contained in the folder [mx_gram](./MWCompiler/src/mx_gram)

### Semantic Analysis

In order to make the semantic analysis more explicit, MWcc build an Abstract Syntex Tree (AST), in which program is seperated into abstract parts and represented by AST nodes, from the Parse Tree built by antlr.

#### AST (Abstract Syntax Tree) Building

After the lexer and parser have finished, MWcc begins AST building by calling [mwcompiler.ast.tools.BuildAstVisitor](./MWCompiler/src/mwcompiler/ast/tools/BuildAstVisitor.java). And the abstract syntex nodes are defined in [mwcompiler.ast.nodes](./MWCompiler/src/mwcompiler/ast/nodes/)

When building the AST, types defined by (class) declaration are transformed to *TypeSymbol*s (same types have only one same instance), as well as the variables in declaration are transformed to *VariableSymbol*s. (Types and variables used are still in form of String).

**Symbol tables**, which forms a chain, are stored in *BlockNode*s. The construction of the Symbol Tables has been done while AST is building, but the content in those Symbol Tables are incompleted.
