// Generated from D:/0_ToDo/Compile-Lab/src\Hello.g4 by ANTLR 4.9.2
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
	 * Enter a parse tree produced by the {@code varDef1}
	 * labeled alternative in {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef1(HelloParser.VarDef1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code varDef1}
	 * labeled alternative in {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef1(HelloParser.VarDef1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code varDef2}
	 * labeled alternative in {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef2(HelloParser.VarDef2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code varDef2}
	 * labeled alternative in {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef2(HelloParser.VarDef2Context ctx);
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
	 * Enter a parse tree produced by {@link HelloParser#funcFParams}.
	 * @param ctx the parse tree
	 */
	void enterFuncFParams(HelloParser.FuncFParamsContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#funcFParams}.
	 * @param ctx the parse tree
	 */
	void exitFuncFParams(HelloParser.FuncFParamsContext ctx);
	/**
	 * Enter a parse tree produced by {@link HelloParser#funcFParam}.
	 * @param ctx the parse tree
	 */
	void enterFuncFParam(HelloParser.FuncFParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#funcFParam}.
	 * @param ctx the parse tree
	 */
	void exitFuncFParam(HelloParser.FuncFParamContext ctx);
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
	 * Enter a parse tree produced by the {@code stmt1}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt1(HelloParser.Stmt1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt1}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt1(HelloParser.Stmt1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code stmt2}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt2(HelloParser.Stmt2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt2}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt2(HelloParser.Stmt2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code stmt3}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt3(HelloParser.Stmt3Context ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt3}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt3(HelloParser.Stmt3Context ctx);
	/**
	 * Enter a parse tree produced by the {@code stmt4}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt4(HelloParser.Stmt4Context ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt4}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt4(HelloParser.Stmt4Context ctx);
	/**
	 * Enter a parse tree produced by the {@code stmt5}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt5(HelloParser.Stmt5Context ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt5}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt5(HelloParser.Stmt5Context ctx);
	/**
	 * Enter a parse tree produced by the {@code stmt6}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt6(HelloParser.Stmt6Context ctx);
	/**
	 * Exit a parse tree produced by the {@code stmt6}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt6(HelloParser.Stmt6Context ctx);
	/**
	 * Enter a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(HelloParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(HelloParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(HelloParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(HelloParser.ContinueStmtContext ctx);
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
	 * Enter a parse tree produced by the {@code addExp2}
	 * labeled alternative in {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 */
	void enterAddExp2(HelloParser.AddExp2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code addExp2}
	 * labeled alternative in {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 */
	void exitAddExp2(HelloParser.AddExp2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code addExp1}
	 * labeled alternative in {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 */
	void enterAddExp1(HelloParser.AddExp1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code addExp1}
	 * labeled alternative in {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 */
	void exitAddExp1(HelloParser.AddExp1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code mulExp2}
	 * labeled alternative in {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void enterMulExp2(HelloParser.MulExp2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code mulExp2}
	 * labeled alternative in {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void exitMulExp2(HelloParser.MulExp2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code mulExp1}
	 * labeled alternative in {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void enterMulExp1(HelloParser.MulExp1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code mulExp1}
	 * labeled alternative in {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 */
	void exitMulExp1(HelloParser.MulExp1Context ctx);
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
	/**
	 * Enter a parse tree produced by {@link HelloParser#funcType}.
	 * @param ctx the parse tree
	 */
	void enterFuncType(HelloParser.FuncTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HelloParser#funcType}.
	 * @param ctx the parse tree
	 */
	void exitFuncType(HelloParser.FuncTypeContext ctx);
}