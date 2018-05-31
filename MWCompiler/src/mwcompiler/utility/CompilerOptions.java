package mwcompiler.utility;

import mwcompiler.ir.operands.Operand;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class CompilerOptions {
    // Machine settings
    public int PTR_SIZE = 8;
    public int LENGTH_SIZE = 8;
    public int FUNC_CALL_STACK_ALIGN_SIZE = 16;


    // Compiling Options
    public boolean dumpAst = false;
    public boolean dumpIR = false;
    public boolean nasmLibIncludeCMD = false;
    public InputStream in = System.in;
    public PrintStream out = System.out;
    public PrintStream astOut = System.err;
    public PrintStream irOut = System.err;
    public int warningLevel = 0;
    // Function inline
    public final int INLINE_CALLEE_BOUND = 8;
    public final int INLINE_CALLER_BOUND = 256;


    public void compilerArgSolve(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("i").longOpt("input").desc("Path to the input file").hasArg().type(String.class)
                .argName("Input File").build());

        options.addOption(Option.builder("o").longOpt("output").desc("Path to the output file").hasArg()
                .type(String.class).argName("Output File").build());

        options.addOption(Option.builder("astOutput").desc("Path to the output file for ast").hasArg()
                .type(String.class).argName("Ast Output File").build());

        options.addOption(Option.builder("irOutput").desc("Path to the output file for ir").hasArg()
                .type(String.class).argName("IR Output File").build());

        options.addOption(Option.builder("Wall").desc("Print warnings to stderr").hasArg(false).build());

        options.addOption(Option.builder("h").longOpt("help").hasArg(false).desc("Print help message (this message)").build());

        options.addOption(Option.builder().longOpt("dump-ast").desc("Dump dumpAst for source code").hasArg(false).build());

        options.addOption(Option.builder().longOpt("dump-ir").desc("Dump dumpIR for source code").hasArg(false).build());

        options.addOption(Option.builder().longOpt("nasm-lib-include-cmd").desc("Add include command at the top of nasm output file").hasArg(false).build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        String inputFile = null;
        String outputFile = null;
        String[] restArgs;
        String astOutFile = null;
        String irOutFile = null;
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
                    System.err.println("Too much arguments with no options");
                    System.exit(1);
                }
            } else if (restArgs.length > 1) {
                System.err.println("Arguments with no options not match (expect exactly 1 as the path of input file)");
                System.exit(1);
                return;
            } else if (restArgs.length == 0) System.err.println("No input file specified, input redirected to std-in");
            else inputFile = restArgs[0];

            if (cmd.hasOption("o")) outputFile = cmd.getOptionValue("o");
            else System.err.println("No output file specified, output redirected to std-out");

            if (cmd.hasOption("Wall")) warningLevel = 1;

            if (cmd.hasOption("dump-ast")) this.dumpAst = true;

            if (cmd.hasOption("dump-ir")) this.dumpIR = true;

            if (cmd.hasOption("nasm-lib-include-cmd")) this.nasmLibIncludeCMD = true;

            if (cmd.hasOption("astOutput")) astOutFile = cmd.getOptionValue("astOutput");
            else if (this.dumpAst) System.err.println("No AST output file specified, output redirected to std-err");

            if (cmd.hasOption("irOutput")) irOutFile = cmd.getOptionValue("irOutput");
            else if (this.dumpIR) System.err.println("No IR output file specified, output redirected to std-err");

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
            if (astOutFile != null)
                astOut = new PrintStream(new FileOutputStream(astOutFile));
            if (irOutFile != null)
                irOut = new PrintStream(new FileOutputStream(irOutFile));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }
}
