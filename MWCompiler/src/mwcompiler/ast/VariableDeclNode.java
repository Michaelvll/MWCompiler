/**
 * VariableDeclNode.java
 * VariableDeclaration extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
package mwcompiler.ast;

import mwcompiler.ast.*;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

public class VariableDeclNode extends DeclaratorNode{
    //TODO
    public TypeNode type;
    public IdentifierNode var;
    public ExprNode init;
    public Location typePos;
    public Location namePos;
    public Location initPos;


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this );
    }
}
