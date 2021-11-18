import java.util.HashMap;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Translate extends HelloBaseListener {
	// TODO public HashMap<String,> voidMap
	public String prefix = "declare i32 @getint()\n" + "declare void @putint(i32)\n" + "declare i32 @getch()\n"
		+ "declare void @putch(i32)\n" + "declare i32 @getarray(i32*)\n" + "declare void @putarray(i32, i32*)";
	public String output = "";
	public int index = 0;
	//	public HashMap<String, Integer> varIndexMap = new HashMap<>();
	public HashMap<String, Var> varMap = new HashMap<>();
	public static ParseTreeProperty<String> result = new ParseTreeProperty<>();
	public static ParseTreeProperty<Integer> location = new ParseTreeProperty<>();

	public static void sout(String s) {
		System.out.print(s);
	}

	public static void debugSout(ParserRuleContext ctx, Object s) {
		//		System.out.println(ctx.getClass() + " " + location.get(ctx));
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
		String tmp = "";
		for (HelloParser.BlockItemContext i : ctx.blockItem()) {
			tmp += result.get(i.stmt()) + '\n';
		}
		result.put(ctx, tmp);
	}

	@Override public void exitBlockItem(HelloParser.BlockItemContext ctx) {
	}

	//  lVal '=' exp ';' # stmt1
	@Override public void exitStmt1(HelloParser.Stmt1Context ctx) {
		String ident = ctx.lVal().Ident().getText();
		if (varMap.get(ident).isConst) {
			System.out.println(ident + " is const!");
			System.exit(-1);
		}
		int tmpIndex = location.get(ctx.exp());
		int tmpTarget = location.get(ctx.lVal());
		output += "%" + (++index) + " = load i32, i32* %" + tmpIndex + ", align 4\n\t";
		output += "store i32 %" + index + ", i32* %" + tmpTarget + ",align 4\n\t";
	}

	//	'return' exp ';' # stmt5
	@Override public void exitStmt5(HelloParser.Stmt5Context ctx) {
		result.put(ctx, "ret i32 %" + location.get(ctx.exp()));
		output += "%" + (++index) + " = load i32, i32* %" + location.get(ctx.exp()) + ", align 4\n\t";
		output += "ret i32 %" + index + "\n";
	}

	//	// 赋值语句，向后看齐
	//	@Override public void exitVarDecl(HelloParser.VarDeclContext ctx) {
	//
	//	}
	//
	//	@Override public void exitConstDecl(HelloParser.ConstDeclContext ctx) {
	//	}

	/**
	 * Ident | Ident '=' initVal;
	 */
	@Override public void exitVarDef(HelloParser.VarDefContext ctx) {
		String tmpVar = ctx.Ident().getText();
		if (ctx.getChildCount() == 1) {
			//Ident
			output += "%" + (++index) + " = alloca i32, align 4\n\t";
			varMap.put(tmpVar, new Var(index));
		} else {
			//Ident = initVal
			output += "%" + (++index) + " = load i32, i32* %" + location.get(ctx.initVal()) + ", align 4\n\t";
			output += "%" + (++index) + " = alloca i32, align 4\n\t";
			varMap.put(tmpVar, new Var(index));
			output += "store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t";
		}
	}

	@Override public void exitConstDef(HelloParser.ConstDefContext ctx) {
		String tmpVar = ctx.Ident().getText();
		output += "%" + (++index) + " = load i32, i32* %" + location.get(ctx.constInitVal()) + ", align 4\n\t";
		output += "%" + (++index) + " = alloca i32, align 4\n\t";
		varMap.put(tmpVar, new Var(index, true));
		output += "store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t";
	}

	@Override public void exitInitVal(HelloParser.InitValContext ctx) {
		location.put(ctx, location.get(ctx.exp()));
	}

	@Override public void exitConstInitVal(HelloParser.ConstInitValContext ctx) {
		location.put(ctx, location.get(ctx.constExp()));
	}

	@Override public void exitExp(HelloParser.ExpContext ctx) {
		location.put(ctx, location.get(ctx.addExp()));
		//        result.put(ctx,tmp);
	}

	@Override public void exitConstExp(HelloParser.ConstExpContext ctx) {
		location.put(ctx, location.get(ctx.addExp()));
	}

	/* mulExp */
	@Override public void exitAddExp1(HelloParser.AddExp1Context ctx) {
		String tmp = result.get(ctx.mulExp());
		result.put(ctx, tmp);
		location.put(ctx, location.get(ctx.mulExp()));
		debugSout(ctx, tmp);
	}

	/* addExp ('+' | '−') mulExp  # addExp2 */
	@Override public void exitAddExp2(HelloParser.AddExp2Context ctx) {
		String tmp = result.get(ctx.mulExp());
		result.put(ctx, tmp);
		//加减法操作
		int index1 = location.get(ctx.addExp());
		output += "%" + (++index) + " = load i32, i32* %" + index1 + ", align 4\n\t";
		index1 = index;
		int index2 = location.get(ctx.mulExp());
		output += "%" + (++index) + " = load i32, i32* %" + index2 + ", align 4\n\t";
		index2 = index;
		String symbol = ctx.getChild(1).getText();
		if (symbol.equals("+")) {
			// 加法
			output += "%" + (++index) + " = add nsw i32 %" + index1 + ", %" + index2 + "\n\t";
		} else {
			// 减法
			output += "%" + (++index) + " = sub nsw i32 %" + index1 + ", %" + index2 + "\n\t";
		}
		output += "%" + (++index) + " = alloca i32, align 4\n\t";
		output += "store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t";
		location.put(ctx, index);
		debugSout(ctx, tmp);
	}

	/* unaryExp # mulExp1 */
	@Override public void exitMulExp1(HelloParser.MulExp1Context ctx) {
		String tmp = "";
		tmp = result.get(ctx.unaryExp());
		debugSout(ctx, tmp);
		location.put(ctx, location.get(ctx.unaryExp()));
		result.put(ctx, tmp);
	}

	/* mulExp ('*' | '/' | '%') unaryExp # mulExp2 */
	@Override public void exitMulExp2(HelloParser.MulExp2Context ctx) {
		String tmp = "";
		debugSout(ctx, tmp);
		//TODO S 乘除法
		int index1 = location.get(ctx.mulExp());
		output += "%" + (++index) + " = load i32, i32* %" + index1 + ", align 4\n\t";
		index1 = index;
		int index2 = location.get(ctx.unaryExp());
		output += "%" + (++index) + " = load i32, i32* %" + index2 + ", align 4\n\t";
		index2 = index;
		String symbol = ctx.getChild(1).getText();
		switch (symbol) {
			case "*":
				output += "%" + (++index) + " = mul nsw i32 %" + index1 + ", %" + index2 + "\n\t";
				break;
			case "/":
				output += "%" + (++index) + " = sdiv i32 %" + index1 + ", %" + index2 + "\n\t";
				break;
			case "%":
				output += "%" + (++index) + " = srem i32 %" + index1 + ", %" + index2 + "\n\t";
				break;
		}
		output += "%" + (++index) + " = alloca i32, align 4\n\t";
		output += "store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t";
		location.put(ctx, index);
		result.put(ctx, tmp);
	}

	// 函数
	@Override public void exitFuncRParams(HelloParser.FuncRParamsContext ctx) {
		//TODO 暂且这样
		location.put(ctx, location.get(ctx.exp(0)));
	}

	/* unaryExp
	 *  表达式计算 要求是保存地址
	 * */
	@Override public void exitCalcResES(HelloParser.CalcResESContext ctx) {
		try {
			checkIdent(ctx);
		} catch (Exception e) {
			System.exit(-1);
		}
		if (ctx.getChildCount() == 3) {

			//Ident '(' ')' # calcResES
			output += "%" + (++index) + " = call i32 @" + ctx.Ident() + "()\n\t";
			output += "%" + (++index) + " = alloca i32, align 4\n\t";
			output += "store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t";
			location.put(ctx, index);
		} else {
			//Ident '(' funcRParams ')' # calcResES
			output += "%" + (++index) + " = load i32, i32* %" + location.get(ctx.funcRParams()) + "\n\t";
			output += "call void @" + ctx.Ident() + "(i32 %" + index + ")\n\t";
			// TODO 好像用不到
			//			location.put(ctx, index);
		}
	}

	@Override public void exitNormResES(HelloParser.NormResESContext ctx) {
		String tmp = result.get(ctx.primaryExp());
		result.put(ctx, tmp);
		// 保存地址
		location.put(ctx, location.get(ctx.primaryExp()));
		debugSout(ctx, tmp);
	}

	@Override public void exitSymbolResES(HelloParser.SymbolResESContext ctx) {
		//        int tmp = Integer.parseInt(result.get(ctx.unaryExp()));
		String symbol = ctx.unaryOp().getText();
		int tmpIndex = location.get(ctx.unaryExp());
		//        System.out.println("symbol"+tmpIndex+symbol);
		// TODO S 符号操作
		switch (symbol) {
			case "+":
				break;
			case "-":
				//                tmp = -tmp;
				output += "%" + (++index) + " = load i32, i32* %" + tmpIndex + "\n\t";
				output += "%" + (++index) + " = sub nsw i32 0, %" + (index - 1) + "\n\t";
				output += "%" + (++index) + " = alloca i32, align 4\n\t";
				output += "store i32 %" + (index - 1) + ", i32* %" + index + ",align 4\n\t";
				tmpIndex = index;
				break;
			case "!":
				break;
		}
		//        debugSout(ctx,tmp);
		location.put(ctx, tmpIndex);
		//        result.put(ctx,tmp+"");
	}

	/*  PrimaryExp
	 *   保存地址
	 * */
	@Override public void exitPrimaryExp1(HelloParser.PrimaryExp1Context ctx) {
		String tmp = result.get(ctx.exp());
		result.put(ctx, tmp);
		location.put(ctx, location.get(ctx.exp()));
		//        System.out.println("location exp is"+location.get(ctx.exp()));
		debugSout(ctx, tmp);
	}

	@Override public void exitPrimaryExp2(HelloParser.PrimaryExp2Context ctx) {
		location.put(ctx, location.get(ctx.lVal()));
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
		output += "%" + (++index) + " = alloca i32, align 4\n\t";
		output += "store i32 " + tmp + ", i32* %" + index + ", align 4" + "\n\t";
		location.put(ctx, index);
		result.put(ctx, tmp + "");
		debugSout(ctx, ctx.Number());
	}

	//Ident
	@Override public void exitLVal(HelloParser.LValContext ctx) {
		location.put(ctx, varMap.get(ctx.Ident().getText()).index);
	}

	@Override public void enterEveryRule(ParserRuleContext ctx) {
	}

	@Override public void exitEveryRule(ParserRuleContext ctx) {
	}

	@Override public void visitTerminal(TerminalNode node) {
	}

	@Override public void visitErrorNode(ErrorNode node) {
	}
}
