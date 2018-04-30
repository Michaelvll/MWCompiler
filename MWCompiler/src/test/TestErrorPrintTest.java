package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.frontend.ForwardRefPreprocessAstVisitor;
import mwcompiler.frontend.TypeCheckAstVisitor;
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
        for (File file : new File("../testcases/type/").listFiles()) {
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
            ForwardRefPreprocessAstVisitor forwardRefPreprocessAstVisitor = new ForwardRefPreprocessAstVisitor();
            program.accept(forwardRefPreprocessAstVisitor);
            TypeCheckAstVisitor typeCheckAstVisitor = new TypeCheckAstVisitor();
            program.accept(typeCheckAstVisitor);
        } catch (CompileError e) {
            CompileWarining.printWarings();
            System.err.println(e.getMessage());
            assertNotNull(e);
        }

    }
}
