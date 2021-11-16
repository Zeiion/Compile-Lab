// Generated from /home/zeiion/IdeaProjects/compile-antlr/src/Hello.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HelloParser}.
 */
public interface HelloListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HelloParser#hello}.
	 * @param ctx the parse tree
	 */
	void enterHello(HelloParser.HelloContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#hello}.
	 * @param ctx the parse tree
	 */
	void exitHello(HelloParser.HelloContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#compUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompUnit(HelloParser.CompUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#compUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompUnit(HelloParser.CompUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(HelloParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(HelloParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#constDecl}.
	 * @param ctx the parse tree
	 */
	void enterConstDecl(HelloParser.ConstDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#constDecl}.
	 * @param ctx the parse tree
	 */
	void exitConstDecl(HelloParser.ConstDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#constDef}.
	 * @param ctx the parse tree
	 */
	void enterConstDef(HelloParser.ConstDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#constDef}.
	 * @param ctx the parse tree
	 */
	void exitConstDef(HelloParser.ConstDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#constInitVal}.
	 * @param ctx the parse tree
	 */
	void enterConstInitVal(HelloParser.ConstInitValContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#constInitVal}.
	 * @param ctx the parse tree
	 */
	void exitConstInitVal(HelloParser.ConstInitValContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#constExp}.
	 * @param ctx the parse tree
	 */
	void enterConstExp(HelloParser.ConstExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#constExp}.
	 * @param ctx the parse tree
	 */
	void exitConstExp(HelloParser.ConstExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void enterVarDecl(HelloParser.VarDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#varDecl}.
	 * @param ctx the parse tree
	 */
	void exitVarDecl(HelloParser.VarDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(HelloParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(HelloParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#initVal}.
	 * @param ctx the parse tree
	 */
	void enterInitVal(HelloParser.InitValContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#initVal}.
	 * @param ctx the parse tree
	 */
	void exitInitVal(HelloParser.InitValContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(HelloParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(HelloParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(HelloParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(HelloParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void enterBlockItem(HelloParser.BlockItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void exitBlockItem(HelloParser.BlockItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(HelloParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(HelloParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(HelloParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(HelloParser.ExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(HelloParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(HelloParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#lVal}.
	 * @param ctx the parse tree
	 */
	void enterLVal(HelloParser.LValContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#lVal}.
	 * @param ctx the parse tree
	 */
	void exitLVal(HelloParser.LValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExp1}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExp1(HelloParser.PrimaryExp1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExp1}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExp1(HelloParser.PrimaryExp1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExp2}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExp2(HelloParser.PrimaryExp2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExp2}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExp2(HelloParser.PrimaryExp2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExp3}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExp3(HelloParser.PrimaryExp3Context ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExp3}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExp3(HelloParser.PrimaryExp3Context ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 */
	void enterAddExp(HelloParser.AddExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 */
	void exitAddExp(HelloParser.AddExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void enterMulExp(HelloParser.MulExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void exitMulExp(HelloParser.MulExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code calcResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void enterCalcResES(HelloParser.CalcResESContext ctx);
	/**
	 * Exit a parse tree produced by the {@code calcResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void exitCalcResES(HelloParser.CalcResESContext ctx);
	/**
	 * Enter a parse tree produced by the {@code normResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void enterNormResES(HelloParser.NormResESContext ctx);
	/**
	 * Exit a parse tree produced by the {@code normResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void exitNormResES(HelloParser.NormResESContext ctx);
	/**
	 * Enter a parse tree produced by the {@code symbolResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void enterSymbolResES(HelloParser.SymbolResESContext ctx);
	/**
	 * Exit a parse tree produced by the {@code symbolResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 */
	void exitSymbolResES(HelloParser.SymbolResESContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(HelloParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(HelloParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#funcRParams}.
	 * @param ctx the parse tree
	 */
	void enterFuncRParams(HelloParser.FuncRParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#funcRParams}.
	 * @param ctx the parse tree
	 */
	void exitFuncRParams(HelloParser.FuncRParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#relExp}.
	 * @param ctx the parse tree
	 */
	void enterRelExp(HelloParser.RelExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#relExp}.
	 * @param ctx the parse tree
	 */
	void exitRelExp(HelloParser.RelExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#eqExp}.
	 * @param ctx the parse tree
	 */
	void enterEqExp(HelloParser.EqExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#eqExp}.
	 * @param ctx the parse tree
	 */
	void exitEqExp(HelloParser.EqExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#lAndExp}.
	 * @param ctx the parse tree
	 */
	void enterLAndExp(HelloParser.LAndExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#lAndExp}.
	 * @param ctx the parse tree
	 */
	void exitLAndExp(HelloParser.LAndExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#lOrExp}.
	 * @param ctx the parse tree
	 */
	void enterLOrExp(HelloParser.LOrExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#lOrExp}.
	 * @param ctx the parse tree
	 */
	void exitLOrExp(HelloParser.LOrExpContext ctx);
}