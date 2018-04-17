package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.tools.ForwardRefPreprocessAstVisitor;
import mwcompiler.symbols.tools.TypeCheckAstVisitor;
import org.junit.Before;
import org.junit.Test;

public class TestTypeCheckAstVisitor {
    private Node program;

    @Before
    public void build() throws Exception {
        PreBuild.build();
        program = PreBuild.program;
    }


    @Test
    public void testDumpAst() {
        AstVisitor  constructSymbolTableAstVisitor = new ForwardRefPreprocessAstVisitor();
        program.accept(constructSymbolTableAstVisitor);
        AstVisitor typeNotPresentException= new TypeCheckAstVisitor();
        program.accept(typeNotPresentException);
    }
}
