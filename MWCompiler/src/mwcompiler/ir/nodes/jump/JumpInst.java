package mwcompiler.ir.nodes.jump;

import mwcompiler.ir.nodes.Instruction;
import mwcompiler.ir.operands.Var;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JumpInst extends Instruction {
    @Override
    public List<Var> dstVar() {
        return new ArrayList<>();
    }
}
