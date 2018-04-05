package test_gen_tools;

import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.FileInputStream;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import mx_gram.tools.*;

public class genTestLangVariDecl {
    public static void main(String[] args) throws Exception {
        String inputFile = "C:/AResource/Compiler/MWCompiler/MxLang/testLangVariDecl.mx";
        String ansFile = "C:/AResource/Compiler/MWCompiler/MxLang/testLangVariDecl.ans";
        // if (args.length > 1)
        //     inputFile = args[0];
        // ansFile = args[1];
        InputStream is;
        String ans = null;
        CharStream input;
        is = new FileInputStream(inputFile);
        ans = new String(Files.readAllBytes(Paths.get(ansFile)));
        input = CharStreams.fromStream(is);

        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();
        PrintWriter writer = new PrintWriter(ansFile, "UTF-8");
        writer.print(tree.toStringTree());
        writer.close();

    }
}