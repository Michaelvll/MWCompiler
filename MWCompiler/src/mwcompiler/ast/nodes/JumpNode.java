package mwcompiler.ast.nodes;


import mwcompiler.ast.tools.Location;

public abstract class JumpNode extends Node {
    Location location;

    JumpNode(Location location) {
        this.location = location;
    }

    @Override
    public Location getStartLocation() {
        return location;
    }
}
