package test;

import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.tools.DumpIRVisitor;
import org.junit.Before;
import org.junit.Test;
import test.tools.LLIRInterpreter;
import test.tools.PreBuild;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class TestIRRun {
    @Before
    public void build() throws Exception {
        PreBuild.build("../testcases/ir/2.mx");
    }

    @Test
    public void test() throws Exception {
        ByteArrayOutputStream irOut = new ByteArrayOutputStream();
        ProgramIR programIRRoot = PreBuild.getProgramIRRoot();
        DumpIRVisitor irDumper = new DumpIRVisitor(irOut);
        irDumper.apply(programIRRoot);
        System.out.println(irOut.toString());
//        ByteArrayInputStream irIn = new ByteArrayInputStream(irOut.toByteArray());
//        LLIRInterpreter.apply(irIn, false);
    }
}
