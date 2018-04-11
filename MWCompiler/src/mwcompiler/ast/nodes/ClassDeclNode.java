package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.AstVisitor;
import mwcompiler.ast.tools.Location;

import java.util.List;

/**
 * ClassDeclNode.java
 * Class declaration node extends from DeclaratorNode
 *
 * @author Michael Wu
 * @since 2018-04-11
 * */
public class ClassDeclNode extends DeclaratorNode {
    private String name;
    private List<DeclaratorNode> body;
    private Location namePos;
    private Location bodyPos;

    public ClassDeclNode(String name, List<DeclaratorNode> body, Location namePos, Location bodyPos) {
        this.name = name;
        this.body = body;
        this.namePos = namePos;
        this.bodyPos = bodyPos;
    }


    @Override
    public void accept(AstVisitor visitor) {
        visitor.visit(this);
    }

    public String getName() {
        return name;
    }

    public List<DeclaratorNode> getBody() {
        return body;
    }

    public Location getNamePos() {
        return namePos;
    }

    public Location getBodyPos() {
        return bodyPos;
    }
}
