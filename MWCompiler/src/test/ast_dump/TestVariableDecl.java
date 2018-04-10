package test.ast_dump;

import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.AstDump;
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
 * TestVariableDecl.java
 * A test for the Ast builder and dumper only with variable declaration
 *
 * @author Michael Wu
 * @since 2018-04-11
 * */

public class TestVariableDecl {
    private Node program;

    @Before
    public void build() throws Exception {
        CharStream input = CharStreams.fromFileName("../testcases/ast/VariableDecl.mx");
        MxLexer lexer = new MxLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MxParser parser = new MxParser(tokens);
        ParseTree tree = parser.program();
        BuildAstVisitor buildAstVisitor = new BuildAstVisitor();
        program = buildAstVisitor.visitProgram((MxParser.ProgramContext) tree);
    }


    @Test
    public void testDumpAst() {
        AstVisitor dump = new AstDump();
        program.accept(dump);
    }
}
