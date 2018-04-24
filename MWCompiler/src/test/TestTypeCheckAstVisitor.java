package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.tools.ForwardRefPreprocessAstVisitor;
import mwcompiler.symbols.tools.TypeCheckAstVisitor;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.CompileWarining;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class TestTypeCheckAstVisitor {
    private Node program;

    @Before
    public void build() throws Exception {
        PreBuild.build();
        program = PreBuild.program;
    }


    @Test
    public void testDumpAst() {
        try {
            AstVisitor constructSymbolTableAstVisitor = new ForwardRefPreprocessAstVisitor();
            program.accept(constructSymbolTableAstVisitor);
            AstVisitor typeNotPresentException = new TypeCheckAstVisitor();
            program.accept(typeNotPresentException);
        } catch (CompileError e) {
            CompileWarining.printWarings();
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
