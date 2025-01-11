// Generated from ../Lisp.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LispParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LispVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LispParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(LispParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(LispParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#boundedExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundedExpression(LispParser.BoundedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(LispParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(LispParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#lambdaExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaExpr(LispParser.LambdaExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#cond_branch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond_branch(LispParser.Cond_branchContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispParser#raw_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRaw_list(LispParser.Raw_listContext ctx);
}