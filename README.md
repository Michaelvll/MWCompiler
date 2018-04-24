# MWCompiler

A Compiler for Mx language @ACM Class 2016, SJTU. Written in 2018.

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

When building the AST, the variables and types are changed to Symbols which are placed in [mwcompiler.symbols](./MWCompiler/src/mwcompiler/symbols), in order to make quicker comparison and convenience.

#### Forward Reference Preprocessing

In order to support the forward reference of class and functions (including the variables in class), [mwcompiler.symbols.tools.ForwardRefPreprocessAstVisitor](./MWCompiler/src/mwcompiler/symbols/tools/ForwardRefPreprocessAstVisitor.java) builds the Symbol Table for classes, adding variables and functions into it, and add functions into global Symbol Table, before the complete type checking runs.

#### Type Checking

Builds Symbol Tables for each block, while type checking is running. This gets rid of variable using before declaration, by type checking and adding maps into symbol table in the order of the code. In [mwcompiler.symbols.tools.TypeCheckingAstVisitor](./MWCompiler/src/mwcompiler/symbols/tools/.java),by changing the returnType, each visit can return the type with lvalue or rvalue of the statement.

#### Compile Errors and Warnings

When occur errors in semantic analysis, a [mwcompiler.utility.CompilerError](./MWCompiler/src/mwcompiler/utilitiy/CompilerError.java) will be thrown, and the message passed in will be polished. At the top [mwcompiler.Mwcc](./MWCompiler/src/mwcompiler/Mwcc.java) will catch these errors, and print the information into stderr and exit.

Warnings will be add to a collection, [mwcompiler.utility.CompileWarning](./MWCompiler/src/mwcompiler/utility/CompileWaring.java). And at the top, if the warning flag is set, Mwcc will print out the warnings by calling the *printWarning* function in CompileWarning.