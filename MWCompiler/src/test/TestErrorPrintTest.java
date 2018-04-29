package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.frontend.ForwardRefPreprocessAstVisitor;
import mwcompiler.frontend.TypeCheckAstVisitor;
import mwcompiler.symbols.tools.ExprReturnType;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import test.tools.PreBuild;

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
            AstVisitor<Void> constructSymbolTableAstVisitor = new ForwardRefPreprocessAstVisitor();
            program.accept(constructSymbolTableAstVisitor);
            AstVisitor<ExprReturnType> typeNotPresentException = new TypeCheckAstVisitor();
            program.accept(typeNotPresentException);
        } catch (CompileError e) {
            CompileWarining.printWarings();
            System.err.println(e.getMessage());
            assertNotNull(e);
        }

    }
}
