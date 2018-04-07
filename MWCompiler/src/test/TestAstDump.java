package test;

import mwcompiler.ast.ExprNode;
import mwcompiler.ast.Node;
import mx_gram.tools.MxLexer;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import mwcompiler.ast.tools.*;

public class TestAstDump {
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
