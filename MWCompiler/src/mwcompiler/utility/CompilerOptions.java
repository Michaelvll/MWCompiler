package mwcompiler.utility;

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
    public boolean graphAllocate = false;
    public boolean functionInline = true;
    // Function inline
    public final int INLINE_CALLEE_BOUND = 1 << 6;
    public final int INLINE_CALLER_BOUND = 1 << 10;
    public Integer INLINE_RECURSIVE_LEVEL = 5;

    // Function memorize search
    public final int MEMORIZE_SEARCH_LEVEL = 1 << 8;
    public boolean memorizeSearch = false;


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

        options.addOption(Option.builder("dumpAst").longOpt("dump-ast").desc("Dump dumpAst for source code").hasArg(false).build());

        options.addOption(Option.builder("dumpIR").longOpt("dump-ir").desc("Dump dumpIR for source code").hasArg(false).build());

        options.addOption(Option.builder("nasmLibIncludeCmd").longOpt("nasm-lib-include-cmd").desc("Add include command at the top of nasm output file").hasArg(false).build());

        options.addOption(Option.builder("a").longOpt("allocator").desc("Register allocator [Naive]/Graph").hasArg().build());

        options.addOption(Option.builder("dinline").longOpt("disable-callee-inline").hasArg(false).desc("Disable callee inline").build());

        options.addOption(Option.builder("recursiveInlineLevel").longOpt("recursive-inline-level").hasArg(true).type(Integer.TYPE).desc("Recursive callee inline level[default 1]").build());

        options.addOption(Option.builder("memorizeSearch").longOpt("memorize-search").hasArg(false).desc("Enable memorize search optimization").build());

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

            if (cmd.hasOption("a")) {
                if (cmd.getOptionValue("a").equals("Graph")) graphAllocate = true;
                else if (cmd.getOptionValue("a").equals("Naive")) graphAllocate = false;
                else System.err.println("Unsupported allocator, use naive allocator by default");
            }
            if (cmd.hasOption("disable-callee-inline")) functionInline = false;

            if (cmd.hasOption("recursive-inline-level"))
                INLINE_RECURSIVE_LEVEL = Integer.valueOf(cmd.getOptionValue("recursive-inline-level"));

            if (cmd.hasOption("memorize-search"))
                memorizeSearch = true;

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
