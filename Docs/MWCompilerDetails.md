# Components of MWCompiler

## Front End

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

## Back End

// TODO

### IR Build

#### Basic Block

#### Constant Fold

### Function Inline

#### Normal Function Inline

#### Recursive-Call Function Inline

### Memorize Search

### Liveness Analysis

#### Analysis

#### Dead Code Elimination

#### Dead loop Elimination

### Register Allocation

#### Naive Allocation

#### Graph Allocation

### Code Generation