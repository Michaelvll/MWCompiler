# MWCompiler

This project is a Compiler for Mx language, a course project for Compiler 2018(MS208), ACM Class 2016, SJTU. Written in 2018.

## Target

* This compiler is aimed for translating *Mx* program into x64-nasm code, which can be linked by gcc and executed on linux.
* The language feature of *Mx* is shown in the [M_language_manual](./MxDescribe/M_language_manual.pdf). And the compiler is for this specific language.

## Prerequest

* Java SDK 1.8.0+ (hasn't been tested for the earilier version)
* // Maven 3.5.3+ (Ditto)
* // Other dependencies, which is written in [pom.xml](./MWCompiler/pom.xml), will be automatally fetched by maven
* Or the dependencies are all in the [lib](./lib)
* *The test-case can be downloaded from [compiler2017-testcases](https://bitbucket.org/acmcompiler/compiler2017-testcases.git)*

## Usage

Arguments that can be passed into the MWCompiler are shown below.

    usage: Mwcc [Options] <File>
    -a,--allocator <arg>                                   Register allocator [Naive]/Graph
    -astOutput <Ast Output File>                           Path to the output file for ast
    -dinline,--disable-callee-inline                       Disable callee inline
    -dumpAst,--dump-ast                                    Dump dumpAst for source code
    -dumpIR,--dump-ir                                      Dump dumpIR for source code
    -h,--help                                              Print help message (this message)
    -i,--input <Input File>                                Path to the input file
    -irOutput <IR Output File>                             Path to the output file for ir
    -memoizedSearch,--memoized-search                      Enable memoized search optimization 
    -nasmLibIncludeCmd,--nasm-lib-include-cmd              Add include command at the top of nasm output file
    -o,--output <Output File>                              Path to the output file
    -recursiveInlineLevel,--recursive-inline-level <arg>   Recursive callee inline level[default 1]
    -Wall                                                  Print warnings to stderr  

## Compontents of MWCompiler

The detail of the components of this compiler can be found in [Docs/MWCompilerDetails](./Docs/MWCompilerDetails.md)
