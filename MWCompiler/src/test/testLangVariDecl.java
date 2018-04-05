package test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.junit.Test;

import mx_gram.tools.*;

public class testLangVariDecl {
    @Test
    public void test() {
        String inputFile = "C:/AResource/Compiler/MWCompiler/MxLang/testLangVariDecl.mx";
        String ansFile = "C:/AResource/Compiler/MWCompiler/MxLang/testLangVariDecl.ans";
        // if (args.length > 1)
        //     inputFile = args[0];
        // ansFile = args[1];
        InputStream is;
        String ans = null;
        CharStream input = null;
        try {
            is = new FileInputStream(inputFile);
            ans = new String(Files.readAllBytes(Paths.get(ansFile)));
            input = CharStreams.fromStream(is);
        } catch (Exception e) {
            assertNotNull("open file failure", e);
        }

        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();

        assertEquals(tree.toStringTree(), ans);
    }
}