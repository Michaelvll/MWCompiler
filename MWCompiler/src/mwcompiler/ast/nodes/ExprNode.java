package mwcompiler.ast.nodes;

import mwcompiler.utility.Location;

/**
 * ExprNode.java
 * Expression extends from Node
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public abstract class ExprNode extends Node {
    private Location location;

    ExprNode(Location pos) {
        location = pos;
    }

    public enum OPs {
        // Binary
        ADD, SUB,// int string
        MUL, DIV, MOD,//int
        LSFT, RSFT,// int
        LT, GT, LTE, GTE,// int string
        EQ, NEQ,// class int string bool
        BITAND, BITXOR, BITOR, // int
        AND, OR, // bool
        ASSIGN, // all
        // Unary
        // ADD, SUB
        INC_SUFF, DEC_SUFF, //int
        INC, DEC, //int
        NOT,//bool
        BITNOT,//int
        // NEW
        NEW
    }


    @Override
    public Location getStartLocation() {
        return location;
    }

}
