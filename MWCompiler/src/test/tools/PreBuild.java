package test.tools;

import mwcompiler.ast.nodes.Node;
import mwcompiler.frontend.AstBuilder;
import mwcompiler.utility.StringProcess;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class PreBuild {
    private static String[] astBuild = {"/ast/VariableDecl.mx", "/ast/FunctionDecl.mx", "/ast/ClassDecl.mx", "./ast/Whole.mx", "./ast/Whole2.mx", "/ast/611.mx"};
    private static String[] typeCheck = {"/type/1.mx", "/type/newfail.mx", "/type/constructor.mx"};
    public static AstBuilder astBuilder;
    public static Node program;

    public static void build() throws Exception {
        CharStream input = CharStreams.fromFileName("../testcases" + typeCheck[0]);
//        CharStream input = CharStreams.fromStream(System.in);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StringProcess.setTokens(tokens);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();
        astBuilder = new AstBuilder();
        program = astBuilder.visit(tree);
    }

    public static void build(String filename) throws Exception {
        CharStream input = CharStreams.fromFileName(filename);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        StringProcess.setTokens(tokens);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();
        astBuilder = new AstBuilder();
        program = astBuilder.visit(tree);
    }
}
