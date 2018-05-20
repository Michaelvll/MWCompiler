package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.symbols.FunctionSymbol;
import mwcompiler.utility.Location;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.TypeSymbol;

import java.util.ArrayList;
import java.util.List;

/**
 * FunctionDeclNode.java
 * Function declaration node extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-13
 */
public class FunctionDeclNode extends DeclaratorNode {
    private InstanceSymbol instanceSymbol;
    private FunctionSymbol functionSymbol;
    private List<VariableDeclNode> paramList;
    private BlockNode body;
    private Location returnTypePos;
    private Location namePos;
    private Location paramListPos;
    private Location bodyPos;

    public FunctionDeclNode(TypeSymbol returnTypeSymbol, InstanceSymbol instanceSymbol, List<VariableDeclNode> paramList, BlockNode body,
                            Location returnTypePos, Location namePos, Location paramListPos, Location bodyPos) {
        this.paramList = paramList;
        this.body = body;
        this.instanceSymbol = instanceSymbol;

        this.returnTypePos = returnTypePos;
        this.namePos = namePos;
        this.paramListPos = paramListPos;
        this.bodyPos = bodyPos;
        List<TypeSymbol> typeParams = new ArrayList<>();
        paramList.forEach(x->typeParams.add(x.getTypeSymbol()));
        this.functionSymbol = new FunctionSymbol(returnTypeSymbol, instanceSymbol, typeParams);
    }

    public void setReturnType(TypeSymbol returnTypeSymbol, Location returnTypePos) {
        this.returnTypePos = returnTypePos;
        this.functionSymbol.setReturnType(returnTypeSymbol);
    }


    @Override
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Location getNamePos() {
        return namePos;
    }

    public Location getParamListPos() {
        return paramListPos;
    }

    public Location getBodyPos() {
        return bodyPos;
    }

    public List<VariableDeclNode> getParamList() {
        return paramList;
    }

    public BlockNode getBody() {
        return body;
    }

    public InstanceSymbol getInstanceSymbol() {
        return instanceSymbol;
    }


    @Override
    public Location getStartLocation() {
        return returnTypePos;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

}
