package mwcompiler.ast.nodes;

import mwcompiler.ast.tools.Location;

/**
 * ExprNode.java
 * Expression extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public abstract class ExprNode extends Node {
    //TODO

    private Location location;

    ExprNode(Location pos) {
        location = pos;
    }

    public enum OPs {
        // Binary
        ADD, SUB,
        MUL, DIV, MOD,
        LSFT, RSFT,
        LT, GT, LTE, GTE,
        EQ, NEQ,
        BITAND, BITXOR, BITOR,
        AND, OR, ASSIGN,
        // Unary
        // ADD, SUB
        INC_SUFF,
        DEC_SUFF,
        INC, DEC,
        NOT, BITNOT,
        // NEW
        NEW
    }

    @Override
    public Location getStartLocation() {
        return location;
    }
}
