package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.DumpAstVisitor;
import mwcompiler.frontend.AstBuilder;
import mwcompiler.frontend.ForwardRefPreprocessAstVisitor;
import mwcompiler.frontend.TypeCheckAstVisitor;
import mwcompiler.ir.tools.DumpIRVisitor;
import org.junit.Before;
import org.junit.Test;
import test.tools.PreBuild;

public class TestDumpIRVisitor {
    private Node program;

    @Before
    public void build() throws Exception {
        PreBuild.build();
        program = PreBuild.program;
        ForwardRefPreprocessAstVisitor forwardRefPreprocessAstVisitor = new ForwardRefPreprocessAstVisitor();
        forwardRefPreprocessAstVisitor.apply(program);
        TypeCheckAstVisitor typeCheckAstVisitor = new TypeCheckAstVisitor();
        typeCheckAstVisitor.apply(program);
    }


    @Test
    public void testDumpIRVisitor() {
        DumpIRVisitor dumpIRVisitor = new DumpIRVisitor();
    }
}
