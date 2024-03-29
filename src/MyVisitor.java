import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class MyVisitor extends HelloBaseVisitor<Void> {
	private static boolean constMode = false;
	private static boolean constMode2 = false;

	public static String globalOutput = "";
	public String prefix = "declare i32 @getint()\n" + "declare void @putint(i32)\n" + "declare i32 @getch()\n"
		+ "declare void @putch(i32)\n" + "declare i32 @getarray(i32*)\n" + "declare void @putarray(i32, i32*)\n"
		+ "declare void @memset(i32*, i32, i32)\n";
	//	public static String output = "";
	public static ParseTreeProperty<StringBuilder> output = new ParseTreeProperty<>();
	public static int index = 0;
	public static int globalIndex = 0;
	public static ParserRuleContext hello;

	public static ArrayList<Func> defaultFuncList = new ArrayList<>();

	public static void initDefaultFuncList() {
		//putint
		ArrayList<Var> pip = new ArrayList<>();
		Var piv = new Var();
		piv.type = "i32";
		pip.add(piv);
		Func putint = new Func("putint", "void", pip);
		defaultFuncList.add(putint);

		//getint
		ArrayList<Var> gip = new ArrayList<>();
		Func getint = new Func("getint", "i32", gip);
		defaultFuncList.add(getint);

		//putch
		ArrayList<Var> pcp = new ArrayList<>();
		Var pcv = new Var();
		pcv.type = "i32";
		pcp.add(pcv);
		Func putch = new Func("putch", "void", pcp);
		defaultFuncList.add(putch);

		//getch
		ArrayList<Var> gcp = new ArrayList<>();
		Func getch = new Func("getch", "i32", gcp);
		defaultFuncList.add(getch);

		//putarray
		ArrayList<Var> pap = new ArrayList<>();
		Var pav1 = new Var();
		Var pav2 = new Var();
		pav1.type = "i32";
		pav2.isArray = true;
		pav2.type = "i32*";
		pav2.dimensionList = new ArrayList<>();
		pav2.dimension = 1;
		pav2.dimensionList.add(100);
		pap.add(pav1);
		pap.add(pav2);
		Func putarray = new Func("putarray", "void", pap);
		defaultFuncList.add(putarray);

		//getarray
		ArrayList<Var> gap = new ArrayList<>();
		Var gav = new Var();
		gav.isArray = true;
		// TODO fake dimensionList/type i32*?
		gav.dimensionList = new ArrayList<>();
		gav.dimensionList.add(100);
		gav.type = "i32*";
		gav.dimension = 1;
		gav.dimensionList = new ArrayList<>();
		gap.add(gav);
		Func getarray = new Func("getarray", "i32", gap);
		defaultFuncList.add(getarray);

	}

	// 定义数组时
	public static boolean defArray = false;

	public static ParseTreeProperty<String> store = new ParseTreeProperty<>();
	public static ParseTreeProperty<Integer> location = new ParseTreeProperty<>();
	public static ParseTreeProperty<Boolean> lock = new ParseTreeProperty<>();
	public static ParseTreeProperty<String> lockStore = new ParseTreeProperty<>();

	public static Stack<ParserRuleContext> blockStack = new Stack<>();
	public static ParseTreeProperty<ArrayList<Var>> blockVar = new ParseTreeProperty<>();

	public static Stack<ParserRuleContext> whileStack = new Stack<>();
	public static ParseTreeProperty<Integer> whileStart = new ParseTreeProperty<>();
	public static ParseTreeProperty<Integer> whileEnd = new ParseTreeProperty<>();

	public static Stack<ParserRuleContext> funcStack = new Stack<>();
	public static ArrayList<ParserRuleContext> funcList = new ArrayList<>();
	public static HashMap<ParserRuleContext, Func> funcMap = new HashMap<>();
	public static boolean hasMain = false;

	// 当前调用的函数的所有参数列表
	public static Stack<ArrayList<Var>> tmpCallFuncParamsStack = new Stack<>();

	public static Var getVarByName(String var) {
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
						if (!v.isConst && !v.isArray) {
							throw new RuntimeException(v.name + " should be const!");
						}
					}
					return v;
				}
			}
		}
		throw new RuntimeException("no such var!");
	}

	public static Var getVarById(int id) {
		int i = blockStack.size();
		while (--i >= 0) {
			ParserRuleContext tmpCtx = blockStack.get(i);
			ArrayList<Var> tmpBlockVar = blockVar.get(tmpCtx);
			if (tmpBlockVar == null) {
				tmpBlockVar = new ArrayList<>();
				blockVar.put(tmpCtx, tmpBlockVar);
			}
			for (Var v : tmpBlockVar) {
				if (v.index == id) {
					//					if (isGlobal()) {
					//						if (!v.isConst && !v.isArray) {
					//							throw new RuntimeException(v.name + " should be const!");
					//						}
					//					}
					return v;
				}
			}
		}
		return null;
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
		//		System.out.println("----" + ctx.getText() + " " + s);
	}

	public static boolean getLock(ParserRuleContext ctx) {
		return false;
		//		boolean ret;
		//		try {
		//			ret = lock.get(ctx);
		//		} catch (NullPointerException e) {
		//			return false;
		//		}
		//		return ret;
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
		if (defArray || constMode || constMode2) {
			return true;
		}
		//		System.out.println("isGlobal " + blockStack.peek().getClass().equals(HelloParser.HelloContext.class));
		//		return blockStack.peek().getClass().equals(HelloParser.HelloContext.class);
		return funcStack.isEmpty();
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
				if (output.get(funcStack.peek()) == null) {
					StringBuilder sb = new StringBuilder();
					output.put(funcStack.peek(), sb);
				}
				output.get(funcStack.peek()).append(s);
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

	public static String define(String type, String name, ArrayList<Var> params, String content) {
		// TODO params
		return "define dso_local " + type + " @" + name + "(" + defineParams(params) + ") {\n\t" + content + "}\n";
	}

	public static String defineParams(ArrayList<Var> params) {
		String tmp = "";
		for (int i = 0; i < params.size(); i++) {
			Var v = params.get(i);
			if (i == 0) {
				if (v.isArray) {
					tmp += subArrayType(v.type) + "* " + getReg(v.fakeIndex);
					continue;
				}
				tmp += v.type + " " + getReg(v.fakeIndex);
			} else {
				if (v.isArray) {
					tmp += ", " + subArrayType(v.type) + "* " + getReg(v.fakeIndex);
					continue;
				}
				tmp += ", " + v.type + " " + getReg(v.fakeIndex);
			}
		}
		return tmp;
	}

	public static String load(int to, int from) {
		return getReg(to) + " = load i32, i32* " + getReg(from) + ", align 4\n\t";
	}

	public static String loadStar(int to, String type, int from) {
		return getReg(to) + " = load " + type + ", " + type + "*" + " " + getReg(from) + ", align 4\n\t";
	}

	public static String alloca(int to) {
		return getReg(to) + " = alloca i32, align 4\n\t";
	}

	public static String alloca(int to, String type) {
		return getReg(to) + " = alloca " + type + "\n\t";
	}

	public static String allocaArray(int to, ArrayList<Integer> arr) {
		//[2 x [2 x i32]]
		//[2 x [2 x [2 x i32]]]
		return getReg(to) + " = alloca " + generateArrayType(arr) + "\n\t";
	}

	public static String generateArrayType(ArrayList<Integer> arr) {
		String tmp = "i32";
		for (int i = arr.size() - 1; i >= 0; i--) {
			if (arr.get(i) < 0) {
				throw new RuntimeException(arr.get(i) + " is not allowed!");
			}
			tmp = "[" + arr.get(i) + " x " + tmp + "]";
		}
		return tmp;
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

	public static String store(String type, int from, int to) {
		return "store " + type + " " + getReg(from) + ", " + type + "* " + getReg(to) + ", align 4\n\t";
	}

	public static String call(int to, String ident) {
		return getReg(to) + " = call i32 @" + ident + "()\n\t";
	}

	public static String call(String ident, int param) {
		return "call void @" + ident + "(i32 " + getReg(param) + ")\n\t";
	}

	public static String proCall(String name, ArrayList<Var> params, ParserRuleContext ctx) {
		Func f = findFuncByName(name);
		if (params.size() != f.params.size()) {
			throw new RuntimeException("params number not match!");
		}
		String ret = "";
		for (int i = 0; i < params.size(); i++) {
			Var tmpParam = f.params.get(i);
			Var tmpInputParam = params.get(i);
			// TODO 看看都有没有定义type
			if (!isArrayTypeEqual(tmpParam.type, params.get(i).type)) {
				throw new RuntimeException("params type not match!");
			}
			//TODO 对比每个参数
			if (tmpParam.isArray) {
				// 如果是数组，要对比dimension
				if (tmpParam.dimension != tmpInputParam.dimension) {
					throw new RuntimeException("dimension count not match!");
				}
				ArrayList<Integer> d1 = tmpParam.dimensionList;
				ArrayList<Integer> d2 = tmpInputParam.dimensionList;
				for (int j = 1; j < d1.size(); j++) {
					if (!d1.get(j).equals(d2.get(j))) {
						throw new RuntimeException("dimension not match!");
					}
				}
				tmpInputParam.fakeIndex = tmpInputParam.index;
			} else {
				output(load(++index, tmpInputParam.index), ctx);
				tmpInputParam.fakeIndex = index;
			}
			if (i == 0) {
				ret += tmpParam.type + " " + getReg(tmpInputParam.fakeIndex);
			} else {
				ret += ", " + tmpParam.type + " " + getReg(tmpInputParam.fakeIndex);
			}
		}
		//call void @putarray(i32 %34, i32* %38)
		//%8 = getelementptr [2 x [3 x i32]], [2 x [3 x i32]]* %1, i32 0, i32 0
		//%9 = call i32 @sum2d([3 x i32]* %8)
		return "call " + f.type + " @" + name + "(" + ret + ")\n\t";
	}

	public static boolean isArrayTypeEqual(String a, String b) {
		if (a.equals(b)) {
			return true;
		} else if (a.equals(subArrayType(b) + "*") || b.equals(subArrayType(a) + "*")) {
			return true;
		}
		return false;
	}

	public static String proCall(int id, String name, ArrayList<Var> params, ParserRuleContext ctx) {
		Func f = findFuncByName(name);
		if (params.size() != f.params.size()) {
			throw new RuntimeException("params number not match!");
		}
		String ret = "";
		for (int i = 0; i < params.size(); i++) {
			Var tmpParam = f.params.get(i);
			Var tmpInputParam = params.get(i);
			// TODO 看看都有没有定义type
			if (!isArrayTypeEqual(tmpParam.type, params.get(i).type)) {
				throw new RuntimeException("params type not match!");
			}
			//TODO 对比每个参数
			if (tmpParam.isArray) {
				// 如果是数组，要对比dimension
				if (tmpParam.dimension != tmpInputParam.dimension) {
					throw new RuntimeException("dimension count not match!");
				}
				ArrayList<Integer> d1 = tmpParam.dimensionList;
				ArrayList<Integer> d2 = tmpInputParam.dimensionList;
				for (int j = 1; j < d1.size(); j++) {
					if (!d1.get(j).equals(d2.get(j))) {
						throw new RuntimeException("dimension not match!");
					}
				}
				tmpInputParam.fakeIndex = tmpInputParam.index;
			} else {
				output(load(++index, tmpInputParam.index), ctx);
				tmpInputParam.fakeIndex = index;
			}
			if (i == 0) {
				ret += tmpParam.type + " " + getReg(tmpInputParam.fakeIndex);
			} else {
				ret += ", " + tmpParam.type + " " + getReg(tmpInputParam.fakeIndex);
			}
		}
		//call void @putarray(i32 %34, i32* %38)
		//%8 = getelementptr [2 x [3 x i32]], [2 x [3 x i32]]* %1, i32 0, i32 0
		//%9 = call i32 @sum2d([3 x i32]* %8)
		return getReg(id) + " = call " + f.type + " @" + name + "(" + ret + ")\n\t";
	}

	public static String proCall(String name, ParserRuleContext ctx) {
		Func f = findFuncByName(name);
		if (f.params.size() != 0) {
			throw new RuntimeException("params number not match!");
		}
		return "call " + f.type + " @" + name + "(" + ")\n\t";
	}

	public static String proCall(int id, String name, ParserRuleContext ctx) {
		Func f = findFuncByName(name);
		if (f.params.size() != 0) {
			throw new RuntimeException("params number not match!");
		}
		return getReg(id) + " = call " + f.type + " @" + name + "(" + ")\n\t";
	}

	public static String calc(int to, String symbol, int index1, int index2) {
		if ("sdiv".equals(symbol) || "srem".equals(symbol)) {
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

	public static String dsoG(String ident, String value) {
		return "@" + ident + " = dso_local global " + value + "\n";
	}

	public static String dsoC(String ident, String value) {
		return "@" + ident + " = dso_local constant " + value + "\n";
	}

	public static String zext(int to, int from) {
		return getReg(to) + " = zext i1 " + getReg(from) + " to i32\n\t";
	}

	/*i32 数值类型*/
	public static String getelementptr(int to, String type, int from, ArrayList<Integer> bias, ParserRuleContext ctx) {
		boolean flag = false;
		//		while (type.contains("*")) {
		//			flag = true;
		//			output(alloca(++index, type), ctx);
		//			output(store(type, from, index), ctx);
		//			output(loadStar(++index, type, index - 1), ctx);
		//			from = index;
		//			type = type.substring(0, type.length() - 1);
		//		}
		String biasString = "";
		//		if (flag) {
		//			// 去 0
		//			bias = new ArrayList<>(bias.subList(1, bias.size()));
		//		}
		for (int b : bias) {
			biasString += ", i32 " + b;
		}
		return getReg(to) + " = getelementptr " + type + ", " + type + "* " + getReg(from) + biasString + "\n\t";
	}

	/*i32 寄存器地址类型*/
	public static String getelementptrAddr(String targetType, Var tmpV, int to, String type, int from,
		ArrayList<Integer> bias, ParserRuleContext ctx) {
		boolean flag = false;
		debugSout(ctx, to + "--" + bias.size() + "--" + type + "--" + targetType);
		//		while (type.contains("*")) {
		//		//			flag = true;
		//		//			output(alloca(++index, type), ctx);
		//		//			output(store(type, from, index), ctx);
		//		//			output(loadStar(++index, type, index - 1), ctx);
		//		//			from = index;
		//		//			type = type.substring(0, type.length() - 1);
		//		//		}
		int biasCount = bias.size();
		if (type.contains("*")) {
			output(loadStar(++index, type, from), ctx);
			type = subArrayType(type);
			biasCount += 1;
			from = index;
		}
		String tmpType = type;
		if (targetType.contains("*")) {
			targetType = subArrayType(targetType);
		}
		biasCount -= 1;
		while (!tmpType.equals(targetType)) {
			biasCount -= 1;
			tmpType = subArrayType(tmpType);
		}
		String biasString = "";
		if (biasCount < 0) {
			// 说明不够
			biasString += ", i32 0";
			biasCount++;
			for (int b : bias) {
				biasString += ", i32 " + getReg(b);
			}
			for (int k = 0; k < (-biasCount); k++) {
				biasString += ", i32 0";
			}
		} else {
			for (int b : bias) {
				biasString += ", i32 " + getReg(b);
			}
		}
		//		if (!flag) {
		//			biasString += ", i32 0";
		//		}
		//		String biasString = flag ? "" : ", i32 0";

		return getReg(to) + " = getelementptr " + type + ", " + type + "* " + getReg(from) + biasString + "\n\t";
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

	public static void newVar(String tmpVar, int tmpIndex, boolean isConst, int value) {
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
		tmpList.add(new Var(false, value, tmpVar, tmpIndex, isConst));
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

	public static void newArrayVar(String tmpVar, int tmpIndex, boolean isConst, int dimension, ArrayList<Integer> arr,
		String type, ArrayList<Integer> dArr) {
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
		Var tmp = new Var(tmpVar, tmpIndex, isConst);
		tmp.setArray(dimension, arr, type, dArr);
		tmpList.add(tmp);
	}

	public static void newGlobalArrayVar(String tmpVar, int tmpIndex, boolean isConst, int dimension,
		ArrayList<Integer> arr, String type, ArrayList<Integer> dArr) {
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
		Var tmp = new Var(true, 0, tmpVar, tmpIndex, isConst);
		tmp.setArray(dimension, arr, type, dArr);
		tmpList.add(tmp);
	}

	public static String ret(int retIndex) {
		return "ret i32 %x" + retIndex + "\n";
	}

	public static String ret() {
		return "ret void\n";
	}

	public static void sout(String s) {
		System.out.print(s);
	}

	public static void checkVoid(HelloParser.CalcResESContext ctx) throws Exception {
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
		initDefaultFuncList();
		hello = ctx;
		blockStack.push(ctx);
		visitChildren(ctx);
		sout(globalOutput);
		sout(prefix);
		for (ParserRuleContext p : funcList) {
			ArrayList<String> outputs = new ArrayList<String>(Arrays.asList(output.get(p).toString().split("\n")));
			String o1 = "";
			String o2 = "";
			for (String s : outputs) {
				if (s.contains("alloca")) {
					o1 += s + "\n";
				} else {
					o2 += s + "\n";
				}
			}
			sout(define(funcMap.get(p).type, funcMap.get(p).name, funcMap.get(p).params, o1 + o2));
		}
		if (!hasMain) {
			throw new RuntimeException("no main error!");
		}
		sout("\n\n\n");
		return null;
	}

	@Override public Void visitFuncDef(HelloParser.FuncDefContext ctx) {
		funcList.add(ctx);
		funcStack.push(ctx);
		funcMap.put(ctx, new Func());
		Func f = funcMap.get(ctx);
		ArrayList<Var> params = new ArrayList<>();
		f.params = params;
		visit(ctx.Ident());
		visit(ctx.funcType());
		String name = ctx.Ident().getText();
		String type = ctx.funcType().getText();
		f.type = type.equals("int") ? "i32" : "void";
		f.name = name;
		visitChildren(ctx);
		if ("main".equals(name)) {
			if (hasMain) {
				throw new RuntimeException("already has main!");
			} else {
				hasMain = true;
			}
		}
		if (type.equals("void")) {
			output("ret void\n", ctx);
		} else {
			output("ret i32 0\n", ctx);
		}
		funcStack.pop();
		return null;
	}

	public static Func findFuncByName(String name) {
		for (Func f : defaultFuncList) {
			if (f.name.equals(name)) {
				return f;
			}
		}
		try {
			for (ParserRuleContext p : funcList) {
				Func f = funcMap.get(p);
				if (f.name.equals(name)) {
					return f;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("func " + name + " found error!");
		}
		throw new RuntimeException("func " + name + "not found!");
	}

	@Override public Void visitFuncFParams(HelloParser.FuncFParamsContext ctx) {
		visitChildren(ctx);
		HelloParser.FuncDefContext parent = (HelloParser.FuncDefContext)ctx.getParent();
		Func f = funcMap.get(parent);
		// 将参数加入block
		ParserRuleContext block = parent.block();
		blockStack.push(block);
		for (int i = 0; i < f.params.size(); i++) {
			Var v = f.params.get(i);
			if (v.isArray) {
				// TODO arrValues?
				newArrayVar(v.name, v.index, v.isConst, v.dimension, v.arrValues, v.type, v.dimensionList);
			} else {
				newVar(v.name, v.index, v.isConst);
			}
		}
		blockStack.pop();
		return null;
	}

	/*
	BType Ident ('[' ']' ('[' exp ']')* )?; 2+(2+3n)?
	* */
	@Override public Void visitFuncFParam(HelloParser.FuncFParamContext ctx) {
		defArray = true;
		visitChildren(ctx);
		defArray = false;
		Func f = funcMap.get(funcStack.peek());
		int count = ctx.getChildCount();
		Var tmpVar = new Var();
		String name = ctx.Ident().getText();
		tmpVar.name = name;
		tmpVar.index = ++index;
		// 还要重新分配一次
		// %5 = alloca [3 x i32]*
		// store [3 x i32]* %0, [3 x i32]* * %5
		if (count == 2) {
			// 普通int类型
			output(alloca(++index), ctx);
			output(store("i32", tmpVar.index, index), ctx);
			//替换fakeId
			tmpVar.fakeIndex = tmpVar.index;
			tmpVar.index = index;
		} else {
			//array
			int expCount = (count - 4) / 3 + 1; //包含空的[]
			tmpVar.isArray = true;
			tmpVar.dimension = expCount;
			ArrayList<Integer> dimensionList = new ArrayList<>();
			dimensionList.add(0);
			for (int i = 1; i < expCount; i++) {
				dimensionList.add(Integer.parseInt(store.get(ctx.exp(i - 1))));
			}
			tmpVar.dimensionList = dimensionList;
			//TODO type
			tmpVar.type = subArrayType(generateArrayType(dimensionList)) + "*";
			//再分配一个地址
			output(alloca(++index, tmpVar.type), ctx);
			output(store(tmpVar.type, tmpVar.index, index), ctx);
			//替换fakeId
			tmpVar.fakeIndex = tmpVar.index;
			tmpVar.index = index;
		}
		f.params.add(tmpVar);
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
		if (getVarByName(ident).isConst) {
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

	//	'return' exp? ';' # stmt5
	@Override public Void visitStmt5(HelloParser.Stmt5Context ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 2) {
			output(ret(), ctx);
			return null;
		}
		output(load(++index, getLocation(ctx.exp())), ctx);
		output(ret(index), ctx);
		return null;
	}

	// 'while' '(' cond ')' stmt # stmt6
	@Override public Void visitStmt6(HelloParser.Stmt6Context ctx) {
		whileStack.push(ctx);
		int startIndex = ++index;
		// save startIndex
		whileStart.put(ctx, startIndex);
		output(br(startIndex), ctx);
		output(label(startIndex, ""), ctx);
		visit(ctx.cond());
		int condIndex = getLocation(ctx.cond());
		int trueIndex = ++index;

		int endIndex = ++index;
		// save endIndex
		whileEnd.put(ctx, endIndex);

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
		whileStack.pop();
		return null;
	}

	// 'break' ';' # breakStmt
	@Override public Void visitBreakStmt(HelloParser.BreakStmtContext ctx) {
		visitChildren(ctx);
		// 跳到endIndex
		ParserRuleContext tmpWhile = whileStack.peek();
		int endIndex = whileEnd.get(tmpWhile);
		output(br(endIndex), ctx);
		return null;
	}

	// 'continue' ';' # continueStmt
	@Override public Void visitContinueStmt(HelloParser.ContinueStmtContext ctx) {
		visitChildren(ctx);
		// 跳到startIndex
		ParserRuleContext tmpWhile = whileStack.peek();
		int startIndex = whileStart.get(tmpWhile);
		output(br(startIndex), ctx);
		return null;
	}

	@Override public Void visitCond(HelloParser.CondContext ctx) {
		visitChildren(ctx);
		//
		location.put(ctx, location.get(ctx.lOrExp()));
		return null;
	}

	@Override public Void visitLOrExp(HelloParser.LOrExpContext ctx) {
		if (ctx.getChildCount() == 1) {
			//lAndExp
			visitChildren(ctx);
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
					symbol = "sge";
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
	 * Ident ( '[' constExp ']')*   # varDef1 1 + 3n
	 */
	@Override public Void visitVarDef1(HelloParser.VarDef1Context ctx) {
		String tmpVar = ctx.Ident().getText();
		if (ctx.getChildCount() == 1) {
			visitChildren(ctx);
			if (isGlobal()) {
				// 全局变量
				newGlobalVar(tmpVar, "0", --globalIndex, false);
				output(dsoG(tmpVar, "i32 0"), ctx);
			} else {
				//Ident
				output(alloca(++index), ctx);
				newVar(tmpVar, index, false);
			}
		} else {
			int totalNumber = 1;
			defArray = true;
			int dimension = (ctx.getChildCount() - 1) / 3;
			ArrayList<Integer> expList = new ArrayList<>();
			for (int i = 0; i < dimension; i++) {
				visit(ctx.constExp(i));
				int tmpD = Integer.parseInt(store.get(ctx.constExp(i)));
				if (tmpD < 0) {
					throw new RuntimeException(tmpD + " is not allowed!");
				}
				expList.add(tmpD);
				totalNumber *= expList.get(i);
			}
			defArray = false;
			String type = generateArrayType(expList);
			if (isGlobal()) {
				// global
				//@d = dso_local global [5 x i32] zeroinitializer
				output(dsoG(tmpVar, type + " zeroinitializer"), ctx);
				ArrayList<Integer> saveList = new ArrayList<>();
				for (int i = 0; i < totalNumber; i++) {
					saveList.add(0);
				}
				newGlobalArrayVar(tmpVar, --globalIndex, false, dimension, saveList, type, expList);
			} else {
				output(allocaArray(++index, expList), ctx);
				// 非全局未初始化报错？
				ArrayList<Integer> saveList = new ArrayList<>();
				for (int i = 0; i < totalNumber; i++) {
					saveList.add(0);
				}
				newArrayVar(tmpVar, index, false, dimension, saveList, type, expList);
			}
		}
		return null;
	}

	/**
	 * Ident ( '[' constExp ']' )* '=' initVal  # varDef2 3 + 3n
	 */
	@Override public Void visitVarDef2(HelloParser.VarDef2Context ctx) {
		String tmpVar = ctx.Ident().getText();
		if (ctx.getChildCount() == 3) {
			visitChildren(ctx);
			if (isGlobal()) {
				// 全局变量
				newGlobalVar(tmpVar, store.get(ctx.initVal()), --globalIndex, false);
				output(dsoG(tmpVar,
					"i32 " + (store.get(ctx.initVal()) == null ? "0" : String.valueOf(store.get(ctx.initVal())))), ctx);
			} else {
				//Ident = initVal
				output(load(++index, getLocation(ctx.initVal())), ctx);
				output(alloca(++index), ctx);
				newVar(tmpVar, index, false);
				output(store(index - 1, index), ctx);
			}
		} else {
			defArray = true;
			int dimension = (ctx.getChildCount() - 3) / 3;
			ArrayList<Integer> expList = new ArrayList<>(); // 保存的是维度信息
			for (int i = 0; i < dimension; i++) {
				visit(ctx.constExp(i));
				int tmpD = Integer.parseInt(store.get(ctx.constExp(i)));
				if (tmpD < 0) {
					throw new RuntimeException(tmpD + " is not allowed!");
				}
				expList.add(tmpD);
			}
			defArray = false;
			// 获取当前数组type
			String type = generateArrayType(expList);
			if (isGlobal()) {
				// global
				//@b = dso_local global [2 x [3 x i32]] [[3 x i32] [i32 1, i32 0, i32 0], [3 x i32] zeroinitializer]

				//'{' ( initVal (',' initVal)* )? '}' 2+(1+2n)
				HelloParser.InitValContext initVal = ctx.initVal();
				// 初始化 如果需要的话
				ArrayList<Integer> saveList = new ArrayList<>(); // 保存的是所有数据
				defArray = true;
				visitChildren(initVal);
				defArray = false;
				initialArray(initVal, type, expList, saveList, 0);

				// TODO 多维
				// [2 x [3 x i32]] [[3 x i32] [i32 1, i32 0, i32 0], [3 x i32] zeroinitializer]
				Stack<Integer> tmpStack = new Stack<>();
				for (int i = saveList.size() - 1; i >= 0; i--) {
					tmpStack.push(saveList.get(i));
				}
				String initString = generateGlobalArrayInitialString(0, type, expList, tmpStack);
				output(dsoG(tmpVar, initString), ctx);
				newGlobalArrayVar(tmpVar, --globalIndex, false, dimension, saveList, type, expList);
			} else {
				output(allocaArray(++index, expList), ctx);
				int addrIndex = index;

				//'{' ( initVal (',' initVal)* )? '}' 2+(1+2n)
				HelloParser.InitValContext initVal = ctx.initVal();
				// 初始化 如果需要的话
				ArrayList<Integer> saveList = new ArrayList<>();
				defArray = true;
				visitChildren(initVal);
				defArray = false;
				initialArray(initVal, type, expList, saveList, 0);

				// TODO 多维
				ArrayList<Integer> tmpArrayList = new ArrayList<>();
				for (int i = 0; i < dimension + 1; i++) {
					tmpArrayList.add(0);
				}
				output(getelementptr(++index, type, index - 1, tmpArrayList, ctx), ctx);
				for (int i = 0; i < saveList.size(); i++) {
					ArrayList<Integer> tmp = new ArrayList<>();
					// 获取偏移量
					if (i == 0) {
						tmp.add(0);
					} else {
						tmp.add(1);
					}
					//output(getelementptr(++index, subArrayType(type), index - 1, tmp), ctx);
					output(getelementptr(++index, "i32", index - 1, tmp, ctx), ctx);
					output(store(String.valueOf(saveList.get(i)), index), ctx);
				}
				newArrayVar(tmpVar, addrIndex, false, dimension, saveList, type, expList);
			}
		}
		return null;
	}

	public static String generateGlobalArrayInitialString(int layer, String type, ArrayList<Integer> expList,
		Stack<Integer> tmpStack) {
		String subType = subArrayType(type);
		String tmp = type;
		if ("i32".equals(tmp)) {
			return type + " " + tmpStack.pop();
		}
		tmp += " [";
		for (int j = 0; j < expList.get(layer); j++) {
			if (j != 0) {
				tmp += ", ";
			}
			tmp += generateGlobalArrayInitialString(layer + 1, subType, expList, tmpStack);
		}
		tmp += "]";
		//		System.out.println("tmp is " + tmp);
		return tmp;
	}

	public static void initialArray(HelloParser.InitValContext tmp, String type, ArrayList<Integer> expList,
		ArrayList<Integer> saveList, int layer) {
		//		output(getelementptr(++index, type, fromIndex, 0, 0), tmp);
		int count = tmp.getChildCount();
		if (count == 1) {
			// exp 补全0 ???
			saveList.add(Integer.parseInt(store.get(tmp.exp())));
			int totalCount = 1;
			for (int i = layer; i < expList.size(); i++) {
				totalCount *= expList.get(i);
			}
			for (int i = 0; i < totalCount - 1; i++) {
				saveList.add(0);
			}
		} else if (count == 2) {
			// empty array 补全0
			int totalCount = 1;
			for (int i = layer; i < expList.size(); i++) {
				totalCount *= expList.get(i);
			}
			for (int i = 0; i < totalCount; i++) {
				saveList.add(0);
			}
		} else {
			String subType = subArrayType(type);
			int subCount = (count - 3) / 2 + 1; // initVal 的数量
			if ("i32".equals(subType)) {
				int totalCount = expList.get(layer);
				for (int i = 0; i < subCount; i++) {
					saveList.add(Integer.parseInt(store.get(tmp.initVal(i))));
				}
				if (totalCount < subCount) {
					throw new RuntimeException("array overflow!");
				} else if (totalCount > subCount) {
					// 补全少的初值为0
					for (int i = 0; i < (totalCount - subCount); i++) {
						saveList.add(0);
					}
				}
			} else {
				// TODO 多维数组
				for (int i = 0; i < subCount; i++) {
					initialArray(tmp.initVal(i), subType, expList, saveList, layer + 1);
				}
				int totalCount = expList.get(layer);
				if (totalCount < subCount) {
					throw new RuntimeException("array overflow!");
				} else if (totalCount > subCount) {
					for (int i = 0; i < (totalCount - subCount); i++) {
						int total = 1;
						for (int j = layer + 1; j < expList.size(); j++) {
							total *= expList.get(j);
						}
						for (int j = 0; j < total; j++) {
							saveList.add(0);
						}
					}
				}
			}
		}
	}

	public static String subArrayType(String type) {
		if (type.contains("*")) {
			return type.substring(0, type.length() - 1);
		}
		type = type.substring(1, type.length() - 1);
		int start = type.indexOf("[");
		if (start == -1) {
			return "i32";
		}
		return type.substring(start);
	}

	/*
	 * Ident ( '[' constExp ']')* '=' constInitVal 3+3n
	 * const定义
	 * */
	@Override public Void visitConstDef(HelloParser.ConstDefContext ctx) {
		String tmpVar = ctx.Ident().getText();
		if (ctx.getChildCount() == 3) {
			constMode2 = true;
			visitChildren(ctx);
			constMode2 = false;
			if (isGlobal()) {
				// 全局变量
				newGlobalVar(tmpVar, store.get(ctx.constInitVal()), --globalIndex, true);
				output(dsoG(tmpVar, "i32 " + (store.get(ctx.constInitVal()) == null ? "0"
					: String.valueOf(store.get(ctx.constInitVal())))), ctx);
			} else {
				output(load(++index, getLocation(ctx.constInitVal())), ctx);
				output(alloca(++index), ctx);
				newVar(tmpVar, index, true, Integer.parseInt(store.get(ctx.constInitVal())));
				output(store(index - 1, index), ctx);
			}
		} else {
			// const array
			defArray = true;
			int dimension = (ctx.getChildCount() - 3) / 3;
			ArrayList<Integer> expList = new ArrayList<>(); // 保存的是维度信息
			for (int i = 0; i < dimension; i++) {
				visit(ctx.constExp(i));
				int tmpD = Integer.parseInt(store.get(ctx.constExp(i)));
				if (tmpD < 0) {
					throw new RuntimeException(tmpD + " is not allowed!");
				}
				expList.add(tmpD);
			}
			defArray = false;
			// 获取当前数组type
			String type = generateArrayType(expList);
			if (isGlobal()) {
				// global
				//@b = dso_local constant [2 x [3 x i32]] [[3 x i32] [i32 1, i32 0, i32 0], [3 x i32] zeroinitializer]

				//'{' ( constInitVal (',' constInitVal)* )? '}' 2+(1+2n)
				HelloParser.ConstInitValContext constInitVal = ctx.constInitVal();
				// 初始化 如果需要的话
				// output(getelementptr(++index, type, addrIndex, 0, 0), ctx);
				ArrayList<Integer> saveList = new ArrayList<>(); // 保存的是所有数据
				defArray = true;
				visitChildren(constInitVal);
				defArray = false;
				constInitialArray(constInitVal, type, expList, saveList, 0);

				// TODO 多维
				// [2 x [3 x i32]] [[3 x i32] [i32 1, i32 0, i32 0], [3 x i32] zeroinitializer]
				Stack<Integer> tmpStack = new Stack<>();
				for (int i = saveList.size() - 1; i >= 0; i--) {
					tmpStack.push(saveList.get(i));
				}
				String initString = generateGlobalArrayInitialString(0, type, expList, tmpStack);

				output(dsoC(tmpVar, initString), ctx);

				newGlobalArrayVar(tmpVar, --globalIndex, true, dimension, saveList, type, expList);
			} else {
				output(allocaArray(++index, expList), ctx);
				int addrIndex = index;

				//'{' ( initVal (',' initVal)* )? '}' 2+(1+2n)
				HelloParser.ConstInitValContext constInitVal = ctx.constInitVal();
				// 初始化 如果需要的话
				// output(getelementptr(++index, type, addrIndex, 0, 0), ctx);
				ArrayList<Integer> saveList = new ArrayList<>();
				defArray = true;
				constMode = true;
				visitChildren(constInitVal);
				constMode = false;
				defArray = false;
				constInitialArray(constInitVal, type, expList, saveList, 0);

				// TODO 多维
				ArrayList<Integer> tmpArrayList = new ArrayList<>();
				for (int i = 0; i < dimension + 1; i++) {
					tmpArrayList.add(0);
				}
				output(getelementptr(++index, type, index - 1, tmpArrayList, ctx), ctx);
				for (int i = 0; i < saveList.size(); i++) {
					ArrayList<Integer> tmp = new ArrayList<>();
					// change
					if (i == 0) {
						tmp.add(0);
					} else {
						tmp.add(1);
					}
					//output(getelementptr(++index, subArrayType(type), index - 1, tmp), ctx);
					output(getelementptr(++index, "i32", index - 1, tmp, ctx), ctx);
					output(store(String.valueOf(saveList.get(i)), index), ctx);
				}

				newArrayVar(tmpVar, addrIndex, true, dimension, saveList, type, expList);
			}
		}
		return null;
	}

	/*
	* constInitVal : constExp
                | '{' ( constInitVal (',' constInitVal)* )? '}';
    * */
	public static void constInitialArray(HelloParser.ConstInitValContext tmp, String type, ArrayList<Integer> expList,
		ArrayList<Integer> saveList, int layer) {
		//		output(getelementptr(++index, type, fromIndex, 0, 0), tmp);
		int count = tmp.getChildCount();
		if (count == 1) {
			// exp 补全0 ???
			saveList.add(Integer.parseInt(store.get(tmp.constExp())));
			int totalCount = 1;
			for (int i = layer; i < expList.size(); i++) {
				totalCount *= expList.get(i);
			}
			for (int i = 0; i < totalCount - 1; i++) {
				saveList.add(0);
			}
		} else if (count == 2) {
			// empty array 补全0
			int totalCount = 1;
			for (int i = layer; i < expList.size(); i++) {
				totalCount *= expList.get(i);
			}
			for (int i = 0; i < totalCount; i++) {
				saveList.add(0);
			}
		} else {
			String subType = subArrayType(type);
			int subCount = (count - 3) / 2 + 1; // initVal 的数量
			if (subType.equals("i32")) {
				int totalCount = expList.get(layer);
				for (int i = 0; i < subCount; i++) {
					saveList.add(Integer.parseInt(store.get(tmp.constInitVal(i))));
				}
				if (totalCount < subCount) {
					throw new RuntimeException("array overflow!");
				} else if (totalCount > subCount) {
					// 补全少的初值为0
					for (int i = 0; i < (totalCount - subCount); i++) {
						saveList.add(0);
					}
				}
			} else {
				// TODO 多维数组
				for (int i = 0; i < subCount; i++) {
					constInitialArray(tmp.constInitVal(i), subType, expList, saveList, layer + 1);
				}
				int totalCount = expList.get(layer);
				if (totalCount < subCount) {
					throw new RuntimeException("array overflow!");
				} else if (totalCount > subCount) {
					for (int i = 0; i < (totalCount - subCount); i++) {
						int total = 1;
						for (int j = layer + 1; j < expList.size(); j++) {
							total *= expList.get(j);
						}
						for (int j = 0; j < total; j++) {
							saveList.add(0);
						}
					}
				}
			}
		}
	}

	@Override public Void visitInitVal(HelloParser.InitValContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			// 只处理非数组情况
			location.put(ctx, getLocation(ctx.exp()));
			if (isGlobal()) {
				store.put(ctx, store.get(ctx.exp()));
			}
		}
		return null;
	}

	@Override public Void visitConstInitVal(HelloParser.ConstInitValContext ctx) {
		visitChildren(ctx);
		if (ctx.getChildCount() == 1) {
			// 只处理非数组情况
			location.put(ctx, getLocation(ctx.constExp()));
			if (isGlobal()) {
				store.put(ctx, store.get(ctx.constExp()));
			}
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
		// 乘除法
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

	/* unaryExp
	 *  表达式计算 要求是保存地址
	 * */
	@Override public Void visitCalcResES(HelloParser.CalcResESContext ctx) {
		//		visitChildren(ctx);
		String name = ctx.Ident().getText();
		visit(ctx.Ident());
		//		try {
		//			checkVoid(ctx);
		//		} catch (Exception e) {
		//			throw new RuntimeException("params illegal!");
		//		}
		if (ctx.getChildCount() == 3) {
			//Ident '(' ')' # calcResES
			Func f = findFuncByName(name);
			//			output(call(++index, name), ctx);
			//			output(alloca(++index), ctx);
			//			output(store(index - 1, index), ctx);
			if (f.type.equals("void")) {
				// void
				output(proCall(name, ctx), ctx);
			} else {
				// int
				output(proCall(++index, name, ctx), ctx);
				output(alloca(++index), ctx);
				output(store(index - 1, index), ctx);
			}
			location.put(ctx, index);
		} else {
			//Ident '(' funcRParams ')' # calcResES
			//			output(load(++index, getLocation(ctx.funcRParams())), ctx);
			// TODO 获取参数列表
			ArrayList<Var> params = new ArrayList<>();
			tmpCallFuncParamsStack.push(params);
			// 让funcRParams的所有参数都加入peek的arraylist
			visit(ctx.funcRParams());
			Func f = findFuncByName(name);
			// TODO 函数返回值
			if (f.type.equals("void")) {
				// void
				output(proCall(name, params, ctx), ctx);
			} else {
				// int
				int retIndex = ++index;
				output(proCall(retIndex, name, params, ctx), ctx);
				int allocaIndex = ++index;
				output(alloca(allocaIndex), ctx);
				output(store(retIndex, allocaIndex), ctx);
			}
			location.put(ctx, index);
			tmpCallFuncParamsStack.pop();
		}
		return null;
	}

	/* 函数参数
	 * exp (',' exp)*; 1+2n
	 */
	@Override public Void visitFuncRParams(HelloParser.FuncRParamsContext ctx) {
		int paramCount = (ctx.getChildCount() - 1) / 2 + 1;
		visitChildren(ctx);
		//TODO params加入list，验证类型，只用传地址
		for (int i = 0; i < paramCount; i++) {
			ArrayList<Var> tmpList = tmpCallFuncParamsStack.peek();
			int tmpIndex = getLocation(ctx.exp(i));
			if (getVarById(tmpIndex) == null) {
				//说明不是数组
				Var v = new Var();
				v.index = tmpIndex;
				tmpList.add(v);
			} else {
				//数组
				tmpList.add(getVarById(tmpIndex));
			}
		}
		// ???
		location.put(ctx, getLocation(ctx.exp(0)));
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
				if (isGlobal()) {
					int tmp = Integer.parseInt(store.get(ctx.unaryExp())) * (-1);
					store.put(ctx, String.valueOf(tmp));
					break;
				}
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
		// to avoid none-nes error
		location.put(ctx, index);
		return null;
	}

	//Ident ( '[' exp ']')* 1+3n
	@Override public Void visitLVal(HelloParser.LValContext ctx) {
		visitChildren(ctx);
		String ident = ctx.Ident().getText();
		if (ctx.getChildCount() == 1) {
			// Ident
			if (constMode) {
				if (!getVarByName(ident).isConst) {
					throw new RuntimeException(ident + " should be ident");
				}
			}
			Var v = getVarByName(ident);
			if (v.isArray) {
				String tmpType = v.type;
				int retIndex = ++index;
				debugSout(ctx, "type " + tmpType + " " + retIndex);
				output(getelementptrAddr(subArrayType(v.type) + "*", v, retIndex, tmpType, v.index, new ArrayList<>(),
					ctx), ctx);
				Var newV = Var.copyVar(v);
				newArrayVar(newV.name + retIndex + newV.name, retIndex, newV.isConst, newV.dimension, newV.arrValues,
					tmpType, newV.dimensionList);
				location.put(ctx, retIndex);
			} else {
				location.put(ctx, v.index);
			}
			if (isGlobal()) {
				store.put(ctx, String.valueOf(v.value));
			}
		} else {
			//Ident ( '[' exp ']')*
			// 获取数组的值 Global
			Var tmpArrVar = getVarByName(ident);
			int expCount = (ctx.getChildCount() - 1) / 3;
			ArrayList<Integer> expIndexList = new ArrayList<>();
			ArrayList<Integer> dimensionList = tmpArrVar.dimensionList;
			if (isGlobal()) {
				for (int i = 0; i < expCount; i++) {
					// 全局 直接编译器计算出值
					expIndexList.add(Integer.parseInt(store.get(ctx.exp(i))));
				}
				int tmpIndex = 0;
				if (dimensionList.size() > expCount) {
					//global 这个情况是不是就是报错
					throw new RuntimeException("global dimension error?");
				}
				// 根据偏移量计算出数据所在的偏移量tmpIndex
				for (int i = expCount - 1; i >= 0; i--) {
					if (i == expCount - 1) {
						tmpIndex += expIndexList.get(i);
						continue;
					}
					tmpIndex += expIndexList.get(i) * dimensionList.get(i + 1);
				}
				store.put(ctx, String.valueOf(tmpArrVar.arrValues.get(tmpIndex)));
				// array index TODO 不对的 但不一定用得到 全局的指针
				location.put(ctx, tmpArrVar.index);
			} else {
				if (constMode) {
					if (!getVarByName(ident).isConst) {
						throw new RuntimeException(ident + " should be ident");
					}
				}
				for (int i = 0; i < expCount; i++) {
					int expTmpIndex = location.get(ctx.exp(i));
					output(load(++index, expTmpIndex), ctx);
					expIndexList.add(index);
				}
				if (dimensionList.size() > expCount) {
					int n = dimensionList.size() - expCount;
					// TODO 少n个维度，作为函数参数 如果是a[][2]这种情况怎么办，好像不可能
					//%4 = getelementptr [2 x [2 x [2 x i32]]], [2 x [2 x [2 x i32]]]* %2, i32 0, i32 1 对应a[][][] 的 a[1]
					ArrayList<Integer> tmpBias = new ArrayList<>(); // 获取当前新数组地址的bias
					ArrayList<Integer> tmpLeftBias = new ArrayList<>(); // 新数组的bias
					// 先来个 i32的寄存器地址
					//					output(alloca(++index), ctx);
					//					output(store("0", index), ctx);
					//					output(load(++index, index - 1), ctx);
					//					tmpBias.add(index);
					// 存储所有偏移 比如a[2][2] 那这里存 2 2的地址 再load
					for (int i = 0; i < expCount; i++) {
						int tmpIndex = expIndexList.get(i);
						//						output(load(++index,tmpIndex),ctx);
						tmpBias.add(tmpIndex);
					}
					for (int i = 0; i < dimensionList.size(); i++) {
						if (i < expCount) {
							continue;
						}
						tmpLeftBias.add(dimensionList.get(i));
					}
					//TODO type + *
					String tmpType = tmpArrVar.type;
					for (int i = 0; i <= expCount; i++) {
						tmpType = subArrayType(tmpType);
					}
					tmpType += "*";

					int retIndex = ++index;
					debugSout(ctx, "type " + tmpType + " " + retIndex);
					output(
						getelementptrAddr(tmpType, tmpArrVar, retIndex, tmpArrVar.type, tmpArrVar.index, tmpBias, ctx),
						ctx);
					ArrayList<Integer> tmpValueArr = new ArrayList<>();
					newArrayVar(tmpArrVar.name + retIndex + tmpArrVar.name, retIndex, false,
						dimensionList.size() - expCount, tmpValueArr, tmpType, tmpLeftBias);
					location.put(ctx, retIndex);
					return null;
				}
				int retIndex = ++index;
				debugSout(ctx, "type " + tmpArrVar.type + " " + retIndex);
				output(
					getelementptrAddr("i32", tmpArrVar, retIndex, tmpArrVar.type, tmpArrVar.index, expIndexList, ctx),
					ctx);
				location.put(ctx, retIndex);
			}
		}
		return null;
	}
}
