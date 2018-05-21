package mwcompiler.ir.operands;

import mwcompiler.ir.tools.IRVisitor;
import mwcompiler.ir.tools.NameBuilder;

import java.util.HashMap;
import java.util.Map;

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

    public String getVal() {
        return val;
    }
}
