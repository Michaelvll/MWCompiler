package test.tools;

import mwcompiler.Mwcc;
import mwcompiler.ast.nodes.Node;
import mwcompiler.frontend.AstBuilder;
import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.utility.StringProcess;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.InputStream;

public class PreBuild {
    private static String[] astBuild = {"/dumpAst/VariableDecl.mx", "/dumpAst/FunctionDecl.mx", "/dumpAst/ClassDecl.mx", "./dumpAst/Whole.mx", "./dumpAst/Whole2.mx", "/dumpAst/611.mx"};
    private static String[] typeCheck = {"/type/1.mx", "/type/newfail.mx", "/type/constructor.mx"};
    public static AstBuilder astBuilder;
    public static Node program;
    public static ProgramIR programIRRoot;

    public static void build() throws Exception {
        build_imp("../testcases" + typeCheck[0]);
    }

    public static void build(String filename) throws Exception {
        build_imp(filename);
    }

//    public static void build(InputStream in) throws Exception {
//        CharStream input = CharStreams.fromStream(in);
//        build_imp(input);
//    }

    public static ProgramIR getProgramIRRoot() {
        return programIRRoot;
    }

    private static void build_imp(String filename) {
        Mwcc.main(new String[]{"-i", filename});
        programIRRoot = Mwcc.getProgramIRRoot();
    }
}
