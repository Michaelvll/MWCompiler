package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

import java.util.List;

public class FunctionDeclNode extends DeclaratorNode {
    private TypeNode returnType;
    private String name;
    private List<VariableDeclNode> paramList;
    private BlockNode body;
    private Location returnTypePos;
    private Location namePos;
    private Location paramListPos;
    private Location bodyPos;


    public FunctionDeclNode(TypeNode returnType, String name, List<VariableDeclNode> paramList, BlockNode body, Location returnTypePos, Location namePos, Location paramListPos, Location bodyPos) {
        this.returnType = returnType;
        this.name = name;
        this.paramList = paramList;
        this.body = body;

        this.returnTypePos = returnTypePos;
        this.namePos = namePos;
        this.paramListPos = paramListPos;
        this.bodyPos = bodyPos;
    }

    public void setReturnType(TypeNode returnType, Location returnTypePos) {
        this.returnType = returnType;
        this.returnTypePos = returnTypePos;
    }

    public String getName() {
        return name;
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
}
