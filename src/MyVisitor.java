import java.util.HashMap;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MyVisitor extends HelloBaseVisitor<Void> {
	private boolean constMode = false;
	// TODO public HashMap<String,> voidMap
	public String prefix = "declare i32 @getint()\n" + "declare void @putint(i32)\n" + "declare i32 @getch()\n"
		+ "declare void @putch(i32)\n" + "declare i32 @getarray(i32*)\n" + "declare void @putarray(i32, i32*)";
	public static String output = "";
	public int index = 0;
	//	public HashMap<String, Integer> varIndexMap = new HashMap<>();
	public HashMap<String, Var> varMap = new HashMap<>();
	public static ParseTreeProperty<String> store = new ParseTreeProperty<>();
	public static ParseTreeProperty<Integer> location = new ParseTreeProperty<>();
	public static ParseTreeProperty<Boolean> lock = new ParseTreeProperty<>();
	public static ParseTreeProperty<String> lockStore = new ParseTreeProperty<>();

	public static void debugSout(ParserRuleContext ctx, Object s) {
		//		System.out.println(ctx.getText() + " " + s);
	}

	public static boolean getLock(ParserRuleContext ctx) {
		boolean ret;
		try {
			ret = lock.get(ctx);
		} catch (NullPointerException e) {
			return false;
		}
		return ret;
	}

	public static String getLockStore(ParserRuleContext ctx) {
		String ret = "";
		try {
			ret = lockStore.get(ctx);
		} catch (NullPointerException e) {
			return "";
		}
		return ret == null ? "" : ret;
	}

	public static int getLocation(ParserRuleContext ctx) {
		int ret = 0;
		try {
			ret = location.get(ctx);
		} catch (NullPointerException e) {
			throw new RuntimeException(ctx.getText() + " location get null");
		}
		return ret;
	}

	public static void output(String s, ParserRuleContext ctx) {
		//		System.out.println("--" + ctx.getText() + " " + s);
		if (getLock(ctx)) {
			//			debugSout(ctx, "lock!");
			lockStore.put(ctx, getLockStore(ctx) + s);
		} else {
			output += s;
		}
	}

	public static String load(int to, int from) {
		return "%" + to + " = load i32, i32* %" + from + ", align 4\n\t";
	}

	public static String alloca(int to) {
		return "%" + to + " = alloca i32, align 4\n\t";
	}

	public static String icmp(int to, String symbol, int index1) {
		//ne 0
		return "%" + to + " = icmp " + symbol + " i32 %" + index1 + ", 0\n\t";
	}

	public static String icmp(int to, String symbol, int index1, int index2) {
		return "%" + to + " = icmp " + symbol + " i32 %" + index1 + ", %" + index2 + "\n\t";
	}

	public static String br(int judge, int trueLabel, int falseLabel) {
		return "br i1 %" + judge + ", label %" + trueLabel + ", label %" + falseLabel + "\n\t";
	}

	public static String br(int label) {
		return "br label %" + label + "\n\t";
	}

	public static String store(int from, int to) {
		return "store i32 %" + from + ", i32* %" + to + ", align 4\n\t";
	}

	public static String store(String value, int to) {
		return "store i32 " + value + ", i32* %" + to + ", align 4\n\t";
	}

	public static String call(int to, String ident) {
		return "%" + to + " = call i32 @" + ident + "()\n\t";
	}

	public static String call(String ident, int param) {
		return "call void @" + ident + "(i32 %" + param + ")\n\t";
	}

	public static String calc(int to, String symbol, int index1, int index2) {
		if (symbol.equals("sdiv") || symbol.equals("srem")) {
			return "%" + to + " = " + symbol + " i32 %" + index1 + ", %" + index2 + "\n\t";
		}
		return "%" + to + " = " + symbol + " nsw i32 %" + index1 + ", %" + index2 + "\n\t";
	}

	public static String calc(int to, String symbol, int index1) {
		return "%" + to + " = " + symbol + " nsw i32 0, %" + index1 + "\n\t";
	}

	public static String label(int label, String s) {
		return "\n" + label + ":                                               ; preds = %0\n\t" + s;
	}

	public static String ret(int retIndex) {
		return "ret i32 %" + retIndex + "\n";
	}

	public static void sout(String s) {
		System.out.print(s);
	}

	public static void checkIdent(HelloParser.CalcResESContext ctx) throws Exception {
		int count = ctx.getChildCount();
		String ident = ctx.Ident().getText();
		switch (ident) {
			case "putint":
			case "putch":
			case "putarray":
			case "getarray":
				if (count != 4) {
					//Ident '(' funcRParams ')' # calcResES
					throw new Exception();
				}
				// 参数个数
				int paramsCount = (ctx.funcRParams().getChildCount() + 1) / 2;
				switch (ident) {
					case "putint":
					case "putch":
					case "getarray":
						if (paramsCount != 1) {
							throw new Exception();
						}
						break;
					case "putarray":
						if (paramsCount != 2) {
							throw new Exception();
						}
						break;
				}
				break;
			case "getint":
			case "getch":
				if (count != 3) {
					//Ident '(' ')' # calcResES
					throw new Exception();
				}
				break;
			default:
				throw new Exception();
		}
	}

	@Override public Void visitFuncDef(HelloParser.FuncDefContext ctx) {
		visitChildren(ctx);
		//        sout("define dso_local i32 @main(){\n\t"+ result.get(ctx.block()) +"}");
		sout(prefix);
		sout("define dso_local i32 @main(){\n\t" + output + "}");
		return null;
	}

	//  lVal '=' exp ';' # stmt1
	@Override public Void visitStmt1(HelloParser.Stmt1Context ctx) {
		visitChildren(ctx);
		String ident = ctx.lVal().Ident().getText();
		if (varMap.get(ident).isConst) {
			throw new RuntimeException(ident + " is const!");
		}
		int tmpIndex = getLocation(ctx.exp());
		int tmpTarget = getLocation(ctx.lVal());
		output(load(++index, tmpIndex), ctx);
		String tmpSout = store(index, tmpTarget);
		output(tmpSout, ctx);
		return null;
	}

	// 'if' '(' cond ')' stmt ('else' stmt)? # stmt4
	@Override public Void visitStmt4(HelloParser.Stmt4Context ctx) {
		visitChildren(ctx);
		lock.put(ctx, true);
		int condIndex = getLocation(ctx.cond());
		output(br(condIndex, ++index, ++index), ctx);
		// TODO if
		output(label(index - 1, store(4, index + 1)), ctx);
		//TODO else
		if (ctx.getChildCount() > 5) {
			output(label(index, store(5, index + 1)) + label(++index, load(13, 4)) + (++index) + call("putint", 13)
				+ "  ret i32 0", ctx);
		}
		return null;
	}

	//	'return' exp ';' # stmt5
	@Override public Void visitStmt5(HelloParser.Stmt5Context ctx) {
		visitChildren(ctx);
		output(load(++index, getLocation(ctx.exp())), ctx);
		output(ret(index), ctx);
		return null;
	}

	@Override public Void visitCond(HelloParser.CondContext ctx) {
		visitChildren(ctx);
		// TODO
		location.put(ctx, ++index);
		return null;
	}

	@Override public Void visitLOrExp(HelloParser.LOrExpContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			//lAndExp
			location.put(ctx, location.get(ctx.lAndExp()));
		} else {
			//lOrExp '||' lAndExp
			String store1 = lockStore.get(ctx.lOrExp());
			String store2 = lockStore.get(ctx.lAndExp());
			int index1 = location.get(ctx.lOrExp());
			int index2 = location.get(ctx.lAndExp());
			output(load(++index, index1), ctx);
			output(load(++index, index2), ctx);
			output(store1 + br(index1, ++index, ++index), ctx);
			int trueIndex = index - 1;
			int falseIndex = index;
			// true
			output(label(trueIndex, ""), ctx);
			// false
			output(label(falseIndex, store2 + br(index2, ++index, ++index)), ctx);
		}
		return null;
	}

	@Override public Void visitLAndExp(HelloParser.LAndExpContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			//eqExp
			location.put(ctx, location.get(ctx.eqExp()));
		} else {
			//lAndExp '&&' eqExp
			String store1 = lockStore.get(ctx.lAndExp());
			String store2 = lockStore.get(ctx.eqExp());
			int index1 = location.get(ctx.lAndExp());
			int index2 = location.get(ctx.eqExp());
			output(load(++index, index1), ctx);
			output(load(++index, index2), ctx);
			output(store1 + br(index1, ++index, ++index), ctx);
			int trueIndex = index - 1;
			int falseIndex = index;
			// true
			output(label(trueIndex, store2 + br(index2, ++index, ++index)), ctx);
			// false
			output(label(falseIndex, br(index)), ctx);
		}
		return null;
	}

	@Override public Void visitEqExp(HelloParser.EqExpContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			//relExp
			location.put(ctx, location.get(ctx.relExp()));
		} else {
			//eqExp ('==' | '!=') relExp
			String symbol = ctx.getChild(1).getText();
			switch (symbol) {
				case "==":
					symbol = "eq";
					break;
				case "!=":
					symbol = "ne";
					break;
			}
			int index1 = getLocation(ctx.eqExp());
			output(load(++index, index1), ctx);
			index1 = index;
			int index2 = getLocation(ctx.relExp());
			output(load(++index, index2), ctx);
			index2 = index;
			output(icmp(++index, symbol, index1, index2), ctx);
			location.put(ctx, index);
		}
		return null;
	}

	@Override public Void visitRelExp(HelloParser.RelExpContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			//AddExp
			int tmpIndex = getLocation(ctx.addExp());
			//TODO
			//output("%" + (++index) + " = icmp ne i32 %" + tmpIndex + ", 0\n\t", ctx);
			location.put(ctx, tmpIndex);
		} else {
			//relExp ('<' | '>' | '<=' | '>=') addExp
			String symbol = ctx.getChild(1).getText();
			switch (symbol) {
				case "<":
					symbol = "slt";
					break;
				case ">":
					symbol = "sgt";
					break;
				case "<=":
					symbol = "sle";
					break;
				case ">=":
					symbol = "slg";
					break;
			}
			int index1 = getLocation(ctx.relExp());
			output(load(++index, index1), ctx);
			index1 = index;
			int index2 = getLocation(ctx.addExp());
			output(load(++index, index2), ctx);
			index2 = index;
			output(icmp(++index, symbol, index1, index2), ctx);
			location.put(ctx, index);
		}
		return null;
	}

	/**
	 * Ident | Ident '=' initVal;
	 */
	@Override public Void visitVarDef(HelloParser.VarDefContext ctx) {
		visitChildren(ctx);
		String tmpVar = ctx.Ident().getText();
		if (ctx.getChildCount() == 1) {
			//Ident
			output(alloca(++index), ctx);
			varMap.put(tmpVar, new Var(index));
		} else {
			//Ident = initVal
			output(load(++index, getLocation(ctx.initVal())), ctx);
			output(alloca(++index), ctx);
			varMap.put(tmpVar, new Var(index));
			output(store(index - 1, index), ctx);
		}
		return null;
	}

	@Override public Void visitConstDef(HelloParser.ConstDefContext ctx) {
		visitChildren(ctx);
		String tmpVar = ctx.Ident().getText();
		output(load(++index, getLocation(ctx.constInitVal())), ctx);
		output(alloca(++index), ctx);
		varMap.put(tmpVar, new Var(index, true));
		output(store(index - 1, index), ctx);
		return null;
	}

	@Override public Void visitInitVal(HelloParser.InitValContext ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.exp()));
		return null;
	}

	@Override public Void visitConstInitVal(HelloParser.ConstInitValContext ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.constExp()));
		return null;
	}

	@Override public Void visitExp(HelloParser.ExpContext ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.addExp()));
		return null;
	}

	@Override public Void visitConstExp(HelloParser.ConstExpContext ctx) {
		constMode = true;
		visitChildren(ctx);
		constMode = false;
		location.put(ctx, getLocation(ctx.addExp()));
		return null;
	}

	/* mulExp */
	@Override public Void visitAddExp1(HelloParser.AddExp1Context ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.mulExp()));
		return null;
	}

	/* addExp ('+' | '−') mulExp  # addExp2 */
	@Override public Void visitAddExp2(HelloParser.AddExp2Context ctx) {
		visitChildren(ctx);
		//加减法操作
		int index1 = getLocation(ctx.addExp());
		output(load(++index, index1), ctx);
		index1 = index;
		int index2 = getLocation(ctx.mulExp());
		output(load(++index, index2), ctx);
		index2 = index;
		String symbol = ctx.getChild(1).getText();
		if (symbol.equals("+")) {
			// 加法
			output(calc(++index, "add", index1, index2), ctx);
		} else {
			// 减法
			output(calc(++index, "sub", index1, index2), ctx);
		}
		output(alloca(++index), ctx);
		output(store(index - 1, index), ctx);
		location.put(ctx, index);
		return null;
	}

	/* unaryExp # mulExp1 */
	@Override public Void visitMulExp1(HelloParser.MulExp1Context ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.unaryExp()));
		return null;
	}

	/* mulExp ('*' | '/' | '%') unaryExp # mulExp2 */
	@Override public Void visitMulExp2(HelloParser.MulExp2Context ctx) {
		visitChildren(ctx);
		String tmp = "";
		//TODO S 乘除法
		int index1 = getLocation(ctx.mulExp());
		output(load(++index, index1), ctx);
		index1 = index;
		int index2 = getLocation(ctx.unaryExp());
		output(load(++index, index2), ctx);
		index2 = index;
		String symbol = ctx.getChild(1).getText();
		switch (symbol) {
			case "*":
				output(calc(++index, "mul", index1, index2), ctx);
				break;
			case "/":
				output(calc(++index, "sdiv", index1, index2), ctx);
				break;
			case "%":
				output(calc(++index, "srem", index1, index2), ctx);
				break;
		}
		output(alloca(++index), ctx);
		output(store(index - 1, index), ctx);
		location.put(ctx, index);
		return null;
	}

	// 函数
	@Override public Void visitFuncRParams(HelloParser.FuncRParamsContext ctx) {
		visitChildren(ctx);
		//TODO 暂且这样
		location.put(ctx, getLocation(ctx.exp(0)));
		return null;
	}

	/* unaryExp
	 *  表达式计算 要求是保存地址
	 * */
	@Override public Void visitCalcResES(HelloParser.CalcResESContext ctx) {
		visitChildren(ctx);
		try {
			checkIdent(ctx);
		} catch (Exception e) {
			throw new RuntimeException("params illegal!");
		}
		if (ctx.getChildCount() == 3) {
			//Ident '(' ')' # calcResES
			output(call(++index, ctx.Ident().getText()), ctx);
			output(alloca(++index), ctx);
			output(store(index - 1, index), ctx);
			location.put(ctx, index);
		} else {
			//Ident '(' funcRParams ')' # calcResES
			output(load(++index, getLocation(ctx.funcRParams())), ctx);
			output(call(ctx.Ident().getText(), index), ctx);
			// TODO 好像用不到
			location.put(ctx, index);
		}
		return null;
	}

	@Override public Void visitNormResES(HelloParser.NormResESContext ctx) {
		visitChildren(ctx);
		// 保存地址
		location.put(ctx, getLocation(ctx.primaryExp()));
		return null;
	}

	@Override public Void visitSymbolResES(HelloParser.SymbolResESContext ctx) {
		visitChildren(ctx);
		String symbol = ctx.unaryOp().getText();
		int tmpIndex = getLocation(ctx.unaryExp());
		switch (symbol) {
			case "+":
				break;
			case "-":
				output(load(++index, tmpIndex), ctx);
				output(calc(++index, "sub", index - 1), ctx);
				output(alloca(++index), ctx);
				output(store(index - 1, index), ctx);
				tmpIndex = index;
				break;
			case "!":
				break;
		}
		location.put(ctx, tmpIndex);
		return null;
	}

	/*  PrimaryExp
	 *   保存地址
	 * */
	@Override public Void visitPrimaryExp1(HelloParser.PrimaryExp1Context ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.exp()));
		return null;
	}

	@Override public Void visitPrimaryExp2(HelloParser.PrimaryExp2Context ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.lVal()));
		return null;
	}

	@Override public Void visitPrimaryExp3(HelloParser.PrimaryExp3Context ctx) {
		visitChildren(ctx);
		int tmp = 0;
		String number = ctx.Number().toString();
		if (number.startsWith("0x") || number.startsWith("0X")) {
			tmp = Integer.valueOf(number.substring(2), 16);
		} else if (number.startsWith("0")) {
			tmp = Integer.valueOf(number, 8);
		} else {
			tmp = Integer.parseInt(number);
		}
		// 分配地址
		output(alloca(++index), ctx);
		output(store(String.valueOf(tmp), index), ctx);
		location.put(ctx, index);
		return null;
	}

	//Ident
	@Override public Void visitLVal(HelloParser.LValContext ctx) {
		visitChildren(ctx);
		String ident = ctx.Ident().getText();
		if (constMode) {
			if (!varMap.get(ident).isConst) {
				throw new RuntimeException(ident + " should be ident");
			}
		}
		location.put(ctx, varMap.get(ident).index);
		return null;
	}

	//	@Override public Void enterEveryRule(ParserRuleContext ctx) {
	//		visitChildren(ctx);
	//		if (getLock(ctx.getParent())) {
	//			lock.put(ctx, true);
	//		}
	//		return null;
	//	}
	//
	//	@Override public Void visitEveryRule(ParserRuleContext ctx) {
	//		visitChildren(ctx);
	//		//		debugSout(ctx, "--debug");
	//		if (getLock(ctx)) {
	//			lock.put(ctx, false);
	//			if (getLock(ctx.getParent())) {
	//				lockStore.put(ctx.getParent(), getLockStore(ctx.getParent()) + getLockStore(ctx));
	//			} else {
	//				output(getLockStore(ctx), ctx);
	//			}
	//		}
	//		return null;
	//	}
	//

}
