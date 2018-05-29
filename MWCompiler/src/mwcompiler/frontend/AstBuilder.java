package mwcompiler.frontend;

import mwcompiler.ast.nodes.*;
import mwcompiler.ast.nodes.declarations.ClassDeclNode;
import mwcompiler.ast.nodes.declarations.DeclarationNode;
import mwcompiler.ast.nodes.declarations.FunctionDeclNode;
import mwcompiler.ast.nodes.declarations.VariableDeclNode;
import mwcompiler.ast.nodes.expressions.*;
import mwcompiler.ast.nodes.literals.*;
import mwcompiler.symbols.Instance;
import mwcompiler.symbols.BaseTypeSymbol;
import mwcompiler.symbols.TypeSymbol;
import mwcompiler.utility.CompileError;
import mwcompiler.utility.ExprOps;
import mwcompiler.utility.Location;
import mwcompiler.utility.StringProcess;
import mx_gram.tools.MxBaseVisitor;
import mx_gram.tools.MxParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * AstBuilder.java
 * Class for building the AST, by extending from the MxBase Visitor generated by antlr.v4
 *
 * @author Michael Wu
 * @since 2018-04-06
 */
public class AstBuilder extends MxBaseVisitor<Node> {
    private String stage = "Building Ast";

    public ProgramNode build(MxParser.ProgramContext ctx) {
        return (ProgramNode) visit(ctx);
    }

    private void buildClassSymbol(MxParser.ProgramContext ctx) {
        for (ParseTree child : ctx.declarator()) {
            if (child.getChild(0) instanceof MxParser.ClassDeclFieldContext) {
                BaseTypeSymbol.builder(
                        ((MxParser.ClassDeclFieldContext) child.getChild(0)).classField().Identifier().getText());
            }
        }
    }


    @Override
    public Node visitProgram(MxParser.ProgramContext ctx) {
        List<Node> declarators = new ArrayList<>();
        Location programPos = new Location(ctx);
        buildClassSymbol(ctx);
        for (ParseTree child : ctx.declarator()) {
            Node childNode = visit(child);
            if (childNode instanceof DeclarationNode) {
                declarators.add(childNode);
                if (childNode instanceof VariableDeclNode) {
                    VariableDeclNode variableDeclNode = (VariableDeclNode) childNode;
                    TypeSymbol search = TypeSymbol.searchSymbol(variableDeclNode.getVarSymbol().getName());
                    if (search != null) {
                        throw new CompileError(stage, "Can not use the same name <" + StringProcess.GREEN
                                + variableDeclNode.getVarSymbol().getName() + "> for class and " +
                                "variable in same scope", variableDeclNode.location());
                    }
                }
            } else
                throw new CompileError(
                        stage, "Get unexpected statement when visiting the global scope", programPos);
        }
        BlockNode block = new BlockNode(declarators, programPos);
        return new ProgramNode(block, programPos);
    }

    // Variable declaration
    @Override
    public Node visitVariableDeclField(MxParser.VariableDeclFieldContext ctx) {
        VariableDeclNode node = (VariableDeclNode) visit(ctx.variableField());
        TypeNode typeNode = (TypeNode) visit(ctx.type());
        node.setType(typeNode.getTypeSymbol(), new Location(ctx.type()));
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
            initPos = new Location(ctx.ASSIGN());
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
        Location identifierLocation = new Location(identifier);
        Location paramLocation = new Location(paramExprFieldContext);
        Location functionBodyLocation = new Location(functionBodyContext);
        List<VariableDeclNode> params = new ArrayList<>();
        paramExprFieldContext.paramExpr().forEach(param -> params.add((VariableDeclNode) visit(param)));

        BlockNode body = (BlockNode) visit(functionBodyContext);
        String name = identifier.getText();
        // Creator Function
        TypeSymbol search = TypeSymbol.searchSymbol(name);
        if (search instanceof BaseTypeSymbol) {
            return new FunctionDeclNode(search, Instance.CONSTRUCTOR, params, body, null,
                    identifierLocation, paramLocation, functionBodyLocation);
        }

        Instance instance;
        try {
            instance = Instance.builder(name);
        } catch (RuntimeException e) {
            throw new CompileError(
                    stage, e.getMessage(), new Location(identifier));
        }

        return new FunctionDeclNode(null, instance, params, body, null, new Location(identifier),
                new Location(paramExprFieldContext), new Location(functionBodyContext));
    }


    @Override
    public Node visitTypeFunction_(MxParser.TypeFunction_Context ctx) {
        TypeNode returnTypeNode = (TypeNode) visit(ctx.type());
        FunctionDeclNode node = (FunctionDeclNode) visit(ctx.functionField());
        node.setReturnType(returnTypeNode.getTypeSymbol(), new Location(ctx.type()));
        return node;
    }

    @Override
    public Node visitVoidFunction_(MxParser.VoidFunction_Context ctx) {
        TypeSymbol returnType = BaseTypeSymbol.builder("void");
        FunctionDeclNode node = (FunctionDeclNode) visit(ctx.functionField());
        node.setReturnType(returnType, new Location(ctx.VOID()));
        return node;
    }

    @Override
    public Node visitClassConstructField(MxParser.ClassConstructFieldContext ctx) {
        FunctionDeclNode node = getFunctionField(ctx.Identifier(), ctx.paramExprField(), ctx.functionBody());
        return node;
    }

    @Override
    public Node visitFunctionField(MxParser.FunctionFieldContext ctx) {
        return getFunctionField(ctx.Identifier(), ctx.paramExprField(), ctx.functionBody());
    }

    @Override
    public Node visitParamExpr(MxParser.ParamExprContext ctx) {
        TypeNode typeNode = (TypeNode) visit(ctx.type());
        String name = ctx.Identifier().getText();
        return new VariableDeclNode(typeNode.getTypeSymbol(), name, null, new Location(ctx.type()),
                new Location(ctx.Identifier()), null);
    }

    @Override
    public Node visitFunctionBody(MxParser.FunctionBodyContext ctx) {
        return visit(ctx.block());
    }

    // Class declaration

    @Override
    public Node visitClassDeclField(MxParser.ClassDeclFieldContext ctx) {
        ClassDeclNode node = (ClassDeclNode) visit(ctx.classField());
        return node;
    }

    @Override
    public Node visitClassField(MxParser.ClassFieldContext ctx) {
        String declClass = ctx.Identifier().getText();
        List<Node> body = new ArrayList<>();
        Location declClassPos = new Location(ctx);
        Location bodyPos = new Location(ctx);
        for (MxParser.ClassBodyContext declarator : ctx.classBody()) {
            Node statement = visit(declarator);
            if (statement instanceof VariableDeclNode) {
                body.add(visit(declarator));

            } else if (statement instanceof FunctionDeclNode) {
                FunctionDeclNode functionDeclNode = (FunctionDeclNode) statement;
                body.add(visit(declarator));
                if (functionDeclNode.getInstance() == Instance.CONSTRUCTOR
                        && !(functionDeclNode.getFunctionSymbol().getReturnType().getName().equals(declClass))) {
                    throw new CompileError(stage, "Creator function must have the same name as the class"
                            , new Location(declarator));
                }
            } else {
                throw new CompileError(stage, "Unexpected statement found in Class declaration"
                        , declClassPos);
            }
        }
        BlockNode block = new BlockNode(body, bodyPos);
        ClassDeclNode classDeclNode;
        try {
            classDeclNode = new ClassDeclNode(declClass, block, declClassPos);
        } catch (CompileError e) {
            throw new CompileError(stage, e.getMessage(), declClassPos);
        }
        return classDeclNode;
    }

    // Block
    @Override
    public Node visitBlock(MxParser.BlockContext ctx) {
        List<Node> statements = new ArrayList<>();
        ctx.statement().forEach(state -> statements.add(visit(state)));
        return new BlockNode(statements, new Location(ctx));
    }

    // BaseTypeSymbol
    @Override
    public Node visitType(MxParser.TypeContext ctx) {
        String type = ctx.nonArrayType().getText();

        return new TypeNode(type, ctx.LBRACK().size(), new Location(ctx));
    }

    // Expressions
    @Override
    public Node visitExprField(MxParser.ExprFieldContext ctx) {
        if (ctx.expr() != null) {
            ExprNode node = (ExprNode) visit(ctx.expr());
            return node;
        }
        return new EmptyExprNode(new Location(ctx));
    }

    @Override
    public Node visitLiteral(MxParser.LiteralContext ctx) {
        String val = ctx.getText();
        LiteralExprNode node;
        Location literalPos = new Location(ctx);
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
                throw new CompileError(stage, "Get unexpected literal typename when visiting literal"
                        , literalPos);
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
        ExprOps op;
        Location opPos = new Location(ctx);
        switch (ctx.op.getType()) {
            case MxParser.MUL: op = ExprOps.MUL;
                break;
            case MxParser.DIV: op = ExprOps.DIV;
                break;
            case MxParser.MOD: op = ExprOps.MOD;
                break;
            case MxParser.ADD: op = ExprOps.ADD;
                break;
            case MxParser.SUB: op = ExprOps.SUB;
                break;
            case MxParser.LSFT: op = ExprOps.LSFT;
                break;
            case MxParser.RSFT: op = ExprOps.RSFT;
                break;
            case MxParser.LT: op = ExprOps.LT;
                break;
            case MxParser.GT: op = ExprOps.GT;
                break;
            case MxParser.LTE: op = ExprOps.LTE;
                break;
            case MxParser.GTE: op = ExprOps.GTE;
                break;
            case MxParser.EQ: op = ExprOps.EQ;
                break;
            case MxParser.NEQ: op = ExprOps.NEQ;
                break;
            case MxParser.BITAND: op = ExprOps.BITAND;
                break;
            case MxParser.BITXOR: op = ExprOps.BITXOR;
                break;
            case MxParser.BITOR: op = ExprOps.BITOR;
                break;
            case MxParser.AND: op = ExprOps.AND;
                break;
            case MxParser.OR: op = ExprOps.OR;
                break;
            case MxParser.ASSIGN: op = ExprOps.ASSIGN;
                break;
            default:
                throw new CompileError(
                        stage, "Get unexpected operator at BinaryExpression ", opPos);
        }
        return new BinaryExprNode((ExprNode) this.visit(ctx.expr(0)), op, (ExprNode) this.visit(ctx.expr(1)), opPos);
    }

    // Unary Expression
    @Override
    public Node visitSuffixIncDec_(MxParser.SuffixIncDec_Context ctx) {
        ExprNode node = (ExprNode) visit(ctx.expr());
        ExprOps op;
        Location opPos = new Location(ctx);

        switch (ctx.op.getType()) {
            case MxParser.INC: op = ExprOps.INC_SUFF;
                break;
            case MxParser.DEC: op = ExprOps.DEC_SUFF;
                break;
            default:
                throw new CompileError(
                        stage, "Get unexpected op at SuffixIncDec", opPos);

        }
        return new UnaryExprNode(op, node, new Location(ctx));
    }

    @Override
    public Node visitUnaryExpr_(MxParser.UnaryExpr_Context ctx) {
        ExprOps op;
        Location opPos = new Location(ctx);
        switch (ctx.op.getType()) {
            case MxParser.INC: op = ExprOps.INC;
                break;
            case MxParser.DEC: op = ExprOps.DEC;
                break;
            case MxParser.ADD: op = ExprOps.ADD;
                break;
            case MxParser.SUB: op = ExprOps.SUB;
                break;
            case MxParser.NOT: op = ExprOps.NOT;
                break;
            case MxParser.BITNOT: op = ExprOps.BITNOT;
                break;
            default:
                throw new CompileError(stage, "Get unexpected operator" + ctx.op.getText()
                        + " in unary expression", opPos);
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
        int dim = 0;
        List<ExprNode> dimArgs = new ArrayList<>();
        if (ctx.arrayCreatorRest() != null) {
            List<MxParser.CreatorInnerContext> creatorInnerContext = ctx.arrayCreatorRest().creatorInner();
            dim = creatorInnerContext.size();
            for (int index = 0; index < dim; ++index) {
                MxParser.ExprContext exprContext = creatorInnerContext.get(index).expr();
                if (exprContext != null) {
                    if (index != dimArgs.size()) {
                        throw new CompileError(
                                stage, "Syntax Expect a ']'," + " but a illegal expression start",
                                new Location(creatorInnerContext.get(index)));
                    }
                    dimArgs.add((ExprNode) visit(exprContext));
                }
            }
        }
        return new NewExprNode(createType, dim, dimArgs, new Location(ctx));
    }

    // Function Call
    @Override
    public Node visitFunctionCall_(MxParser.FunctionCall_Context ctx) {
        List<ExprNode> args = new ArrayList<>();
        if (ctx.arguments().exprList() != null) {
            ctx.arguments().exprList().expr().forEach(expr -> args.add((ExprNode) visit(expr)));
        }
        // For constructor
        if (ctx.expr() instanceof MxParser.Identifier_Context) {
            TypeSymbol symbol = TypeSymbol.searchSymbol(((MxParser.Identifier_Context) ctx.expr()).Identifier().getText());
            if (symbol instanceof BaseTypeSymbol) {
                return new ConstructorCallNode((BaseTypeSymbol) symbol, args, new Location(ctx));
            }
        }

        // For normal function
        ExprNode caller = (ExprNode) visit(ctx.expr());
        return new FunctionCallNode(caller, args, new Location(ctx));
    }

    // Member Function call
    @Override
    public Node visitDotMember_(MxParser.DotMember_Context ctx) {
        ExprNode container = (ExprNode) visit(ctx.expr());
        IdentifierExprNode member = new IdentifierExprNode(ctx.Identifier().getText(), new Location(ctx));
        return new DotMemberNode(container, member, new Location(ctx));
    }

    @Override
    public Node visitBrackMember_(MxParser.BrackMember_Context ctx) {
        ExprNode mom = (ExprNode) visit(ctx.mom);
        ExprNode subscript = (ExprNode) visit(ctx.subscript);
        return new BrackMemberNode(mom, subscript, new Location(ctx));
    }

    @Override
    public Node visitThis_(MxParser.This_Context ctx) {
        return new IdentifierExprNode("this", new Location(ctx));
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
        IfNode ifNode = new IfNode(condition, body, new Location(ctx.cond));
        IfNode prevCond = ifNode;
        if (ctx.elseifConditionField().size() != 0) {
            for (MxParser.ElseifConditionFieldContext field : ctx.elseifConditionField()) {
                IfNode nextCond = (IfNode) visit(field);
                prevCond.setElseNode(nextCond);
                prevCond = nextCond;
            }
        }
        if (ctx.elseConditionField() != null) {
            prevCond.setElseNode((IfNode) visit(ctx.elseConditionField()));
        }
        return ifNode;
    }

    @Override
    public Node visitElseifConditionField(MxParser.ElseifConditionFieldContext ctx) {
        return new IfNode((ExprNode) visit(ctx.cond), (BlockNode) visit(ctx.body()), new Location(ctx.cond));
    }

    @Override
    public Node visitElseConditionField(MxParser.ElseConditionFieldContext ctx) {
        return new IfNode(null, (BlockNode) visit(ctx.body()), new Location(ctx));
    }

    // Loop statement
    @Override
    public Node visitForField(MxParser.ForFieldContext ctx) {
        BinaryExprNode vardecl = null;
        ExprNode condition = null;
        ExprNode step = null;
        BlockNode body = (BlockNode) visit(ctx.body());
        Location vardeclPos = null;
        Location conditionPos = null;
        Location stepPos = null;

        if (ctx.init != null) {
            VariableDeclNode variableDeclNode = (VariableDeclNode) visit(ctx.variableField());
            vardecl = new BinaryExprNode(
                    new IdentifierExprNode(variableDeclNode.getVarSymbol(), new Location(ctx)),
                    ExprOps.ASSIGN, variableDeclNode.init(), new Location(ctx));
        }

        if (ctx.cond != null) {
            condition = (ExprNode) visit(ctx.cond);
            conditionPos = new Location(ctx.cond);
        }
        if (ctx.step != null) {
            step = (ExprNode) visit(ctx.step);
            stepPos = new Location(ctx.step);
        }
        return new LoopNode(vardecl, condition, step, body, new Location(ctx), vardeclPos, conditionPos, stepPos);
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
        return new LoopNode(null, condition, null, body, new Location(ctx), null, condPos, null);
    }

    @Override
    public Node visitBody(MxParser.BodyContext ctx) {
        Node node = visit(ctx.statement());
        if (node instanceof BlockNode) {
            return node;
        } else {
            List<Node> statements = new ArrayList<>();
            statements.add(node);
            return new BlockNode(statements, new Location(ctx));
        }
    }

    @Override
    public Node visitJumpField(MxParser.JumpFieldContext ctx) {
        ExprNode node = (ExprNode) visit(ctx.jump());
        return node;
    }

    @Override
    public Node visitReturnJump_(MxParser.ReturnJump_Context ctx) {
        ExprNode node = null;
        if (ctx.expr() != null)
            node = (ExprNode) visit(ctx.expr());
        return new ReturnNode(node, new Location(ctx));
    }

    @Override
    public Node visitBreakJump_(MxParser.BreakJump_Context ctx) {
        return new BreakNode(new Location(ctx));
    }

    @Override
    public Node visitContinueJump_(MxParser.ContinueJump_Context ctx) {
        return new ContinueNode(new Location(ctx));
    }

}
