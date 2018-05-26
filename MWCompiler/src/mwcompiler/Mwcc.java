package mwcompiler;

import mwcompiler.ast.nodes.ProgramNode;
import mwcompiler.ast.tools.DumpAstVisitor;
import mwcompiler.backend.CodeGenerator;
import mwcompiler.backend.NaiveAllocator;
import mwcompiler.frontend.*;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.tools.DumpIRVisitor;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import mwcompiler.utility.CompilerOptions;
import mwcompiler.utility.StringProcess;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

/**
 * Mwcc.java
 * The main class of mwcompiler for Mx language
 *
 * @author Michael Wu
 * @version 1.0
 * @since 2018-04-05
 */
public class Mwcc {
    private ProgramNode programAst;
    private ProgramIR programIR;
    private CompilerOptions options = new CompilerOptions();


    /**
     * @param args The entry of Mwcc
     */
    public static void main(String[] args) {
        Mwcc mwwcc = new Mwcc();
        mwwcc.compile(args);
    }

    public void compile(String[] args) {
        options.compilerArgSolve(args);
        buildAst();
        typeCheck();
        buildIR();

//        allocate();
//        codeGenerate();
    }

    public ProgramIR getProgramIR() {
        // For test
        return programIR;
    }

    public ProgramNode getProgramAst() {
        // For test
        return programAst;
    }

    private void buildAst() {
        try {
            CharStream input = CharStreams.fromStream(options.in);
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            StringProcess.setTokens(tokens);
//            parser.setErrorHandler(new BailErrorStrategy());
            parser.removeErrorListeners();
            parser.addErrorListener(new ParserErrorListener());
            MxParser.ProgramContext programContext = parser.program();
            AstBuilder astBuilder = new AstBuilder();
            programAst = astBuilder.build(programContext);
        } catch (IOException e) {
            System.err.println("Can't read from the input file: " + e.getMessage());
            System.exit(1);
        } catch (CompileError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        if (options.dumpAst) {
            DumpAstVisitor astDumper = new DumpAstVisitor();
            astDumper.apply(programAst);
//            System.exit(0);
        }
    }


    private void typeCheck() {
        try {
            ForwardRefPreprocessAstVisitor preprocessAstVisitor = new ForwardRefPreprocessAstVisitor();
            preprocessAstVisitor.apply(programAst);
            TypeCheckAstVisitor typeCheckAstVisitor = new TypeCheckAstVisitor();
            typeCheckAstVisitor.apply(programAst);
        } catch (CompileError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        if (options.warningLevel > 0) {
            CompileWarining.printWarings();
        }
    }

    private void buildIR() {
        IRBuilder irBuilder = new IRBuilder(options);
        programIR = irBuilder.build(programAst);

        if (options.dumpIR) {
            DumpIRVisitor irDumper = new DumpIRVisitor();
            irDumper.apply(programIR);
        }
    }

    private void allocate() {
        NaiveAllocator allocator = new NaiveAllocator(options);
        allocator.apply(programIR);
    }

    private void codeGenerate() {
        CodeGenerator codeGenerator = new CodeGenerator(options);
        codeGenerator.apply(programIR);
    }

}
