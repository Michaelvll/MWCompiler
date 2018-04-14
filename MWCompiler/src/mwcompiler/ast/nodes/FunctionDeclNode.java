package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;
import mwcompiler.symbols.FunctionTypeSymbol;
import mwcompiler.symbols.InstanceSymbol;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclNode extends DeclaratorNode {
    private TypeNode returnType;
    private InstanceSymbol instanceSymbol;
    private FunctionTypeSymbol functionTypeSymbol;
    private List<VariableDeclNode> paramList;
    private BlockNode body;
    private Location returnTypePos;
    private Location namePos;
    private Location paramListPos;
    private Location bodyPos;


    public FunctionDeclNode(TypeNode returnType, InstanceSymbol instanceSymbol, List<VariableDeclNode> paramList, BlockNode body,
                            Location returnTypePos, Location namePos, Location paramListPos, Location bodyPos) {
        this.returnType = returnType;
        this.paramList = paramList;
        this.body = body;
        this.instanceSymbol = instanceSymbol;

        this.returnTypePos = returnTypePos;
        this.namePos = namePos;
        this.paramListPos = paramListPos;
        this.bodyPos = bodyPos;
    }

    public void setReturnType(TypeNode returnType, Location returnTypePos) {
        this.returnType = returnType;
        this.returnTypePos = returnTypePos;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public Location getReturnTypePos() {
        return returnTypePos;
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

    public TypeNode getReturnType() {
        return returnType;
    }

    public InstanceSymbol getInstanceSymbol() {
        return instanceSymbol;
    }

    @Override
    public void transform2Symbol() {
        List<TypeNode> params = new ArrayList<>();
        for (VariableDeclNode variableDeclNode : paramList) {
            params.add(variableDeclNode.getType());
        }
       this.functionTypeSymbol = FunctionTypeSymbol.builder(returnType, params);

    }

    @Override
    public Location getStartLocation() {
        return returnTypePos;
    }

    public FunctionTypeSymbol getFunctionTypeSymbol() {
        return functionTypeSymbol;
    }
}
