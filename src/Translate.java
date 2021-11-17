import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Translate extends HelloBaseListener {

	public String output = "";
	public int index = 0;
	//    public HashMap<String, Integer> indexMap = new HashMap<>();
	public static ParseTreeProperty<String> result = new ParseTreeProperty<>();
	public static ParseTreeProperty<Integer> location = new ParseTreeProperty<>();

	public static void sout(String s) {
		System.out.print(s);
	}

	public static void debugSout(ParserRuleContext ctx, Object s) {
		//        System.out.println(ctx.getClass() + " " + location.get(ctx));
	}

	@Override public void exitHello(HelloParser.HelloContext ctx) {
	}

	@Override public void exitFuncDef(HelloParser.FuncDefContext ctx) {
		//        sout("define dso_local i32 @main(){\n\t"+ result.get(ctx.block()) +"}");
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

	@Override public void exitStmt(HelloParser.StmtContext ctx) {
		result.put(ctx, "ret i32 %" + location.get(ctx.exp()));
		output += "%" + (++index) + " = load i32, i32* %" + location.get(ctx.exp()) + ", align 4\n\t";
		output += "ret i32 %" + index + "\n";
	}

	@Override public void exitExp(HelloParser.ExpContext ctx) {
		location.put(ctx, location.get(ctx.addExp()));
		//        result.put(ctx,tmp);
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

	/* unaryExp
	 *  表达式计算 要求是保存地址
	 * */
	@Override public void exitCalcResES(HelloParser.CalcResESContext ctx) {
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
	}

	@Override public void exitPrimaryExp3(HelloParser.PrimaryExp3Context ctx) {
		int tmp = 0;
		String number = ctx.Number().toString();
		if (number.startsWith("0x") || number.startsWith("0X")) {
			tmp = Integer.valueOf(number.substring(2), 16);
		} else if (number.startsWith("0")) {
			tmp = Integer.valueOf(number.substring(1), 8);
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

	@Override public void enterEveryRule(ParserRuleContext ctx) {
	}

	@Override public void exitEveryRule(ParserRuleContext ctx) {
	}

	@Override public void visitTerminal(TerminalNode node) {
	}

	@Override public void visitErrorNode(ErrorNode node) {
	}
}
