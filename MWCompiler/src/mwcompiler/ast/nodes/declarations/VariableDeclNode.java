package mwcompiler.ast.nodes.declarations;

import mwcompiler.ast.nodes.expressions.ExprNode;
import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.Instance;
import mwcompiler.utility.Location;
import mwcompiler.symbols.TypeSymbol;

/**
 * VariableDeclNode.java
 * VariableDeclaration extends from DeclarationNode
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class VariableDeclNode extends DeclarationNode {
    private TypeSymbol typeSymbol;
    private Instance var;
    private ExprNode init;
    private Location typePos;
    private Location varPos;
    private Location initPos;

    public VariableDeclNode(TypeSymbol typeSymbol, String var, ExprNode init, Location typePos, Location varPos, Location initPos) {
        this.typeSymbol = typeSymbol;
        this.var = Instance.builder(var);
        this.init = init;
        this.typePos = typePos;
        this.varPos = varPos;
        this.initPos = initPos;
    }

    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public void setType(TypeSymbol typeSymbol, Location typePos) {
        this.typeSymbol = typeSymbol;
        this.typePos = typePos;
    }

    public Instance getVarSymbol() {
        return var;
    }

    public ExprNode init() {
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
