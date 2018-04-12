package mwcompiler.ast.tools;

import mwcompiler.ast.nodes.*;
import mwcompiler.symbols.SymbolTable;
import mx_gram.tools.*;

import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * BuildAstVisitor.java
 * Class for building the AST, by extending from the MxBase Visitor generated by antlr.v4
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class BuildAstVisitor extends MxBaseVisitor<Node> {
    private SymbolTable currentSymbolTable = null;

    @Override
    public Node visitProgram(MxParser.ProgramContext ctx) {
        List<Node> declarators = new ArrayList<>();
        Location programPos = new Location(1,1);
        for (ParseTree child : ctx.declarator()) {
            Node childNode = visit(child);
            if (childNode instanceof DeclaratorNode)
                declarators.add(childNode);
            else
                throw new RuntimeException("ERROR: (Building AST) Get unexpected statement when visiting the global scope "+programPos.getLocation());
        }
        BlockNode block = new BlockNode(declarators, programPos, currentSymbolTable);
        currentSymbolTable = block.getCurrentSymbolTable();
        return new ProgramNode(block);
    }

    // Variable declaration
    @Override
    public Node visitVariableDeclField(MxParser.VariableDeclFieldContext ctx) {
        VariableDeclNode node = (VariableDeclNode) visit(ctx.variableField());
        node.setType((TypeNode) visit(ctx.type()), new Location(ctx.type()));
        return node;
    }

    @Override
    public Node visitVariableField(MxParser.VariableFieldContext ctx) {
        String var = ctx.Identifier().getText();
        Location varPos = new Location(ctx.Identifier());
        ExprNode init = null;
        Location initPos = null;
        if (ctx.variableInitializer() != null) {
            init = (ExprNode) this.visit(ctx.variableInitializer());
            initPos = new Location(ctx.variableInitializer());
        }
        return new VariableDeclNode(null, var, init, null, varPos, initPos);
    }

    @Override
    public Node visitVariableInitializer(MxParser.VariableInitializerContext ctx) {
        return this.visit(ctx.expr());
    }

    // Function declaration
    private FunctionDeclNode getFunctionField(TerminalNode identifier,
                                              MxParser.ParamExprFieldContext paramExprFieldContext, MxParser.FunctionBodyContext functionBodyContext) {
        String name = identifier.getText();
        List<VariableDeclNode> params = new ArrayList<>();
        for (MxParser.ParamExprContext param : paramExprFieldContext.paramExpr()) {
            params.add((VariableDeclNode) visit(param));
        }
        BlockNode body = (BlockNode) visit(functionBodyContext);
        return new FunctionDeclNode(null, name, params, body, null, new Location(identifier),
                new Location(paramExprFieldContext), new Location(functionBodyContext));
    }

    @Override
    public Node visitTypeFunction_(MxParser.TypeFunction_Context ctx) {
        TypeNode returnType = (TypeNode) visit(ctx.type());
        FunctionDeclNode node = (FunctionDeclNode) visit(ctx.functionField());
        node.setReturnType(returnType, new Location(ctx.type()));
        return node;
    }

    @Override
    public Node visitVoidFunction_(MxParser.VoidFunction_Context ctx) {
        TypeNode returnType = new VoidTypeNode();
        FunctionDeclNode node = (FunctionDeclNode) visit(ctx.functionField());
        node.setReturnType(returnType, new Location(ctx.VOID()));
        return node;
    }

    @Override
    public Node visitFunctionField(MxParser.FunctionFieldContext ctx) {
        return getFunctionField(ctx.Identifier(), ctx.paramExprField(), ctx.functionBody());
    }

    @Override
    public Node visitParamExpr(MxParser.ParamExprContext ctx) {
        TypeNode type = (TypeNode) visit(ctx.type());
        String name = ctx.Identifier().getText();
        return new VariableDeclNode(type, name, null, new Location(ctx.type()), new Location(ctx.Identifier()), null);
    }

    @Override
    public Node visitFunctionBody(MxParser.FunctionBodyContext ctx) {
        return visit(ctx.block());
    }

    // Class declaration

    @Override
    public Node visitClassDeclField(MxParser.ClassDeclFieldContext ctx) {
        return visit(ctx.classField());
    }

    @Override
    public Node visitClassField(MxParser.ClassFieldContext ctx) {
        String declClass = ctx.Identifier().getText();
        List<Node> body = new ArrayList<>();
        Location declClassPos = new Location(ctx.Identifier());
        Location bodyPos = new Location(ctx.LBRACE());
        for (MxParser.ClassBodyContext declarator : ctx.classBody()) {
            Node statement = visit(declarator);
            if (statement instanceof VariableDeclNode || statement instanceof FunctionDeclNode) {
                body.add(visit(declarator));
            } else {
                throw new RuntimeException("ERROR: (Building AST) Unexpected statement found in Class declaration " + declClassPos.getLocation());
            }
        }
        BlockNode block = new BlockNode(body, bodyPos, currentSymbolTable);
        currentSymbolTable = block.getCurrentSymbolTable();
        return new ClassDeclNode(declClass, block, declClassPos);
    }

    @Override
    public Node visitClassConstructField(MxParser.ClassConstructFieldContext ctx) {
        FunctionDeclNode node = getFunctionField(ctx.Identifier(), ctx.paramExprField(), ctx.functionBody());
        node.setReturnType(new NullTypeNode(), null);
        return node;
    }

    // Block
    @Override
    public Node visitBlock(MxParser.BlockContext ctx) {
        List<Node> statements = new ArrayList<>();
        for (MxParser.StatementContext state : ctx.statement()) {
            statements.add(visit(state));
        }
        BlockNode block =  new BlockNode(statements, new Location(ctx),currentSymbolTable);
        currentSymbolTable = block.getCurrentSymbolTable();
        return block;
    }

    // TypeSymbol
    @Override
    public Node visitType(MxParser.TypeContext ctx) {
        String type = ctx.nonArrayType().getText();
        if (ctx.LBRACK().size() != 0) {
            return new ArrayTypeNode(type, ctx.LBRACK().size());
        } else {
            return new NonArrayTypeNode(ctx.getText());
        }
    }

    // Expression

    @Override
    public Node visitExprField(MxParser.ExprFieldContext ctx) {
        if (ctx.expr() != null) {
            return visit(ctx.expr());
        } else {
            return new EmptyExprNode(new Location(ctx));
        }
    }

    @Override
    public Node visitLiteral(MxParser.LiteralContext ctx) {
        String val = ctx.getText();
        LiteralExprNode node;
        Location literalPos = new Location(ctx.literalType);
        switch (ctx.literalType.getType()) {
            case MxParser.BoolLiteral:
                node = new BoolLiteralNode(val, literalPos);
                break;
            case MxParser.IntLiteral:
                node = new IntLiteralNode(val, literalPos);
                break;
            case MxParser.StringLiteral:
                node = new StringLiteralNode(val, literalPos);
                break;
            case MxParser.NULL:
                node = new NullLiteralNode(literalPos);
                break;
            default:
                throw new RuntimeException("ERROR: (Building AST) Get unexpected literal type when visiting literal " + literalPos.getLocation());
        }
        return node;
    }

    @Override
    public Node visitIdentifier_(MxParser.Identifier_Context ctx) {
        return new IdentifierExprNode(ctx.getText(), new Location(ctx));
    }

    // Binary Expression
    @Override
    public Node visitBinaryExpr_(MxParser.BinaryExpr_Context ctx) {
        ExprNode.OPs op;
        Location opPos = new Location(ctx.op);
        switch (ctx.op.getType()) {
            case MxParser.MUL:
                op = ExprNode.OPs.MUL;
                break;
            case MxParser.DIV:
                op = ExprNode.OPs.DIV;
                break;
            case MxParser.MOD:
                op = ExprNode.OPs.MOD;
                break;
            case MxParser.ADD:
                op = ExprNode.OPs.ADD;
                break;
            case MxParser.SUB:
                op = ExprNode.OPs.SUB;
                break;
            case MxParser.LSFT:
                op = ExprNode.OPs.LSFT;
                break;
            case MxParser.RSFT:
                op = ExprNode.OPs.RSFT;
                break;
            case MxParser.LT:
                op = ExprNode.OPs.LT;
                break;
            case MxParser.GT:
                op = ExprNode.OPs.GT;
                break;
            case MxParser.LTE:
                op = ExprNode.OPs.LTE;
                break;
            case MxParser.GTE:
                op = ExprNode.OPs.GTE;
                break;
            case MxParser.EQ:
                op = ExprNode.OPs.EQ;
                break;
            case MxParser.NEQ:
                op = ExprNode.OPs.NEQ;
                break;
            case MxParser.BITAND:
                op = ExprNode.OPs.BITAND;
                break;
            case MxParser.BITXOR:
                op = ExprNode.OPs.BITXOR;
                break;
            case MxParser.BITOR:
                op = ExprNode.OPs.BITOR;
                break;
            case MxParser.AND:
                op = ExprNode.OPs.AND;
                break;
            case MxParser.OR:
                op = ExprNode.OPs.OR;
                break;
            case MxParser.ASSIGN:
                op = ExprNode.OPs.ASSIGN;
                break;
            default:
                throw new RuntimeException("ERROR: (Building AST) Get unexpected operator at BinaryExpression " + opPos.getLocation());
        }
        return new BinaryExprNode((ExprNode) this.visit(ctx.expr(0)),
                op, (ExprNode) this.visit(ctx.expr(1)), opPos);
    }

    // Unary Expression
    @Override
    public Node visitSuffixIncDec_(MxParser.SuffixIncDec_Context ctx) {
        ExprNode node = (ExprNode) visit(ctx.expr());
        ExprNode.OPs op;
        Location opPos = new Location(ctx.op);

        switch (ctx.op.getType()) {
            case MxParser.INC:
                op = ExprNode.OPs.INC_SUFF;
                break;
            case MxParser.DEC:
                op = ExprNode.OPs.DEC_SUFF;
                break;
            default:
                throw new RuntimeException("ERROR: (Building AST) Get unexpected op at SuffixIncDec " + opPos.getLocation());

        }
        return new UnaryExprNode(op, node, new Location(ctx.op));
    }

    @Override
    public Node visitUnaryExpr_(MxParser.UnaryExpr_Context ctx) {
        ExprNode.OPs op;
        Location opPos = new Location(ctx.op);
        switch (ctx.op.getType()) {
            case MxParser.INC:
                op = ExprNode.OPs.INC;
                break;
            case MxParser.DEC:
                op = ExprNode.OPs.DEC;
                break;
            case MxParser.ADD:
                op = ExprNode.OPs.ADD;
                break;
            case MxParser.SUB:
                op = ExprNode.OPs.SUB;
                break;
            case MxParser.NOT:
                op = ExprNode.OPs.NOT;
                break;
            case MxParser.BITNOT:
                op = ExprNode.OPs.BITNOT;
                break;
            default:
                throw new RuntimeException("ERROR: (Building AST) Get unexpected operator" + ctx.op.getText() + " in unary expression " + opPos.getLocation());
        }
        return new UnaryExprNode(op, (ExprNode) visit(ctx.expr()), opPos);
    }

    // New
    @Override
    public Node visitNewCreator_(MxParser.NewCreator_Context ctx) {
        return visit(ctx.creator());
    }

    @Override
    public Node visitCreator(MxParser.CreatorContext ctx) {
        String createType = ctx.createdName().getText();
        Integer dim = 0;
        List<ExprNode> dimArgs = new ArrayList<>();
        if (ctx.arrayCreatorRest() != null) {
            dim = ctx.arrayCreatorRest().LBRACK().size();
            for (MxParser.ExprContext exprs : ctx.arrayCreatorRest().expr()) {
                dimArgs.add((ExprNode) visit(exprs));
            }
        }
        return new NewExprNode(createType, dim, dimArgs, new Location(ctx.createdName()));
    }

    // Function Call

    @Override
    public Node visitFunctionCall_(MxParser.FunctionCall_Context ctx) {
        ExprNode caller = (ExprNode) visit(ctx.expr());
        List<ExprNode> args = new ArrayList<>();
        if (ctx.arguments().exprList() != null) {
            for (MxParser.ExprContext expr : ctx.arguments().exprList().expr()) {
                args.add((ExprNode) visit(expr));
            }
        }
        return new FunctionCallNode(caller, args, new Location(ctx.arguments()));
    }

    // Member Function call

    @Override
    public Node visitDotMember_(MxParser.DotMember_Context ctx) {
        ExprNode mom = (ExprNode) visit(ctx.expr());
        String member = ctx.Identifier().getText();
        return new DotMemberNode(mom, member, new Location(ctx.DOT()));
    }

    @Override
    public Node visitBrackMember_(MxParser.BrackMember_Context ctx) {
        ExprNode mom = (ExprNode) visit(ctx.mom);
        ExprNode subscript = (ExprNode) visit(ctx.subscript);
        return new BrackMemberNode(mom, subscript, new Location(ctx.LBRACK()));
    }

    @Override
    public Node visitThis_(MxParser.This_Context ctx) {
        return new IdentifierExprNode("this", new Location(ctx.THIS()));
    }

    @Override
    public Node visitParenExpr_(MxParser.ParenExpr_Context ctx) {
        return visit(ctx.expr());
    }

    // If statement
    @Override
    public Node visitConditionField(MxParser.ConditionFieldContext ctx) {
        ExprNode condition = (ExprNode) visit(ctx.cond);
        BlockNode body = (BlockNode) visit(ctx.body());
        IfNode ifNode = new IfNode(condition, body, new Location(ctx.IF()));
        IfNode prevCond = ifNode;
        if (ctx.elseifConditionField().size() != 0) {
            for (MxParser.ElseifConditionFieldContext field : ctx.elseifConditionField()) {
                IfNode nextCond = (IfNode) visit(field);
                prevCond.setElseCondition(nextCond);
                prevCond = nextCond;
            }
        }
        if (ctx.elseConditionField() != null) {
            prevCond.setElseCondition((IfNode) visit(ctx.elseConditionField()));
        }
        return ifNode;
    }

    @Override
    public Node visitElseifConditionField(MxParser.ElseifConditionFieldContext ctx) {
        return new IfNode((ExprNode) visit(ctx.cond), (BlockNode) visit(ctx.body()), new Location(ctx.ELSE()));
    }

    @Override
    public Node visitElseConditionField(MxParser.ElseConditionFieldContext ctx) {
        return new IfNode(null, (BlockNode) visit(ctx.body()), new Location(ctx.ELSE()));
    }

    // Loop statement
    @Override
    public Node visitForField(MxParser.ForFieldContext ctx) {
        ExprNode vardecl = null;
        ExprNode condition = null;
        ExprNode step = null;
        BlockNode body = (BlockNode) visit(ctx.body());
        Location vardeclPos = null;
        Location conditionPos = null;
        Location stepPos = null;
        if (ctx.vardecl != null) {
            vardecl = (ExprNode) visit(ctx.vardecl);
            vardeclPos = new Location(ctx.vardecl);
        }
        if (ctx.cond != null) {
            condition = (ExprNode) visit(ctx.cond);
            conditionPos = new Location(ctx.cond);
        }
        if (ctx.step != null) {
            step = (ExprNode) visit(ctx.step);
            stepPos = new Location(ctx.step);
        }
        return new LoopNode(vardecl, condition, step, body, vardeclPos, conditionPos, stepPos);
    }

    @Override
    public Node visitWhileField(MxParser.WhileFieldContext ctx) {
        ExprNode condition = null;
        Location condPos = null;
        BlockNode body = (BlockNode) visit(ctx.body());
        if (ctx.cond != null) {
            condition = (ExprNode) visit(ctx.cond);
            condPos = new Location(ctx.cond);
        }
        return new LoopNode(null, condition, null, body, null,
                condPos, null);
    }

    @Override
    public Node visitBody(MxParser.BodyContext ctx) {
        Node node = visit(ctx.statement());
        if (node instanceof BlockNode) {
            return node;
        } else {
            List<Node> statements = new ArrayList<>();
            statements.add(node);
            BlockNode block= new BlockNode(statements, new Location(ctx), currentSymbolTable);
            currentSymbolTable = block.getCurrentSymbolTable();
            return block;
        }
    }

    @Override
    public Node visitJumpField(MxParser.JumpFieldContext ctx) {
        return visit(ctx.jump());
    }

    @Override
    public Node visitReturnJump_(MxParser.ReturnJump_Context ctx) {
        return new ReturnNode((ExprNode) visit(ctx.expr()));
    }

    @Override
    public Node visitBreakJump_(MxParser.BreakJump_Context ctx) {
        return new BreakNode();
    }

    @Override
    public Node visitContinueJump_(MxParser.ContinueJump_Context ctx) {
        return new ContinueNode();
    }

    //TODO
}
