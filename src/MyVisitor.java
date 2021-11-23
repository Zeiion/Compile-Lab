import java.util.ArrayList;
import java.util.Stack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MyVisitor extends HelloBaseVisitor<Void> {
	private boolean constMode = false;

	public static String globalOutput = "";
	public String prefix = "declare i32 @getint()\n" + "declare void @putint(i32)\n" + "declare i32 @getch()\n"
		+ "declare void @putch(i32)\n" + "declare i32 @getarray(i32*)\n" + "declare void @putarray(i32, i32*)\n";
	public static String output = "";
	public static int index = 0;
	public static int globalIndex = 0;
	public static ParserRuleContext hello;

	public static ParseTreeProperty<String> store = new ParseTreeProperty<>();
	public static ParseTreeProperty<Integer> location = new ParseTreeProperty<>();
	public static ParseTreeProperty<Boolean> lock = new ParseTreeProperty<>();
	public static ParseTreeProperty<String> lockStore = new ParseTreeProperty<>();

	public static Stack<ParserRuleContext> blockStack = new Stack<>();
	public static ParseTreeProperty<ArrayList<Var>> blockVar = new ParseTreeProperty<>();

	public static Var getVar(String var) {
		int i = blockStack.size();
		while (--i >= 0) {
			ParserRuleContext tmpCtx = blockStack.get(i);
			ArrayList<Var> tmpBlockVar = blockVar.get(tmpCtx);
			if (tmpBlockVar == null) {
				tmpBlockVar = new ArrayList<>();
				blockVar.put(tmpCtx, tmpBlockVar);
			}
			for (Var v : tmpBlockVar) {
				if (v.name.equals(var)) {
					if (isGlobal()) {
						if (!v.isConst) {
							throw new RuntimeException(v.name + " should be const!");
						}
					}
					return v;
				}
			}
		}
		throw new RuntimeException("no such var!");
	}

	public static Var getGlobalVarById(int id) {
		for (Var v : blockVar.get(hello)) {
			if (v.index == id) {
				return v;
			}
		}
		throw new RuntimeException("no such global var!");
	}

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

	public static boolean isGlobal() {
		//		System.out.println("isGlobal " + blockStack.peek().getClass().equals(HelloParser.HelloContext.class));
		return blockStack.peek().getClass().equals(HelloParser.HelloContext.class);
	}

	public static void output(String s, ParserRuleContext ctx) {
		//		System.out.println("--" + ctx.getText() + " " + s);
		if (getLock(ctx)) {
			//			debugSout(ctx, "lock!");
			lockStore.put(ctx, getLockStore(ctx) + s);
		} else {
			if (isGlobal()) {
				globalOutput += s;
			} else {
				output += s;
			}
		}
	}

	public static String getReg(int i) {
		if (i < 0) {
			String ident = getGlobalVarById(i).name;
			return "@" + ident;
		}
		return "%x" + i;
	}

	public static String load(int to, int from) {
		return getReg(to) + " = load i32, i32* " + getReg(from) + ", align 4\n\t";
	}

	public static String alloca(int to) {
		return getReg(to) + " = alloca i32, align 4\n\t";
	}

	public static String icmp(int to, String symbol, int index1) {
		//ne 0
		return getReg(to) + " = icmp " + symbol + " i32 " + getReg(index1) + ", 0\n\t";
	}

	public static String icmp(int to, String symbol, int index1, int index2) {
		return getReg(to) + " = icmp " + symbol + " i32 " + getReg(index1) + ", " + getReg(index2) + "\n\t";
	}

	public static String br(int judge, int trueLabel, int falseLabel) {
		return "br i1 %x" + judge + ", label %x" + trueLabel + ", label %x" + falseLabel + "\n\t";
	}

	public static String br(int label) {
		return "br label %x" + label + "\n\t";
	}

	public static String store(int from, int to) {
		return "store i32 " + getReg(from) + ", i32* " + getReg(to) + ", align 4\n\t";
	}

	public static String store(String value, int to) {
		return "store i32 " + value + ", i32* " + getReg(to) + ", align 4\n\t";
	}

	public static String call(int to, String ident) {
		return getReg(to) + " = call i32 @" + ident + "()\n\t";
	}

	public static String call(String ident, int param) {
		return "call void @" + ident + "(i32 " + getReg(param) + ")\n\t";
	}

	public static String calc(int to, String symbol, int index1, int index2) {
		if (symbol.equals("sdiv") || symbol.equals("srem")) {
			return getReg(to) + " = " + symbol + " i32 " + getReg(index1) + ", " + getReg(index2) + "\n\t";
		}
		return getReg(to) + " = " + symbol + " nsw i32 " + getReg(index1) + ", " + getReg(index2) + "\n\t";
	}

	public static String calc(int to, String symbol, int index1) {
		return getReg(to) + " = " + symbol + " nsw i32 0, " + getReg(index1) + "\n\t";
	}

	public static String label(int label, String s) {
		return "\nx" + label + ":                                               ; preds = ???\n\t" + s;
	}

	public static String dso(String ident, String value) {
		return "@" + ident + " = dso_local global i32 " + value + "\n";
	}

	public static String zext(int to, int from) {
		return getReg(to) + " = zext i1 " + getReg(from) + " to i32\n\t";
	}

	public static String toi32point(int tmpIndex) {
		int i = ++index;
		return alloca(i) + store(tmpIndex, i);
	}

	public static void newVar(String tmpVar, int tmpIndex, boolean isConst) {
		ParserRuleContext tmpCtx = blockStack.peek();
		ArrayList<Var> tmpList = blockVar.get(tmpCtx);
		if (tmpList == null) {
			tmpList = new ArrayList<>();
			blockVar.put(tmpCtx, tmpList);
		}
		for (Var v : tmpList) {
			if (v.name.equals(tmpVar)) {
				throw new RuntimeException("var already exist!");
			}
		}
		tmpList.add(new Var(tmpVar, tmpIndex, isConst));
	}

	public static void newGlobalVar(String tmpVar, String value, int tmpIndex, boolean isConst) {
		ParserRuleContext tmpCtx = blockStack.peek();
		ArrayList<Var> tmpList = blockVar.get(tmpCtx);
		if (tmpList == null) {
			tmpList = new ArrayList<>();
			blockVar.put(tmpCtx, tmpList);
		}
		for (Var v : tmpList) {
			if (v.name.equals(tmpVar)) {
				throw new RuntimeException("global var already exist!");
			}
		}
		if (value == null) {
			// 全局量 初始化为0
			value = "0";
		}
		tmpList.add(new Var(true, Integer.valueOf(value), tmpVar, tmpIndex, isConst));
	}

	public static String ret(int retIndex) {
		return "ret i32 %x" + retIndex + "\n";
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

	@Override public Void visitHello(HelloParser.HelloContext ctx) {
		hello = ctx;
		blockStack.push(ctx);
		visitChildren(ctx);
		sout(globalOutput);
		sout(prefix);
		sout("define dso_local i32 @main(){\n\t" + output + "}");
		return null;
	}

	@Override public Void visitFuncDef(HelloParser.FuncDefContext ctx) {
		visitChildren(ctx);
		return null;
	}

	@Override public Void visitBlock(HelloParser.BlockContext ctx) {
		blockStack.push(ctx);
		visitChildren(ctx);
		blockStack.pop();
		return null;
	}

	//  lVal '=' exp ';' # stmt1
	@Override public Void visitStmt1(HelloParser.Stmt1Context ctx) {
		visitChildren(ctx);
		String ident = ctx.lVal().Ident().getText();
		if (getVar(ident).isConst) {
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

		visit(ctx.cond());
		int condIndex = getLocation(ctx.cond());
		int ifIndex = ++index;
		int elseIndex = 0;
		if (ctx.getChildCount() > 5) {
			elseIndex = ++index;
		}
		int endIndex = ++index;
		output(load(++index, condIndex), ctx);
		condIndex = index;
		output(icmp(++index, "ne", condIndex), ctx);
		condIndex = index;
		output(br(condIndex, ifIndex, elseIndex == 0 ? endIndex : elseIndex), ctx);
		// if
		output(label(ifIndex, ""), ctx);
		visit(ctx.stmt(0));
		output(br(endIndex), ctx);
		// else
		if (ctx.getChildCount() > 5) {
			output(label(elseIndex, ""), ctx);
			visit(ctx.stmt(1));
			output(br(endIndex), ctx);
		}
		output(label(endIndex, ""), ctx);
		return null;
	}

	//	'return' exp ';' # stmt5
	@Override public Void visitStmt5(HelloParser.Stmt5Context ctx) {
		visitChildren(ctx);
		output(load(++index, getLocation(ctx.exp())), ctx);
		output(ret(index), ctx);
		return null;
	}

	// 'while' '(' cond ')' stmt # stmt6
	@Override public Void visitStmt6(HelloParser.Stmt6Context ctx) {
		int startIndex = ++index;
		output(br(startIndex), ctx);
		output(label(startIndex, ""), ctx);
		visit(ctx.cond());
		int condIndex = getLocation(ctx.cond());
		int trueIndex = ++index;
		int endIndex = ++index;
		output(load(++index, condIndex), ctx);
		condIndex = index;
		output(icmp(++index, "ne", condIndex), ctx);
		condIndex = index;
		output(br(condIndex, trueIndex, endIndex), ctx);
		// while
		output(label(trueIndex, ""), ctx);
		visit(ctx.stmt());
		output(br(startIndex), ctx);
		// end
		output(label(endIndex, ""), ctx);
		return null;
	}

	@Override public Void visitCond(HelloParser.CondContext ctx) {
		visitChildren(ctx);
		// TODO
		location.put(ctx, location.get(ctx.lOrExp()));
		return null;
	}

	@Override public Void visitLOrExp(HelloParser.LOrExpContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			//lAndExp
			location.put(ctx, location.get(ctx.lAndExp()));
		} else {
			//lAndExp '||' lOrExp
			int res = ++index;
			output(alloca(res), ctx);
			visit(ctx.lAndExp());
			int index1 = location.get(ctx.lAndExp());
			int falseIndex = ++index;
			int endIndex = ++index;
			int failIndex = ++index;
			int successIndex = ++index;
			output(load(++index, index1), ctx);
			index1 = index;
			output(icmp(++index, "ne", index1), ctx);
			index1 = index;
			output(br(index1, successIndex, falseIndex), ctx);
			// false
			output(label(falseIndex, ""), ctx);
			visit(ctx.lOrExp());
			int index2 = location.get(ctx.lOrExp());
			output(load(++index, index2), ctx);
			index2 = index;
			output(icmp(++index, "ne", index2), ctx);
			index2 = index;
			output(br(index2, successIndex, failIndex), ctx);
			// fail
			output(label(failIndex, ""), ctx);
			output(store("0", res), ctx);
			output(br(endIndex), ctx);
			// success
			output(label(successIndex, ""), ctx);
			output(store("1", res), ctx);
			output(br(endIndex), ctx);
			// end
			output(label(endIndex, ""), ctx);
			location.put(ctx, res);
		}
		return null;
	}

	@Override public Void visitLAndExp(HelloParser.LAndExpContext ctx) {
		if (ctx.getChildCount() == 1) {
			//eqExp
			visitChildren(ctx);
			location.put(ctx, location.get(ctx.eqExp()));
		} else {
			//eqExp '&&' lAndExp
			int res = ++index;
			output(alloca(res), ctx);
			visit(ctx.eqExp());
			int index1 = location.get(ctx.eqExp());
			int trueIndex = ++index;
			int endIndex = ++index;
			int failIndex = ++index;
			output(load(++index, index1), ctx);
			index1 = index;
			output(icmp(++index, "ne", index1), ctx);
			index1 = index;
			output(br(index1, trueIndex, failIndex), ctx);
			// true
			output(label(trueIndex, ""), ctx);
			visit(ctx.lAndExp());
			int successIndex = ++index;
			int index2 = location.get(ctx.lAndExp());
			output(load(++index, index2), ctx);
			index2 = index;
			output(icmp(++index, "ne", index2), ctx);
			index2 = index;
			output(br(index2, successIndex, failIndex), ctx);
			// fail
			output(label(failIndex, ""), ctx);
			output(store("0", res), ctx);
			output(br(endIndex), ctx);
			// success
			output(label(successIndex, ""), ctx);
			output(store("1", res), ctx);
			output(br(endIndex), ctx);
			// end
			output(label(endIndex, ""), ctx);
			location.put(ctx, res);
		}
		return null;
	}

	@Override public Void visitEqExp(HelloParser.EqExpContext ctx) {
		// 返回的都是 i32 类型地址
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
			output(zext(++index, index - 1), ctx);
			output(toi32point(index), ctx);
			location.put(ctx, index);
		}
		return null;
	}

	@Override public Void visitRelExp(HelloParser.RelExpContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			//AddExp
			int tmpIndex = getLocation(ctx.addExp());
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
			output(zext(++index, index - 1), ctx);
			output(toi32point(index), ctx);
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
		if (isGlobal()) {
			// 全局变量
			newGlobalVar(tmpVar, store.get(ctx.initVal()), --globalIndex, false);
			output(dso(tmpVar, store.get(ctx.initVal()) == null ? "0" : String.valueOf(store.get(ctx.initVal()))), ctx);
		} else {
			if (ctx.getChildCount() == 1) {
				//Ident
				output(alloca(++index), ctx);
				newVar(tmpVar, index, false);
			} else {
				//Ident = initVal
				output(load(++index, getLocation(ctx.initVal())), ctx);
				output(alloca(++index), ctx);
				newVar(tmpVar, index, false);
				output(store(index - 1, index), ctx);
			}
		}
		return null;
	}

	@Override public Void visitConstDef(HelloParser.ConstDefContext ctx) {
		visitChildren(ctx);
		String tmpVar = ctx.Ident().getText();
		if (isGlobal()) {
			// 全局变量
			newGlobalVar(tmpVar, store.get(ctx.constInitVal()), --globalIndex, true);
			output(dso(tmpVar,
				store.get(ctx.constInitVal()) == null ? "0" : String.valueOf(store.get(ctx.constInitVal()))), ctx);
		} else {
			output(load(++index, getLocation(ctx.constInitVal())), ctx);
			output(alloca(++index), ctx);
			newVar(tmpVar, index, true);
			output(store(index - 1, index), ctx);
		}
		return null;
	}

	@Override public Void visitInitVal(HelloParser.InitValContext ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.exp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.exp()));
		}
		return null;
	}

	@Override public Void visitConstInitVal(HelloParser.ConstInitValContext ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.constExp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.constExp()));
		}
		return null;
	}

	@Override public Void visitExp(HelloParser.ExpContext ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.addExp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.addExp()));
		}
		return null;
	}

	@Override public Void visitConstExp(HelloParser.ConstExpContext ctx) {
		constMode = true;
		visitChildren(ctx);
		constMode = false;
		location.put(ctx, getLocation(ctx.addExp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.addExp()));
		}
		return null;
	}

	/* mulExp */
	@Override public Void visitAddExp1(HelloParser.AddExp1Context ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.mulExp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.mulExp()));
		}
		return null;
	}

	/* addExp ('+' | '−') mulExp  # addExp2 */
	@Override public Void visitAddExp2(HelloParser.AddExp2Context ctx) {
		visitChildren(ctx);
		//加减法操作
		String symbol = ctx.getChild(1).getText();
		if (isGlobal()) {
			// 全局
			int value1 = Integer.parseInt(store.get(ctx.addExp()));
			int value2 = Integer.parseInt(store.get(ctx.mulExp()));
			int res = 0;
			if (symbol.equals("+")) {
				// 加法
				res = value1 + value2;
			} else {
				// 减法
				res = value1 - value2;
			}
			store.put(ctx, String.valueOf(res));
		} else {
			int index1 = getLocation(ctx.addExp());
			output(load(++index, index1), ctx);
			index1 = index;
			int index2 = getLocation(ctx.mulExp());
			output(load(++index, index2), ctx);
			index2 = index;
			if (symbol.equals("+")) {
				// 加法
				output(calc(++index, "add", index1, index2), ctx);
			} else {
				// 减法
				output(calc(++index, "sub", index1, index2), ctx);
			}
			output(alloca(++index), ctx);
			output(store(index - 1, index), ctx);
		}
		location.put(ctx, index);
		return null;
	}

	/* unaryExp # mulExp1 */
	@Override public Void visitMulExp1(HelloParser.MulExp1Context ctx) {
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.unaryExp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.unaryExp()));
		}
		return null;
	}

	/* mulExp ('*' | '/' | '%x') unaryExp # mulExp2 */
	@Override public Void visitMulExp2(HelloParser.MulExp2Context ctx) {
		visitChildren(ctx);
		String tmp = "";
		//TODO S 乘除法
		String symbol = ctx.getChild(1).getText();
		if (isGlobal()) {
			// 全局
			int value1 = Integer.parseInt(store.get(ctx.mulExp()));
			int value2 = Integer.parseInt(store.get(ctx.unaryExp()));
			int res = 0;
			switch (symbol) {
				case "*":
					res = value1 * value2;
					break;
				case "/":
					res = value1 / value2;
					break;
				case "%":
					res = value1 % value2;
					break;
			}
			store.put(ctx, String.valueOf(res));
		} else {
			int index1 = getLocation(ctx.mulExp());
			output(load(++index, index1), ctx);
			index1 = index;
			int index2 = getLocation(ctx.unaryExp());
			output(load(++index, index2), ctx);
			index2 = index;
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
		}
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
		// 存储值
		store.put(ctx, store.get(ctx.primaryExp()));
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
				output(load(++index, tmpIndex), ctx);
				output(icmp(++index, "eq", index - 1), ctx);
				output(zext(++index, index - 1), ctx);
				output(toi32point(index), ctx);
				tmpIndex = index;
				break;
		}
		location.put(ctx, tmpIndex);
		return null;
	}

	/*  PrimaryExp
	 * */
	@Override public Void visitPrimaryExp1(HelloParser.PrimaryExp1Context ctx) {
		//'(' exp ')'
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.exp()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.exp()));
		}
		return null;
	}

	@Override public Void visitPrimaryExp2(HelloParser.PrimaryExp2Context ctx) {
		//lVal
		visitChildren(ctx);
		location.put(ctx, getLocation(ctx.lVal()));
		if (isGlobal()) {
			store.put(ctx, store.get(ctx.lVal()));
		}
		return null;
	}

	@Override public Void visitPrimaryExp3(HelloParser.PrimaryExp3Context ctx) {
		//Number
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
		if (isGlobal()) {
			// 存储值
			store.put(ctx, String.valueOf(tmp));
		} else {
			// 分配地址
			output(alloca(++index), ctx);
			output(store(String.valueOf(tmp), index), ctx);
		}
		// TODO to avoid none-nes error
		location.put(ctx, index);
		return null;
	}

	//Ident
	@Override public Void visitLVal(HelloParser.LValContext ctx) {
		visitChildren(ctx);
		String ident = ctx.Ident().getText();
		if (constMode) {
			if (!getVar(ident).isConst) {
				throw new RuntimeException(ident + " should be ident");
			}
		}
		location.put(ctx, getVar(ident).index);
		if (isGlobal()) {
			store.put(ctx, String.valueOf(getVar(ident).value));
		}
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
