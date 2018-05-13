package mwcompiler.utility;

public enum ExprOps {
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
    NEW;

}
