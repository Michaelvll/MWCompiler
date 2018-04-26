package mwcompiler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.BuildAst;
import mwcompiler.utility.Location;
import mwcompiler.symbols.tools.ForwardRefPreprocessAstVisitor;
import mwcompiler.symbols.tools.TypeCheckAstVisitor;
import mwcompiler.utility.StringProcess;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import mwcompiler.utility.ParserErrorListener;
import org.apache.commons.cli.*;

import mx_gram.tools.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Mwcc.java
 * The main class of mwcompiler for Mx language
 *
 * @author Michael Wu
 * @version 1.0
 * @since 2018-04-05
 */
public class Mwcc {
    private static InputStream in = System.in;
    private static PrintStream out = System.err;
    private static Node programAstRoot;
    private static Integer warningLevel = 0;

    /**
     * @param args The entry of Mwcc
     */
    public static void main(String[] args) {
        compilerArgSolve(args);
        buildAst();
        typeCheck();
    }

    private static void compilerArgSolve(String[] args) {
        Options options = new Options();
        Option input = Option.builder("i").longOpt("input").desc("Path to the input file").hasArg().type(String.class)
                .argName("Input File").build();
        options.addOption(input);

        Option output = Option.builder("o").longOpt("output").desc("Path to the output File").hasArg()
                .type(String.class).argName("Output File").build();
        options.addOption(output);

        Option warning = new Option("W", "Wall", false, "Print warnings to stderr");
        options.addOption(warning);

        Option help = new Option("h", "help", false, "Print help message (this message)");
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        String inputFile = null;
        String outputFile = null;
        String[] restArgs;
        try {
            System.err.print(StringProcess.BLACK);
            cmd = parser.parse(options, args);
            restArgs = cmd.getArgs();
            if (cmd.hasOption("h")) {
                formatter.printHelp("MWcc [options] <File>", options);
                System.exit(0);
            }
            if (cmd.hasOption("i")) {
                inputFile = cmd.getOptionValue("i");
                if (restArgs.length > 0) {
                    System.err.println("Too much unflagged arguments");
                    System.exit(1);
                    return;
                }
            } else if (restArgs.length > 1) {
                System.err.println("Unflagged arguments not match (expect exactly 1 as the path of input file)");
                System.exit(1);
                return;
            } else if (restArgs.length == 0) {
                System.err.println("No input file specified, input redirected to stdin");
            } else {
                inputFile = cmd.getArgs()[0];
            }
            if (cmd.hasOption("o")) {
                outputFile = cmd.getOptionValue("o");
            } else {
                System.err.println("No output file specified, output redirected to stderr");
            }
            if (cmd.hasOption("Wall")) {
                warningLevel = 1;
            }

        } catch (ParseException e) {
            System.err.println("Unexpected exception: " + e.getMessage());
            formatter.printHelp("Mwcc [Options] <File>", options);

            System.exit(1);
            return;
        }

        try {
            if (inputFile != null)
                in = new FileInputStream(inputFile);
            if (outputFile != null)
                out = new PrintStream(new FileOutputStream(outputFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());

            System.exit(1);
        }

    }

    private static void buildAst() {
        BuildAst buildAst;
        try {
            CharStream input = CharStreams.fromStream(in);
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            StringProcess.setTokens(tokens);
//            parser.setErrorHandler(new BailErrorStrategy());
            parser.removeErrorListeners();
            parser.addErrorListener(new ParserErrorListener());
            ParseTree tree = parser.program();
            buildAst = new BuildAst();
            programAstRoot = buildAst.visit(tree);
        } catch (IOException e) {
            System.err.println("Can't read from the input file: " + e.getMessage());
            System.exit(1);
        } catch (CompileError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    private static void typeCheck() {
        try {
            AstVisitor constructSymbolTableAstVisitor = new ForwardRefPreprocessAstVisitor();
            programAstRoot.accept(constructSymbolTableAstVisitor);
            AstVisitor typeNotPresentException = new TypeCheckAstVisitor();
            programAstRoot.accept(typeNotPresentException);
        } catch (CompileError e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        if (warningLevel > 0) {
            CompileWarining.printWarings();
        }
    }

}
