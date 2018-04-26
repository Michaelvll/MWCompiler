package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.BuildAst;
import mwcompiler.symbols.tools.ForwardRefPreprocessAstVisitor;
import mwcompiler.symbols.tools.TypeCheckAstVisitor;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.TestCase.assertNotNull;


@RunWith(Parameterized.class)
public class TestErrorPrintTest {

    private Node program;

    @Parameterized.Parameters
    public static Collection<Object[]> testFiles() {
        Collection<Object[]> params = new ArrayList<>();
        for (File file : new File("../testcases/error_test/").listFiles()) {
            if (file.isFile()) {
                params.add(new Object[]{file.getAbsolutePath()});
            }
        }
        return params;
    }

    private String filename;

    /**
     * @param filename the filename to set
     */
    public TestErrorPrintTest(String filename) {
        this.filename = filename;
    }



    @Test
    public void test() throws Exception {
        PreBuild.build(filename);
        program = PreBuild.program;
        try {
            AstVisitor constructSymbolTableAstVisitor = new ForwardRefPreprocessAstVisitor();
            program.accept(constructSymbolTableAstVisitor);
            AstVisitor typeNotPresentException = new TypeCheckAstVisitor();
            program.accept(typeNotPresentException);
        } catch (CompileError e) {
            CompileWarining.printWarings();
            System.err.println(e.getMessage());
            assertNotNull(e);
            //            System.exit(1);
        }

    }
}
