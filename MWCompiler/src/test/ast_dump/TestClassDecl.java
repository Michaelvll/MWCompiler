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

public class TestClassDecl {
    private Node program;

    @Before
    public void build() throws Exception {
        CharStream input = CharStreams.fromFileName("../testcases/ast/ClassDecl.mx");
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
