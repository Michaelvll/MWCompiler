package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.DumpAstVisitor;
import mwcompiler.frontend.AstBuilder;
import org.junit.Before;
import org.junit.Test;
import test.tools.PreBuild;

/**
 * TestDumpAstVisitor.java
 * A test for the Ast builder and dumper only with variable declaration
 *
 * @author Michael Wu
 * @since 2018-04-11
 */

public class TestDumpAstVisitor {


    private Node program;
    private AstBuilder astBuilder;

    @Before
    public void build() throws Exception {
        PreBuild.build();
        program = PreBuild.program;
    }


    @Test
    public void testDumpAst() {
        AstVisitor<Void> dump = new DumpAstVisitor();
        program.accept(dump);

    }
}
