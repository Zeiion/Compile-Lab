// Generated from D:/0_ToDo/Compile-Lab/src\Hello.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HelloParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HelloVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HelloParser#hello}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHello(HelloParser.HelloContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#compUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompUnit(HelloParser.CompUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(HelloParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#constDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDecl(HelloParser.ConstDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#constDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDef(HelloParser.ConstDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#constInitVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstInitVal(HelloParser.ConstInitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#constExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstExp(HelloParser.ConstExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#varDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDecl(HelloParser.VarDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#varDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarDef(HelloParser.VarDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#initVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitVal(HelloParser.InitValContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#funcDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncDef(HelloParser.FuncDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(HelloParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#blockItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockItem(HelloParser.BlockItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(HelloParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(HelloParser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(HelloParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#lVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLVal(HelloParser.LValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryExp1}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp1(HelloParser.PrimaryExp1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryExp2}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp2(HelloParser.PrimaryExp2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code primaryExp3}
	 * labeled alternative in {@link HelloParser#primaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExp3(HelloParser.PrimaryExp3Context ctx);
	/**
	 * Visit a parse tree produced by the {@code addExp2}
	 * labeled alternative in {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp2(HelloParser.AddExp2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code addExp1}
	 * labeled alternative in {@link HelloParser#addExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp1(HelloParser.AddExp1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code mulExp2}
	 * labeled alternative in {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp2(HelloParser.MulExp2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code mulExp1}
	 * labeled alternative in {@link HelloParser#mulExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp1(HelloParser.MulExp1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code calcResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCalcResES(HelloParser.CalcResESContext ctx);
	/**
	 * Visit a parse tree produced by the {@code normResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNormResES(HelloParser.NormResESContext ctx);
	/**
	 * Visit a parse tree produced by the {@code symbolResES}
	 * labeled alternative in {@link HelloParser#unaryExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbolResES(HelloParser.SymbolResESContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#unaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(HelloParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#funcRParams}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncRParams(HelloParser.FuncRParamsContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#relExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExp(HelloParser.RelExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#eqExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(HelloParser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#lAndExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLAndExp(HelloParser.LAndExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link HelloParser#lOrExp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLOrExp(HelloParser.LOrExpContext ctx);
}