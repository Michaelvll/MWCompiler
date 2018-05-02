package mwcompiler;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.DumpAstVisitor;
import mwcompiler.frontend.AstBuilder;
import mwcompiler.frontend.ForwardRefPreprocessAstVisitor;
import mwcompiler.frontend.TypeCheckAstVisitor;
import mwcompiler.utility.*;
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
    private static Node programAstRoot;


    /**
     * @param args The entry of Mwcc
     */
    public static void main(String[] args) {
        CompilerOptions.compilerArgSolve(args);
        buildAst();
        typeCheck();
    }

    private static void buildAst() {
        try {
            CharStream input = CharStreams.fromStream(CompilerOptions.in);
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            StringProcess.setTokens(tokens);
//            parser.setErrorHandler(new BailErrorStrategy());
            parser.removeErrorListeners();
            parser.addErrorListener(new ParserErrorListener());
            MxParser.ProgramContext programContext = parser.program();
            AstBuilder astBuilder = new AstBuilder();
            programAstRoot = astBuilder.build(programContext);
        } catch (IOException e) {
            System.err.println("Can't read from the input file: " + e.getMessage());
            System.exit(1);
        } catch (CompileError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
//        if (!ErrorSet.empty()) {
//            ErrorSet.print(System.err);
//        }
        if (CompilerOptions.ast) {
            DumpAstVisitor astDumper = new DumpAstVisitor();
            astDumper.apply(programAstRoot);
            System.exit(0);
        }

    }


    private static void typeCheck() {
        try {
            ForwardRefPreprocessAstVisitor preprocessAstVisitor = new ForwardRefPreprocessAstVisitor();
            preprocessAstVisitor.apply(programAstRoot);
            TypeCheckAstVisitor typeCheckAstVisitor = new TypeCheckAstVisitor();
            typeCheckAstVisitor.apply(programAstRoot);
        } catch (CompileError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        if (CompilerOptions.warningLevel > 0) {
            CompileWarining.printWarings();
        }
    }

}
