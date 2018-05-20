package mwcompiler.utility;

import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class CompilerOptions {
    public static Boolean dumpAst = false;
    public static Boolean dumpIR = false;
    public static InputStream in = System.in;
    public static PrintStream out = System.out;
    public static Integer warningLevel = 0;


    public static void compilerArgSolve(String[] args) {
        Options options = new Options();
        Option input = Option.builder("i").longOpt("input").desc("Path to the input file").hasArg().type(String.class)
                .argName("Input File").build();
        options.addOption(input);

        Option output = Option.builder("o").longOpt("output").desc("Path to the output file").hasArg()
                .type(String.class).argName("Output File").build();
        options.addOption(output);

        Option warning = Option.builder("Wall").desc("Print warnings to stderr").hasArg(false).build();
        options.addOption(warning);

        Option help = Option.builder("h").longOpt("help").hasArg(false).desc("Print help message (this message)").build();
        options.addOption(help);

        Option dumpAst = Option.builder().longOpt("dump_ast").desc("Dump dumpAst for source code").hasArg(false).build();
        options.addOption(dumpAst);

        Option dumpIR = Option.builder().longOpt("dump_ir").desc("Dump dumpIR for source code").hasArg(false).build();
        options.addOption(dumpIR);

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
                    System.err.println("Too much arguments with no options");
                    System.exit(1);
                }
            } else if (restArgs.length > 1) {
                System.err.println("Arguments with no options not match (expect exactly 1 as the path of input file)");
                System.exit(1);
                return;
            } else if (restArgs.length == 0) {
                System.err.println("No input file specified, input redirected to std-in");
            } else {
                inputFile = restArgs[0];
            }

            if (cmd.hasOption("o")) {
                outputFile = cmd.getOptionValue("o");
            } else {
                System.err.println("No output file specified, output redirected to std-out");
            }

            if (cmd.hasOption("Wall")) {
                warningLevel = 1;
            }

            if (cmd.hasOption("dump_ast")) {
                CompilerOptions.dumpAst = true;
            }

            if (cmd.hasOption("dump_ir")) {
                CompilerOptions.dumpIR = true;
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
}
