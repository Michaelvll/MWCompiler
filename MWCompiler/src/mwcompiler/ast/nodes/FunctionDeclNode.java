package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;
import mwcompiler.symbols.TypeSymbol;

import java.sql.Types;
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
    private FunctionTypeSymbol functionTypeSymbol;
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
        for (VariableDeclNode node : paramList) {
            typeParams.add(node.getTypeSymbol());
        }
        this.functionTypeSymbol = FunctionTypeSymbol.builder(returnTypeSymbol, typeParams);
    }

    public void setReturnType(TypeSymbol returnTypeSymbol, Location returnTypePos) {
        this.returnTypePos = returnTypePos;
        this.functionTypeSymbol.setReturnType(returnTypeSymbol);
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
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

    public FunctionTypeSymbol getFunctionTypeSymbol() {
        return functionTypeSymbol;
    }
}
