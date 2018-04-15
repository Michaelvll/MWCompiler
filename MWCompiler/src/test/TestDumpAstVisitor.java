package test;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.DumpAstVisitor;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.BuildAstVisitor;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;

/**
 * TestDumpAstVisitor.java
 * A test for the Ast builder and dumper only with variable declaration
 *
 * @author Michael Wu
 * @since 2018-04-11
 * */

public class TestDumpAstVisitor {
    private String[] files={"/ast/VariableDecl.mx", "/ast/FunctionDecl.mx", "/ast/ClassDecl.mx", "./ast/Whole.mx", "./ast/Whole2.mx","/ast/611.mx"};

    private Node program;
    private BuildAstVisitor buildAstVisitor;

    @Before
    public void build() throws Exception {
        CharStream input = CharStreams.fromFileName("../testcases"+files[5]);
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();
        buildAstVisitor = new BuildAstVisitor();
        program = buildAstVisitor.visitProgram((MxParser.ProgramContext) tree);
    }


    @Test
    public void testDumpAst() {
        AstVisitor dump = new DumpAstVisitor();
        program.accept(dump);

    }
}
