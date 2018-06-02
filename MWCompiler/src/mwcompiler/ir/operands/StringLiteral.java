package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.ir.tools.NameBuilder;

import java.util.Map;
import java.util.StringJoiner;

public class StringLiteral extends Literal {
    private String val;
    private String label;


    public StringLiteral(String val) {
        label = NameBuilder.builder("__str_literal_");

        this.val = val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public String getLabel() {
        return label;
    }

    public String stringVal() {
        return val;
    }

    public String hexVal() {
        String val = this.val;
        val = val.replaceAll("\\\\" + "n", "\n");
        val = val.replaceAll("\\\\" + "t", "\t");
        val = val.replaceAll("\\\\" + "\"", "\"");
        val = val.replaceAll("\\\\" + "\'", "\'");
        val = val.replaceAll("\\\\" + "\\\\", "\\\\");
        StringJoiner str = new StringJoiner(", ");
        for (byte b : val.getBytes()) {
            str.add(String.format("%02X", b) + "H");
        }
        str.add("00H");
        return str.toString();
    }

    public static final StringLiteral stringlnFormat = new StringLiteral("%s\n");
    public static final StringLiteral stringFormat = new StringLiteral("%s");
    public static final StringLiteral intlnFormat = new StringLiteral("%ld\n");
    public static final StringLiteral intFormat = new StringLiteral("%ld");


    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringLiteral && ((StringLiteral) obj).val.equals(val);
    }

}
