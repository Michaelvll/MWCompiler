package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.antlr.v4.runtime.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;

/**
 * TestParser.java
 * A test for the parser in order to test no exceptions
 * 
 * @author Michael Wu
 * @version 1.0
 * @since 2018-04-06
 */
@RunWith(Parameterized.class)
public class TestParser {
    @Parameters
    public static Collection<Object[]> testFiles() {
        Collection<Object[]> params = new ArrayList<>();
        for (File file : new File("../testcases/parser").listFiles()) {
            if (file.isFile()) {
                params.add(new Object[] { file.getAbsolutePath() });
            }
        }
        return params;
    }

    private String filename;

    /**
     * @param filename the filename to set
     */
    public TestParser(String filename) {
        this.filename = filename;
    }

    @Test
    public void testNoException() {
        System.out.println(filename);
        try {
            CharStream input = CharStreams.fromFileName(filename);
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            parser.setErrorHandler(new BailErrorStrategy());
            parser.program();
        } catch (Exception e) {
            assertNull(filename, e);
        }
        // assertFalse(true);
    }
}