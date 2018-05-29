package mwcompiler.utility;

import mwcompiler.ir.operands.Register;

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

    public ExprOps exchange() {
        switch (nasmOp) {
            case "l": return GT;
            case "g" :return LT;
            case "le":return GTE;
            case "ge": return LTE;
            case "e": return EQ;
            case "ne": return NEQ;
            default: throw new RuntimeException("???not "+ nasmOp);
        }
    }

    public ExprOps not() {
        switch (nasmOp) {
            case "l": return GTE;
            case "g": return LTE;
            case "le": return GT;
            case "ge": return LT;
            case "e": return NEQ;
            case "ne": return EQ;
            default: throw new RuntimeException("???not "+ nasmOp);
        }
    }
}
