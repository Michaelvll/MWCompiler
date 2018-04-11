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

    protected Location location;

    public enum OPs {
        // Binary
        ADD, SUB,
        MUL, DIV, MOD,
        LSFT, RSFT,
        LT, GT, LTE, GTE,
        EQ, NEQ,
        BITAND, BITOR,
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

    public Location getLocation() {
        return location;
    }
}
