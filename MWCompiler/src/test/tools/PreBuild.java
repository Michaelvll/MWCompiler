package test.tools;

import mwcompiler.Mwcc;
import mwcompiler.ast.nodes.Node;
import mwcompiler.frontend.AstBuilder;
import mwcompiler.ir.nodes.ProgramIR;

public class PreBuild {
    private String[] astBuild = {"/dumpAst/VariableDecl.mx", "/dumpAst/FunctionDecl.mx", "/dumpAst/ClassDecl.mx", "./dumpAst/Whole.mx", "./dumpAst/Whole2.mx", "/dumpAst/611.mx"};
    private String[] typeCheck = {"/type/1.mx", "/type/newfail.mx", "/type/constructor.mx"};
    public AstBuilder astBuilder;
    public Node program;
    public ProgramIR programIRRoot;

    public void build() throws Exception {
        build_imp("../testcases" + typeCheck[0]);
    }

    public void build(String filename) throws Exception {
        build_imp(filename);
    }

//    public static void build(InputStream in) throws Exception {
//        CharStream input = CharStreams.fromStream(in);
//        build_imp(input);
//    }

    public ProgramIR getProgramIRRoot() {
        return programIRRoot;
    }

    private void build_imp(String filename) {
        Mwcc mwcc = new Mwcc();
        mwcc.compile(new String[]{"-i", filename});
        programIRRoot = mwcc.getProgramIR();
    }
}
