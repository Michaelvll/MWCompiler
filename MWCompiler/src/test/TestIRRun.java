package test;

import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.tools.DumpIRVisitor;
import org.junit.Before;
import org.junit.Test;
import test.tools.PreBuild;

import java.io.ByteArrayOutputStream;

public class TestIRRun {
    private PreBuild preBuild = new PreBuild();

    @Before
    public void build() throws Exception {
        preBuild.build("../testcases/ir/test.mx");
    }

    @Test
    public void test() {
        ByteArrayOutputStream irOut = new ByteArrayOutputStream();
        ProgramIR programIRRoot = preBuild.getProgramIRRoot();
        DumpIRVisitor irDumper = new DumpIRVisitor(irOut);
        irDumper.apply(programIRRoot);
        System.out.println(irOut.toString());
//        ByteArrayInputStream irIn = new ByteArrayInputStream(irOut.toByteArray());
//        LLIRInterpreter.apply(irIn, false);
    }
}
