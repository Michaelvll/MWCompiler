package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.TypeSymbol;

/**
 * VariableDeclNode.java
 * VariableDeclaration extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */

public class VariableDeclNode extends DeclaratorNode{
    //TODO
    private TypeNode type;
    private TypeSymbol typeSymbol;
    private InstanceSymbol var;
    private ExprNode init;
    private Location typePos;
    private Location varPos;
    private Location initPos;

    public VariableDeclNode(TypeNode type, String var, ExprNode init, Location typePos, Location varPos, Location initPos) {
        this.type = type;
        this.var = InstanceSymbol.getSymbol(var);
        this.init = init;
        this.typePos = typePos;
        this.varPos = varPos;
        this.initPos = initPos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this );
    }

    public void setType(TypeNode type, Location typePos) {
        this.type = type;
        this.typePos = typePos;
    }

    public InstanceSymbol getVar() {
        return var;
    }

    public TypeNode getType() {
        return type;
    }

    public ExprNode getInit() {
        return init;
    }

    public Location getTypePos() {
        return typePos;
    }

    public Location getVarPos() {
        return varPos;
    }

    public Location getInitPos() {
        return initPos;
    }

    public TypeSymbol getTypeSymbol() {
        return typeSymbol;
    }

    @Override
    public void transform2Symbol() {
        typeSymbol = type.getSymbol();
    }

    @Override
    public Location getStartLocation() {
        return typePos;
    }
}
