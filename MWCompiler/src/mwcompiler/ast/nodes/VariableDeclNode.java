package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.utility.Location;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.TypeSymbol;

/**
 * VariableDeclNode.java
 * VariableDeclaration extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class VariableDeclNode extends DeclaratorNode {
    private TypeSymbol typeSymbol;
    private InstanceSymbol var;
    private ExprNode init;
    private Location typePos;
    private Location varPos;
    private Location initPos;

    public VariableDeclNode(TypeSymbol typeSymbol, String var, ExprNode init, Location typePos, Location varPos, Location initPos) {
        this.typeSymbol = typeSymbol;
        this.var = InstanceSymbol.builder(var);
        this.init = init;
        this.typePos = typePos;
        this.varPos = varPos;
        this.initPos = initPos;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public void setType(TypeSymbol typeSymbol, Location typePos) {
        this.typeSymbol = typeSymbol;
        this.typePos = typePos;
    }

    public InstanceSymbol getVarSymbol() {
        return var;
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
    public Location getStartLocation() {
        return typePos;
    }

}
