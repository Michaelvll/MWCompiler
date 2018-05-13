package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.frontend.ForwardRefPreprocessAstVisitor;
import mwcompiler.frontend.IRBuilder;
import mwcompiler.frontend.TypeCheckAstVisitor;
import mwcompiler.ir.nodes.BasicBlock;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.tools.DumpIRVisitor;
import org.junit.Before;
import org.junit.Test;
import test.tools.PreBuild;

public class TestDumpIRVisitor {
    private Node program;

    @Before
    public void build() throws Exception {
        PreBuild.build(System.in);
        program = PreBuild.program;
        ForwardRefPreprocessAstVisitor forwardRefPreprocessAstVisitor = new ForwardRefPreprocessAstVisitor();
        forwardRefPreprocessAstVisitor.apply(program);
        TypeCheckAstVisitor typeCheckAstVisitor = new TypeCheckAstVisitor();
        typeCheckAstVisitor.apply(program);
    }


    @Test
    public void testDumpIRVisitor() {
        IRBuilder irBuilder = new IRBuilder();
        ProgramIR programIR = irBuilder.build(program);
        DumpIRVisitor dumpIRVisitor = new DumpIRVisitor();
        dumpIRVisitor.apply(programIR);

    }
}
