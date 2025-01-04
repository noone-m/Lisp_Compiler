import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

// Custom ASTBuilder class extending LispBaseVisitor
public class ASTBuilder extends LispBaseVisitor<ASTNode> {

    @Override
    public ASTNode visitProgram(LispParser.ProgramContext ctx) {
        ASTNode node = new ASTNode("Program");
        for (LispParser.ExpressionContext exprCtx : ctx.expression()) {
            ASTNode expressionNode = visit(exprCtx); // Recursively visit expressions
            if(expressionNode != null){
                node.addChild(expressionNode);
            }
        }
        return node;
    }

    @Override
    public ASTNode visitExpression(LispParser.ExpressionContext ctx) {
        if (ctx.NUMBER() != null) {
            return new ASTNode(ctx.NUMBER().getText());
        } else if (ctx.SYMBOL() != null) {
            return new ASTNode(ctx.SYMBOL().getText());       
        } else if (ctx.STRING() != null) {
            return new ASTNode(ctx.STRING().getText());       
        } else if (ctx.NIL() != null) {
            return new ASTNode(ctx.NIL().getText().toLowerCase());       
        } else if (ctx.T() != null) {
            return new ASTNode(ctx.T().getText().toLowerCase());       
        } else if (ctx.list() != null) {
            return visit(ctx.list());
        } else if (ctx.PLUS() != null || ctx.MINUS() != null || ctx.MULTIPLY() != null || ctx.DIVIDE() != null) {
            String operator = ctx.getChild(0).getText(); // Get the operator symbol
            ASTNode node = new ASTNode(operator);
            for (int i = 1; i < ctx.getChildCount(); i++) { // Skip the operator itself
                node.addChild(visit(ctx.getChild(i)));
            }
            return node;
        } 
        return null;
    }

    @Override
    public ASTNode visitList(LispParser.ListContext ctx) {

        ASTNode node = new ASTNode("List");
        for (ParseTree child : ctx.children) {
            ASTNode expressionNode = visit(child); // Recursively visit expressions
            if(expressionNode != null){
                node.addChild(expressionNode);
            }
        }
        return node;
    }

    @Override
    public ASTNode visitBoundedExpression(LispParser.BoundedExpressionContext ctx) {
        if (ctx.WRITE() != null || ctx.WRITE_LINE() != null || ctx.DEFPARAMETER() != null || ctx.AND() != null || ctx.OR() != null || ctx.NOT() != null || ctx.EQUAL() != null|| ctx.GREATER() != null || ctx.GREATER_EQUAL() != null || ctx.LESS() != null || ctx.LESS_EQUAL() != null || ctx.IF() != null || ctx.COND() != null ) {
            String preserveWordName = ctx.getChild(0).getText().toLowerCase(); 
            System.out.println("preserve word name is " + preserveWordName);
            ASTNode node = new ASTNode(preserveWordName);
            for (int i = 1; i < ctx.getChildCount(); i++) { 
                ParseTree child = ctx.getChild(i);
                // System.out.println("child type is " + child.getClass());
                if (child instanceof TerminalNodeImpl) { // Handle terminal nodes (like string literals) 
                    node.addChild(new ASTNode(child.getText()));
                }else{
                    node.addChild(visit(ctx.getChild(i)));
                }
            }
            return node;
        } 
        if (ctx.COND() != null) {
            ASTNode condNode = new ASTNode("cond");
            for (ParseTree child : ctx.children) {
                if (child instanceof LispParser.Cond_branchContext) {
                    ASTNode branchNode = visit(child);
                    condNode.addChild(branchNode);
                }
            }
            return condNode;
        }
        return null;
    }

    @Override
    public ASTNode visitCond_branch(LispParser.Cond_branchContext ctx) {
        ASTNode branchNode = new ASTNode("cond_branch");
        for (LispParser.ExpressionContext exprCtx : ctx.expression()) {
            ASTNode exprNode = visit(exprCtx);
            branchNode.addChild(exprNode);
        }
        return branchNode;
    }
}