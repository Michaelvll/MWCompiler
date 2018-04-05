/**
 * mwcc
 * The main class of mwcompiler for Mx language
 * 
 * @author Michael Wu
 * @version 1.0
 * @since 2018-04-05
 */
package mwcompiler;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.cli.*;

public class Mwcc {
    private static InputStream in = System.in;
    private static PrintStream out = System.out;

    public static void main(String[] args) throws Exception {
        compilerArgSolve(args);
        buildAst();
    }

    private static void compilerArgSolve(String[] args) {
        Options options = new Options();
        Option input = Option.builder("i").longOpt("input").desc("Path to the input file").hasArg().type(String.class)
                .argName("Input File").build();
        options.addOption(input);

        Option output = Option.builder("o").longOpt("output").desc("Path to the output File").hasArg()
                .type(String.class).argName("Output File").build();
        options.addOption(output);

        Option help = new Option("h", "help", false, "Print help message (this message)");
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        String inputFile = null;
        String outputFile = null;
        String[] restArgs;
        try {
            cmd = parser.parse(options, args);
            restArgs = cmd.getArgs();
            if (cmd.hasOption("i")) {
                inputFile = cmd.getOptionValue("i");
                if (restArgs.length > 0) {
                    System.out.println("Too much unflagged arguments");
                    System.exit(1);
                    return;
                }
            } else if (restArgs.length > 1) {
                System.out.println("Unflagged arguments not match (expect exactly 1 as the path of input file)");
                System.exit(1);
                return;
            } else if (restArgs.length == 0) {
                System.out.println("No input file specified, input redirected to stdin");
            } else {
                inputFile = cmd.getArgs()[0];
            }
            if (cmd.hasOption("o")) {
                outputFile = cmd.getOptionValue("o");
            } else {
                System.out.println("No output file specified, output redirected to stdout");
            }
        } catch (ParseException e) {
            System.out.println("Unexpected exception: " + e.getMessage());
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
            System.out.println(e.getMessage());

            System.exit(1);
            return;
        }

    }

    private static void buildAst() {
    }

}