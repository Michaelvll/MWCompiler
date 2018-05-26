package mwcompiler.utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ExprOps {
    // Binary
    ADD("add"), SUB("sub"),// int string
    MUL("imul"), DIV("div"), MOD("div"),//int
    LSFT("shl"), RSFT("shr"),// int
    LT("l"), GT("g"), LTE("le"), GTE("ge"),// int string
    EQ("e"), NEQ("ne"),// class int string bool
    BITAND("and"), BITXOR("xor"), BITOR("or"), // int
    AND("???and"), OR("???or"), // bool
    ASSIGN("mov"), // all
    // Unary
    // ADD("// add"), SUB
    INC_SUFF("???inc_suf"), DEC_SUFF("???dec_suf"), //int
    INC("???inc"), DEC("???dec"), //int
    NOT("not"),//bool
    BITNOT("not");//int


    private static final Set<ExprOps> compOP = new HashSet<>(Arrays.asList(LT, GT, LTE, GTE, EQ, NEQ));

    private String nasmOp;
    private ExprOps(String nasmOp) {
        this.nasmOp = nasmOp;
    }

    public String nasmOp() {
        return nasmOp;
    }

    public boolean isCompare() {
        return compOP.contains(this);
    }
}
