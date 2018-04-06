package test;

import mwcompiler.ast.tools.BuildAstVisitor;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class TestProgramNodeVistInit {
    @Parameterized.Parameters
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
    public TestProgramNodeVistInit(String filename) {
        this.filename = filename;
    }

    @Test
    public void test() {
        try{
            BuildAstVisitor builder = new BuildAstVisitor();
            CharStream input = CharStreams.fromFileName(filename);
            MxLexer lexer = new MxLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MxParser parser = new MxParser(tokens);
            parser.setErrorHandler(new BailErrorStrategy());
            ParseTree tree = parser.program();
            builder.visit(tree);
        }catch (Exception e) {
            assertNull(e);
        }
    }
}
