// Generated from ../Lisp.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LispParser}.
 */
public interface LispListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LispParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(LispParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(LispParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(LispParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(LispParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#boundedExpression}.
	 * @param ctx the parse tree
	 */
	void enterBoundedExpression(LispParser.BoundedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#boundedExpression}.
	 * @param ctx the parse tree
	 */
	void exitBoundedExpression(LispParser.BoundedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(LispParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(LispParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(LispParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(LispParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#cond_branch}.
	 * @param ctx the parse tree
	 */
	void enterCond_branch(LispParser.Cond_branchContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#cond_branch}.
	 * @param ctx the parse tree
	 */
	void exitCond_branch(LispParser.Cond_branchContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispParser#raw_list}.
	 * @param ctx the parse tree
	 */
	void enterRaw_list(LispParser.Raw_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispParser#raw_list}.
	 * @param ctx the parse tree
	 */
	void exitRaw_list(LispParser.Raw_listContext ctx);
}