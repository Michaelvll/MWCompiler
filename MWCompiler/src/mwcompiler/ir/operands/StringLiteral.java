package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.ir.tools.NameBuilder;

public class StringLiteral extends Literal {
    private String val;
    private String label;


    public StringLiteral(String val) {
        label = NameBuilder.builder("__str_literal_");
        val = val.replaceFirst("^\"", "");
        val = val.replaceFirst("\"$", "");
        this.val = val;
    }

    @Override
    public <T> T accept(IRVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getLabel() {
        return label;
    }

    public String getVal() {
        return "`" + val + "\\0`";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringLiteral && ((StringLiteral) obj).val.equals(val);
    }

}
