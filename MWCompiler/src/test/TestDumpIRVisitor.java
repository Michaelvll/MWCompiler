package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.frontend.ForwardRefPreprocessAstVisitor;
import mwcompiler.frontend.IRBuilder;
import mwcompiler.frontend.TypeCheckAstVisitor;
import mwcompiler.ir.nodes.BasicBlock;
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
        IRBuilder irBuilder = new IRBuilder();
        irBuilder.build(program);
        BasicBlock startBasicBlock = irBuilder.getStartBasicBlock();
        DumpIRVisitor dumpIRVisitor = new DumpIRVisitor();
        dumpIRVisitor.apply(startBasicBlock);

    }
}
