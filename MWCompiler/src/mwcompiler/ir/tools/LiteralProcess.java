package mwcompiler.ir.tools;

import mwcompiler.ir.nodes.ProgramIR;
import mwcompiler.ir.operands.IntLiteral;
import mwcompiler.ir.operands.Literal;
import mwcompiler.ir.operands.Operand;
import mwcompiler.ir.operands.StringLiteral;
import mwcompiler.utility.ExprOps;

import static mwcompiler.ir.operands.IntLiteral.ONE_LITERAL;
import static mwcompiler.ir.operands.IntLiteral.ZERO_LITERAL;

public class LiteralProcess {
    public static ProgramIR programIR;
    public static Literal calc(Operand left, ExprOps op, Operand right) {
        if (left instanceof IntLiteral) {
            int x = ((IntLiteral) left).val();
            int y = ((IntLiteral) right).val();
            switch (op) {
                case ADD: return new IntLiteral(x + y);
                case SUB: return new IntLiteral(x - y);
                case MUL: return new IntLiteral(x * y);
                case DIV: return new IntLiteral(x / y);
                case MOD: return new IntLiteral(x % y);
                case EQ: return x == y ? ONE_LITERAL : ZERO_LITERAL;
                case NEQ: return x != y ? ONE_LITERAL : ZERO_LITERAL;
                case GTE: return x >= y ? ONE_LITERAL : ZERO_LITERAL;
                case GT: return x > y ? ONE_LITERAL : ZERO_LITERAL;
                case LTE: return x <= y ? ONE_LITERAL : ZERO_LITERAL;
                case LT: return x < y ? ONE_LITERAL : ZERO_LITERAL;
                case OR: case BITOR: return new IntLiteral(x | y);
                case AND: case BITAND: return new IntLiteral(x & y);
                case BITXOR: return new IntLiteral(x ^ y);
                case LSFT: return new IntLiteral(x << y);
                case RSFT: return new IntLiteral(x >> y);
                default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for IntLiteral");
            }
        } else {
            String x = ((StringLiteral) left).stringVal();
            String y = ((StringLiteral) right).stringVal();
            switch (op) {
                case ADD: return stringLiteralBuilder(x + y);
                case EQ: return x.equals(y) ? ONE_LITERAL : ZERO_LITERAL;
                case NEQ: return !x.equals(y) ? ONE_LITERAL : ZERO_LITERAL;
                case GTE: return x.compareTo(y) >= 0 ? ONE_LITERAL : ZERO_LITERAL;
                case GT: return x.compareTo(y) > 0 ? ONE_LITERAL : ZERO_LITERAL;
                case LTE: return x.compareTo(y) <= 0 ? ONE_LITERAL : ZERO_LITERAL;
                case LT: return x.compareTo(y) < 0 ? ONE_LITERAL : ZERO_LITERAL;
                default: throw new RuntimeException("Compiler Bug: (IR building) Undefined operation for StringLiteral");
            }
        }
    }
    public static StringLiteral stringLiteralBuilder(String val) {
        StringLiteral search = programIR.getStringLiteral(val);
        if (search == null) {
            search = new StringLiteral(val);
            programIR.putStringLiteral(val, search);
        }
        return search;
    }
}
