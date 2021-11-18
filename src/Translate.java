import java.util.HashMap;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Translate extends HelloBaseListener {
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
		return ret;
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
		if (getLock(ctx)) {
			debugSout(ctx, "lock!");
			lockStore.put(ctx, getLockStore(ctx) + s);
		} else {
			output += s;
		}
	}

	public static void sout(String s) {
		System.out.print(s);
	}

	public static void debugSout(ParserRuleContext ctx, Object s) {
		System.out.println(ctx.getText() + " " + s);
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

	@Override public void exitHello(HelloParser.HelloContext ctx) {
	}

	@Override public void exitFuncDef(HelloParser.FuncDefContext ctx) {
		//        sout("define dso_local i32 @main(){\n\t"+ result.get(ctx.block()) +"}");
		sout(prefix);
		sout("define dso_local i32 @main(){\n\t" + output + "}");
	}

	@Override public void exitBlock(HelloParser.BlockContext ctx) {
	}

	@Override public void exitBlockItem(HelloParser.BlockItemContext ctx) {
	}

	//  lVal '=' exp ';' # stmt1
	@Override public void exitStmt1(HelloParser.Stmt1Context ctx) {
		String ident = ctx.lVal().Ident().getText();
		if (varMap.get(ident).isConst) {
			throw new RuntimeException(ident + " is const!");
		}
		int tmpIndex = getLocation(ctx.exp());
		int tmpTarget = getLocation(ctx.lVal());
		output("%" + (++index) + " = load i32, i32* %" + tmpIndex + ", align 4\n\t", ctx);
		String tmpSout = "store i32 %" + index + ", i32* %" + tmpTarget + ",align 4\n\t";
		output(tmpSout, ctx);
	}

	// 'if' '(' cond ')' stmt ('else' stmt)? # stmt4
	@Override public void exitStmt4(HelloParser.Stmt4Context ctx) {
		lock.put(ctx, true);
		int condIndex = getLocation(ctx.cond());
		output("br i1 %" + (condIndex) + ", label" + (++index) + ", label" + (++index) + "\n", ctx);
		// if
		output("\n" + (index - 1) + ":                                               ; preds = %0\n\t"
			+ "store i32 5, i32* %4\n\t" + "br label %" + (index + 1) + "\n", ctx);
		//else
		if (ctx.getChildCount() > 5) {
			output("\n" + index + ":                                               ; preds = %0\n\t"
				+ "store i32 10, i32* %5\n\t" + "br label %" + (index + 1) + "\n" + "\n" + (++index)
				+ ":                                               ; preds = %11, %10\n\t"
				+ "%13 = load i32, i32* %4\n\t" + "call void @putint(i32 %13)\n" + "  ret i32 0", ctx);
		}
	}

	//	'return' exp ';' # stmt5
	@Override public void exitStmt5(HelloParser.Stmt5Context ctx) {
		output("%" + (++index) + " = load i32, i32* %" + getLocation(ctx.exp()) + ", align 4\n\t", ctx);
		output("ret i32 %" + index + "\n", ctx);
	}

	@Override public void exitCond(HelloParser.CondContext ctx) {
	}

	@Override public void exitLOrExp(HelloParser.LOrExpContext ctx) {
		if (ctx.getChildCount() == 1) {
			//lAndExp
		} else {
			//lOrExp '||' lAndExp
		}
	}

	@Override public void exitLAndExp(HelloParser.LAndExpContext ctx) {
		if (ctx.getChildCount() == 1) {
			//eqExp
			location.put(ctx, location.get(ctx.eqExp()));
		} else {
			//lAndExp '&&' eqExp

		}
	}

	@Override public void exitEqExp(HelloParser.EqExpContext ctx) {
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
			output("%" + (++index) + " = load i32, i32* %" + index1 + ", align 4\n\t", ctx);
			index1 = index;
			int index2 = getLocation(ctx.relExp());
			output("%" + (++index) + " = load i32, i32* %" + index2 + ", align 4\n\t", ctx);
			index2 = index;
			output("%" + (++index) + " = icmp " + symbol + " i32, i32* %" + index1 + ", %" + index2 + "\n\t", ctx);
			location.put(ctx, index);
		}
	}

	@Override public void exitRelExp(HelloParser.RelExpContext ctx) {
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
			output("%" + (++index) + " = load i32, i32* %" + index1 + ", align 4\n\t", ctx);
			index1 = index;
			int index2 = getLocation(ctx.addExp());
			output("%" + (++index) + " = load i32, i32* %" + index2 + ", align 4\n\t", ctx);
			index2 = index;
			output("%" + (++index) + " = icmp " + symbol + " i32, i32* %" + index1 + ", %" + index2 + "\n\t", ctx);
			location.put(ctx, index);
		}
	}

	/**
	 * Ident | Ident '=' initVal;
	 */
	@Override public void exitVarDef(HelloParser.VarDefContext ctx) {
		String tmpVar = ctx.Ident().getText();
		if (ctx.getChildCount() == 1) {
			//Ident
			output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
			varMap.put(tmpVar, new Var(index));
		} else {
			//Ident = initVal
			output("%" + (++index) + " = load i32, i32* %" + getLocation(ctx.initVal()) + ", align 4\n\t", ctx);
			output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
			varMap.put(tmpVar, new Var(index));
			output("store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t", ctx);
		}
	}

	@Override public void exitConstDef(HelloParser.ConstDefContext ctx) {
		String tmpVar = ctx.Ident().getText();
		output("%" + (++index) + " = load i32, i32* %" + getLocation(ctx.constInitVal()) + ", align 4\n\t", ctx);
		output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
		varMap.put(tmpVar, new Var(index, true));
		output("store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t", ctx);
	}

	@Override public void exitInitVal(HelloParser.InitValContext ctx) {
		location.put(ctx, getLocation(ctx.exp()));
	}

	@Override public void exitConstInitVal(HelloParser.ConstInitValContext ctx) {
		location.put(ctx, getLocation(ctx.constExp()));
	}

	@Override public void exitExp(HelloParser.ExpContext ctx) {
		location.put(ctx, getLocation(ctx.addExp()));
	}

	@Override public void enterConstExp(HelloParser.ConstExpContext ctx) {
		// 进入const定义，之后的所有lVal都必须是const的，否则报错
		constMode = true;
	}

	@Override public void exitConstExp(HelloParser.ConstExpContext ctx) {
		constMode = false;
		location.put(ctx, getLocation(ctx.addExp()));
	}

	/* mulExp */
	@Override public void exitAddExp1(HelloParser.AddExp1Context ctx) {
		location.put(ctx, getLocation(ctx.mulExp()));
	}

	/* addExp ('+' | '−') mulExp  # addExp2 */
	@Override public void exitAddExp2(HelloParser.AddExp2Context ctx) {
		//加减法操作
		int index1 = getLocation(ctx.addExp());
		output("%" + (++index) + " = load i32, i32* %" + index1 + ", align 4\n\t", ctx);
		index1 = index;
		int index2 = getLocation(ctx.mulExp());
		output("%" + (++index) + " = load i32, i32* %" + index2 + ", align 4\n\t", ctx);
		index2 = index;
		String symbol = ctx.getChild(1).getText();
		if (symbol.equals("+")) {
			// 加法
			output("%" + (++index) + " = add nsw i32 %" + index1 + ", %" + index2 + "\n\t", ctx);
		} else {
			// 减法
			output("%" + (++index) + " = sub nsw i32 %" + index1 + ", %" + index2 + "\n\t", ctx);
		}
		output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
		output("store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t", ctx);
		location.put(ctx, index);
	}

	/* unaryExp # mulExp1 */
	@Override public void exitMulExp1(HelloParser.MulExp1Context ctx) {
		location.put(ctx, getLocation(ctx.unaryExp()));
	}

	/* mulExp ('*' | '/' | '%') unaryExp # mulExp2 */
	@Override public void exitMulExp2(HelloParser.MulExp2Context ctx) {
		String tmp = "";
		//TODO S 乘除法
		int index1 = getLocation(ctx.mulExp());
		output("%" + (++index) + " = load i32, i32* %" + index1 + ", align 4\n\t", ctx);
		index1 = index;
		int index2 = getLocation(ctx.unaryExp());
		output("%" + (++index) + " = load i32, i32* %" + index2 + ", align 4\n\t", ctx);
		index2 = index;
		String symbol = ctx.getChild(1).getText();
		switch (symbol) {
			case "*":
				output("%" + (++index) + " = mul nsw i32 %" + index1 + ", %" + index2 + "\n\t", ctx);
				break;
			case "/":
				output("%" + (++index) + " = sdiv i32 %" + index1 + ", %" + index2 + "\n\t", ctx);
				break;
			case "%":
				output("%" + (++index) + " = srem i32 %" + index1 + ", %" + index2 + "\n\t", ctx);
				break;
		}
		output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
		output("store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t", ctx);
		location.put(ctx, index);
	}

	// 函数
	@Override public void exitFuncRParams(HelloParser.FuncRParamsContext ctx) {
		//TODO 暂且这样
		location.put(ctx, getLocation(ctx.exp(0)));
	}

	/* unaryExp
	 *  表达式计算 要求是保存地址
	 * */
	@Override public void exitCalcResES(HelloParser.CalcResESContext ctx) {
		try {
			checkIdent(ctx);
		} catch (Exception e) {
			throw new RuntimeException("params illegal!");
		}
		if (ctx.getChildCount() == 3) {

			//Ident '(' ')' # calcResES
			output("%" + (++index) + " = call i32 @" + ctx.Ident() + "()\n\t", ctx);
			output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
			output("store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t", ctx);
			location.put(ctx, index);
		} else {
			//Ident '(' funcRParams ')' # calcResES
			output("%" + (++index) + " = load i32, i32* %" + getLocation(ctx.funcRParams()) + "\n\t", ctx);
			output("call void @" + ctx.Ident() + "(i32 %" + index + ")\n\t", ctx);
			// TODO 好像用不到
			location.put(ctx, index);
		}
	}

	@Override public void exitNormResES(HelloParser.NormResESContext ctx) {
		// 保存地址
		location.put(ctx, getLocation(ctx.primaryExp()));
	}

	@Override public void exitSymbolResES(HelloParser.SymbolResESContext ctx) {
		String symbol = ctx.unaryOp().getText();
		int tmpIndex = getLocation(ctx.unaryExp());
		switch (symbol) {
			case "+":
				break;
			case "-":
				//                tmp = -tmp;
				output("%" + (++index) + " = load i32, i32* %" + tmpIndex + "\n\t", ctx);
				output("%" + (++index) + " = sub nsw i32 0, %" + (index - 1) + "\n\t", ctx);
				output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
				output("store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t", ctx);
				tmpIndex = index;
				break;
			case "!":
				break;
		}
		location.put(ctx, tmpIndex);
	}

	/*  PrimaryExp
	 *   保存地址
	 * */
	@Override public void exitPrimaryExp1(HelloParser.PrimaryExp1Context ctx) {
		location.put(ctx, getLocation(ctx.exp()));
	}

	@Override public void exitPrimaryExp2(HelloParser.PrimaryExp2Context ctx) {
		location.put(ctx, getLocation(ctx.lVal()));
	}

	@Override public void exitPrimaryExp3(HelloParser.PrimaryExp3Context ctx) {
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
		output("%" + (++index) + " = alloca i32, align 4\n\t", ctx);
		output("store i32 " + tmp + ", i32* %" + index + ", align 4" + "\n\t", ctx);
		location.put(ctx, index);
	}

	//Ident
	@Override public void exitLVal(HelloParser.LValContext ctx) {
		String ident = ctx.Ident().getText();
		if (constMode) {
			if (!varMap.get(ident).isConst) {
				throw new RuntimeException(ident + " should be ident");
			}
		}
		location.put(ctx, varMap.get(ident).index);
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) {
		if (getLock(ctx.getParent())) {
			lock.put(ctx, true);
		}
	}

	@Override public void exitEveryRule(ParserRuleContext ctx) {
		debugSout(ctx, "--debug");
		lock.put(ctx, false);
		if (getLock(ctx.getParent())) {
			lockStore.put(ctx.getParent(), getLockStore(ctx.getParent()) + getLockStore(ctx));
		} else {
			output(getLockStore(ctx), ctx);
		}
	}

	@Override public void visitTerminal(TerminalNode node) {
	}

	@Override public void visitErrorNode(ErrorNode node) {
	}
}
