package mwcompiler.ir.operands;

import mwcompiler.ir.tools.NameBuilder;

public class StringLiteral {
    private String val;
    private String label;

    public StringLiteral(String val) {
        label = NameBuilder.builder("__str_literal_");
        this.val = val;
    }

}
