/**
 * DeclaratorNode.java
 * Declarator extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.tools.AstVisitor;

public class DeclaratorNode extends Node {
    //TODO

    @Override
    void accept(AstVisitor visitor) {
        visitor.visit(this);
    }
}
