package mwcompiler.symbols.tools;

import mwcompiler.ast.nodes.DeclaratorNode;
import mwcompiler.ast.nodes.IdentifierExprNode;
import mwcompiler.ast.nodes.Node;
import mwcompiler.ast.tools.BuildAstVisitor;

import java.util.List;

public class TransformType2Symbol {
    private static List<Node> unsymboledFunctionNodes;
    private static List<Node> unsymboledVariableNodes;
    private  static  List<Node> unsymboledIdentifierNodes;

    public static void symbolize(BuildAstVisitor astVisitor) {
        unsymboledFunctionNodes = astVisitor.unsymboledFunctionNodes;
        unsymboledVariableNodes = astVisitor.unsymboledVariableNodes;
        unsymboledIdentifierNodes = astVisitor.unsymboledIdentifierExprNode;
        symbolizeMeta(unsymboledFunctionNodes);
        symbolizeMeta(unsymboledVariableNodes);
        symbolizeMeta(unsymboledIdentifierNodes);
    }


    private static void symbolizeMeta(List<Node> unsymboledList) {
        for (Node unsymboled : unsymboledList) {
            try {
                unsymboled.transform2Symbol();
            } catch (RuntimeException e) {
                throw new RuntimeException("ERROR: (Transforming Type to Symbols) " + e.getMessage() + unsymboled.getStartLocation().getLocation());
            }
        }
    }
}
