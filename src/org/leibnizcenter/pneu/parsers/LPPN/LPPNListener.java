// Generated from /home/giovanni/dev/pneu/antlr4/LPPN.g4 by ANTLR 4.5
package org.leibnizcenter.pneu.parsers.LPPN;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LPPNParser}.
 */
public interface LPPNListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LPPNParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(@NotNull LPPNParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(@NotNull LPPNParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void enterStatement_list(@NotNull LPPNParser.Statement_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#statement_list}.
	 * @param ctx the parse tree
	 */
	void exitStatement_list(@NotNull LPPNParser.Statement_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(@NotNull LPPNParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(@NotNull LPPNParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#head}.
	 * @param ctx the parse tree
	 */
	void enterHead(@NotNull LPPNParser.HeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#head}.
	 * @param ctx the parse tree
	 */
	void exitHead(@NotNull LPPNParser.HeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#antecedent}.
	 * @param ctx the parse tree
	 */
	void enterAntecedent(@NotNull LPPNParser.AntecedentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#antecedent}.
	 * @param ctx the parse tree
	 */
	void exitAntecedent(@NotNull LPPNParser.AntecedentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#consequent}.
	 * @param ctx the parse tree
	 */
	void enterConsequent(@NotNull LPPNParser.ConsequentContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#consequent}.
	 * @param ctx the parse tree
	 */
	void exitConsequent(@NotNull LPPNParser.ConsequentContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(@NotNull LPPNParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(@NotNull LPPNParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#bterm}.
	 * @param ctx the parse tree
	 */
	void enterBterm(@NotNull LPPNParser.BtermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#bterm}.
	 * @param ctx the parse tree
	 */
	void exitBterm(@NotNull LPPNParser.BtermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(@NotNull LPPNParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(@NotNull LPPNParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link LPPNParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(@NotNull LPPNParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link LPPNParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(@NotNull LPPNParser.AtomContext ctx);
}