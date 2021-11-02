import java.util.List;

public class Recur {
	private static int index = 0;

	/**
	 * 文法识别->执行对应函数
	 * */
	public static String executeV(String s, List<String> list, int size) {
		try {
			switch (s) {
				case V.CompUnit:
					return isCompUnit(list);
				case V.FuncDef:
					return isFuncDef(list);
				case V.FuncType:
					return isFuncType(list);
				case V.INT:
					return isVT(list, V.INT);
				case V.Ident:
					return isIdent(list);
				case V.MAIN:
					return isVT(list, V.MAIN);
				case V.SBL:
					return isVT(list, V.SBL);
				case V.SBR:
					return isVT(list, V.SBR);
				case V.Block:
					return isBlock(list);
				case V.LBL:
					return isVT(list, V.LBL);
				case V.LBR:
					return isVT(list, V.LBR);
				case V.Stmt:
					return isStmt(list);
				case V.RETURN:
					return isVT(list, V.RETURN);
				case V.Number:
					return isNumber(list);
				case V.SEMICOLON:
					return isVT(list, V.SEMICOLON);
				case V.DecConst:
					return isDecConst(list);
				case V.OctConst:
					return isOctConst(list);
				case V.HexConst:
					return isHexConst(list);
				case V.NoneZeroDigit:
					return isNoneZeroDigit(list);
				case V.Digit:
					return isDigit(list);
				case V.OctDigit:
					return isOctDigit(list);
				case V.HexPre:
					return isHexPre(list);
				case V.HexDigit:
					return isHexDigit(list);
				//
				//
				//
				case V.EPS:
					//					System.out.println("index"+index+"epseps");
					return "";
				case V.Exp:
					return isExp(list);
				case V.AddExp:
					return isAddExp(list);
				case V.AddExpPlus:
					return isAddExpPlus(list);
				case V.MulExp:
					return isMulExp(list);
				case V.MulExpPlus:
					return isMulExpPlus(list);
				case V.UnaryExp:
					return isUnaryExp(list);
				case V.PrimaryExp:
					return isPrimaryExp(list);
				case V.UnaryOp:
					return isUnaryOp(list);
				case V.PLUS:
					return isVT(list, V.PLUS);
				case V.PLUSPLUS:
					//					System.out.println("index"+index+"plusplus");
					return isVT(list, V.PLUSPLUS);
				case V.MINUS:
					return isVT(list, V.MINUS);
				case V.MINUSMINUS:
					//					System.out.println("index"+index+"minusminus");
					return isVT(list, V.MINUSMINUS);
				case V.MUL:
					return isVT(list, V.MUL);
				case V.DIV:
					return isVT(list, V.DIV);
				case V.MOD:
					return isVT(list, V.MOD);
					//
				//
				//
				case V.Decl:
					return isDecl(list);
				case V.ConstDecl:
					return isConstDecl(list);
				case V.ConstDefs:
					return isConstDefs(list);
				case V.BType:
					return isBType(list);
				case V.ConstDef:
					return isConstDef(list);
				case V.ConstInitVal:
					return isConstInitVal(list);
				case V.ConstExp:
					return isConstExp(list);
				case V.VarDecl:
					return isVarDecl(list);
				case V.VarDefs:
					return isVarDefs((list));
				case V.VarDef:
					return isVarDef(list);
				case V.InitVal:
					return isInitVal((list));
				case V.BlockItem:
					return isBlockItem(list);
				case V.BlockItems:
					return isBlockItems(list);
				case V.LVal:
					return isLVal(list);
				case V.FuncRParams:
					return isFuncRParams(list);
				case V.Exps:
					return isExps(list);
				case V.COMMA:
					return isVT(list,V.COMMA);
				case V.EQUAL:
					return isVT(list,V.EQUAL);
			}
		} catch (Exception e) {
			//			System.out.println("Exception " + e);
			return null;
		}
		//		System.out.println("No match");
		System.exit(-1);
		return "";
	}

	public static boolean isVN(String type) {
		switch (type) {
			case V.CompUnit:
			case V.FuncDef:
			case V.Ident:
			case V.FuncType:
			case V.Block:
			case V.Stmt:
			case V.DecConst:
			case V.OctConst:
			case V.HexConst:
			case V.NoneZeroDigit:
			case V.Digit:
			case V.OctDigit:
			case V.HexPre:
			case V.HexDigit:
			case V.Exp:
			case V.AddExp:
			case V.AddExpPlus:
			case V.MulExp:
			case V.MulExpPlus:
			case V.UnaryExp:
			case V.PrimaryExp:
			case V.UnaryOp:
			//eps
			case V.EPS:
				return true;
			//vt
			case V.Number:
			case V.INT:
			case V.MAIN:
			case V.SBL:
			case V.SBR:
			case V.LBL:
			case V.LBR:
			case V.PLUS:
			case V.PLUSPLUS:
			case V.MINUS:
			case V.MINUSMINUS:
			case V.MUL:
			case V.DIV:
			case V.MOD:
			case V.RETURN:
			case V.SEMICOLON:
				return false;
		}
		return false;
	}

	public static String isVT(List<String> list, String string) {
		//		System.out.println(list.get(index)+" isVT "+string);
		if (!list.get(index).equals(string)) {
			if (string.equals("++")) {
				if (list.get(index).equals("+")) {
					//					index++;
					return "++";
				}
			} else if (string.equals("--")) {
				if (list.get(index).equals("-")) {
					//					index++;
					return "--";
				}
			}
			return null;
		} else {
			//			index++;
			switch (string) {
				case V.INT:
					return "define dso_local i32 ";
				case V.MAIN:
					return "@main";
				case V.RETURN:
					return "\tret i32 ";
				case V.SBL:
					return "(";
				case V.SBR:
					return ")";
				case V.LBL:
					return "{\n\r";
				case V.LBR:
					return "}";
				case V.SEMICOLON:
					return "\n";
				//
				case V.PLUS:
					return "+";
				case V.MINUS:
					return "-";
				case V.MUL:
					return "*";
				case V.DIV:
					return "/";
				case V.MOD:
					return "%";
					//
				case V.EQUAL:
					return " = ";
				case V.COMMA:
					return ", ";
				default:
					//					index--;
					return null;
			}
		}
	}

	public static String showG(String[] g) {
		String ret = "[";
		for (String s : g) {
			ret += s;
			ret += " ";
		}
		return ret + "]";
	}

	public static String executeG(String[][] grammar, List<String> list) {
		String ret = null;
		for (String[] g : grammar) {
//			System.out.println("---try " + showG(g) + " ing");
			ret = catString(g, list);
//			System.out.println("---result " + ret);
			if (ret == null) {
//				System.out.println("!!! " + showG(g) + " not work");
				continue;
			}
			return ret;
		}
		return null;
	}

	public static void recurIndex(String[] grammar, int ii) {
		for (int i = 0; i < ii; i++) {
			if (!isVN(grammar[i])) {
//				System.out.println("RRRecur"+grammar[i]+index);
				index--;
			}
		}
	}

	public static String catString(String[] grammar, List<String> list) {
		String ret = "";
		for (int i = 0; i < grammar.length; i++) {
			try {
				String s = executeV(grammar[i], list, list.size());
				if (s == null) {
					recurIndex(grammar, i);
					return null;
				} else {
					if (!isVN(grammar[i])) {
//						System.out.println("???grammar" + grammar[i]+index);
						index++;
					}
				}
				ret += s;
			} catch (Exception e) {
				return null;
			}
		}
		return ret;
	}

	public static String isCompUnit(List<String> list) {
		String[] grammar = {V.FuncDef};
		String s = catString(grammar, list);
		if (s == null) {
			System.exit(-1);
		}
		if (index == list.size()) {
			return s;
		} else {
			System.exit(-1);
			return "err";
		}
	}

	public static String isFuncDef(List<String> list) {
		String[] grammar = {V.FuncType, V.Ident, V.SBL, V.SBR, V.Block};
		return catString(grammar, list);
	}

	public static String isFuncType(List<String> list) {
		String[] grammar = {V.INT};
		return catString(grammar, list);
	}

	public static String isIdent(List<String> list) {
		String[] grammar = {V.MAIN};
		return catString(grammar, list);
	}

	public static String isBlock(List<String> list) {
		String[] grammar = {V.LBL, V.BlockItems, V.LBR};
		return catString(grammar, list);
	}

	public static String isBlockItems(List<String> list) {
		String[][] grammar = {{V.Decl,V.BlockItems},{V.Stmt,V.BlockItems},{V.BlockItem},{V.EPS}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isBlockItem(List<String> list) {
		String[][] grammar = {{V.Decl},{V.Stmt}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isDecl(List<String> list) {
		String[][] grammar = {{V.ConstDecl},{V.VarDecl}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isConstDecl(List<String> list) {
		String[][] grammar = {{V.CONST,V.BType,V.ConstDef,V.ConstDefs,V.SEMICOLON}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isConstDefs(List<String> list) {
		String[][] grammar = {{V.COMMA,V.ConstDef},{V.COMMA,V.ConstDef,V.ConstDefs},{V.EPS}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isConstDef(List<String> list) {
		String[][] grammar = {{V.Ident,V.EQUAL,V.ConstInitVal}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isBType(List<String> list) {
		String[][] grammar = {{V.INT}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isConstInitVal(List<String> list) {
		String[][] grammar = {{V.ConstExp}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isConstExp(List<String> list) {
		String[][] grammar = {{V.AddExp}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isVarDecl(List<String> list) {
		String[][] grammar = {{V.BType,V.VarDef,V.VarDefs,V.SEMICOLON}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isVarDefs(List<String> list) {
		String[][] grammar = {{V.COMMA,V.VarDef},{V.COMMA,V.VarDef,V.VarDefs},{V.EPS}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isVarDef(List<String> list) {
		String[][] grammar = {{V.Ident},{V.Ident,V.EQUAL,V.InitVal}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isInitVal(List<String> list) {
		String[][] grammar = {{V.Exp}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isStmt(List<String> list) {
		String[][] grammar = {{V.LVal,V.EQUAL,V.Exp,V.SEMICOLON},{V.SEMICOLON},{V.Exp,V.SEMICOLON},{V.RETURN, V.Exp, V.SEMICOLON}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isLVal(List<String> list) {
		String[][] grammar = {{V.Ident}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isExp(List<String> list) {
		String[][] grammar = {{V.AddExp}};
		String s = executeG(grammar, list);
		return s;
	}

	public static String isAddExp(List<String> list) {
		String[][] grammar = {{V.MulExp, V.AddExpPlus}};
		String s = executeG(grammar, list);
//		System.out.println("isAddExp " + s);
		return getPlus(s);
	}

	public static String isAddExpPlus(List<String> list) {
		String[][] grammar = {{V.PLUSPLUS, V.MulExp, V.AddExpPlus}, {V.MINUSMINUS, V.MulExp, V.AddExpPlus}, {V.EPS}};
		String s = executeG(grammar, list);
		//		System.out.println("index" + index);
		//		System.out.println("isAddExpPlus " + s);
		//		if (s == null) {
		//			index--;
		//			return null;
		//		}
		return s;
	}

	public static String isMulExp(List<String> list) {
		String[][] grammar = {{V.UnaryExp, V.MulExpPlus}};
		String s = executeG(grammar, list);
		//		System.out.println("isME " + s);
		return getMul(s);
	}

	public static String isMulExpPlus(List<String> list) {
		String[][] grammar =
			{{V.MUL, V.UnaryExp, V.MulExpPlus}, {V.DIV, V.UnaryExp, V.MulExpPlus}, {V.MOD, V.UnaryExp, V.MulExpPlus},
				{V.EPS}};
		String s = executeG(grammar, list);
		//		System.out.println("isMEP " + s);
		return s;
	}

	public static String isUnaryExp(List<String> list) {
		String[][] grammar = {{V.PrimaryExp}, {V.Ident,V.SBL,V.SBR}, {V.Ident,V.SBL,V.FuncRParams,V.SBR},{V.UnaryOp, V.UnaryExp}};
		String s = executeG(grammar, list);
		//		System.out.println("isUnaryExp " + s);
		if (s == null) {
			return null;
		}
		int flag = 1;
		String ss = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '-') {
				flag = -flag;
			} else if (s.charAt(i) == '+') {
				continue;
			} else {
				ss += s.charAt(i);
			}
		}
		return ss.equals("0") ? "0" : flag > 0 ? ss : "-" + ss;
	}

	public static String isFuncRParams(List<String> list) {
		String[][] grammar = {{V.Exp,V.Exps}};
		String s = executeG(grammar, list);
		//		System.out.println("isUnaryExp " + s);
		return s;
	}

	public static String isExps(List<String> list) {
		String[][] grammar = {{V.SEMICOLON,V.Exp},{V.SEMICOLON,V.Exps},{V.EPS}};
		String s = executeG(grammar, list);
		//		System.out.println("isUnaryExp " + s);
		return s;
	}

	public static String isPrimaryExp(List<String> list) {
		String[][] grammar = {{V.SBL, V.Exp, V.SBR}, {V.Number}};
		String s = executeG(grammar, list);
		if (s == null) {
			return null;
		}
		if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
			return s.substring(1, s.length() - 1);
		}
		//		System.out.println("isPrimaryExp " + s);
		return s;
	}

	public static String isUnaryOp(List<String> list) {
		String[][] grammar = {{V.PLUS}, {V.MINUS}};
		String s = executeG(grammar, list);
		//		System.out.println("isUnaryOp " + s);
		return s;
	}

	public static String isNumber(List<String> list) {
		//		String tmp = list.get(index++);
		String tmp = list.get(index);
		//		System.out.println("isNumber "+tmp);
		try {
			Integer.parseInt(tmp);
		} catch (NumberFormatException e) {
			//			index--;
			return null;
		}
		//		return "i32 " + tmp;
		return tmp;
	}

	public static String isDecConst(List<String> list) {
		return "";
	}

	public static String isOctConst(List<String> list) {
		return "";
	}

	public static String isHexConst(List<String> list) {
		return "";
	}

	public static String isNoneZeroDigit(List<String> list) {
		return "";
	}

	public static String isDigit(List<String> list) {
		return "";
	}

	public static String isOctDigit(List<String> list) {
		return "";
	}

	public static String isHexPre(List<String> list) {
		return "";
	}

	public static String isHexDigit(List<String> list) {
		return "";
	}

	public static String getMul(String s) {
		if (s == null) {
			return null;
		}
		try {
			if (s.contains("*")) {
				String[] nums = s.split("\\*");
				for (int i = 0; i < nums.length; i++) {
					if (nums[i].contains("/") || nums[i].contains("%")) {
						nums[i] = getMul(nums[i]);
						if (nums[i] == null) {
							return null;
						}
					}
				}
				int result = 1;
				for (String num : nums) {
					result *= Integer.parseInt(num);
				}
				return String.valueOf(result);
			} else if (s.contains("/")) {
				String[] nums = s.split("/");
				for (int i = 0; i < nums.length; i++) {
					if (nums[i].contains("*") || nums[i].contains("%")) {
						nums[i] = getMul(nums[i]);
						if (nums[i] == null) {
							return null;
						}
					}
				}
				int result = 1;
				for (int i = 0; i < nums.length; i++) {
					if (i == 0) {
						result = Integer.parseInt(nums[i]);
						continue;
					}
					result /= Integer.parseInt(nums[i]);
				}
				return String.valueOf(result);
			} else if (s.contains("%")) {
				String[] nums = s.split("%");
				for (int i = 0; i < nums.length; i++) {
					if (nums[i].contains("*") || nums[i].contains("/")) {
						nums[i] = getMul(nums[i]);
						if (nums[i] == null) {
							return null;
						}
					}
				}
				int result = 1;
				for (int i = 0; i < nums.length; i++) {
					if (i == 0) {
						result = Integer.parseInt(nums[i]);
						continue;
					}
					result %= Integer.parseInt(nums[i]);
				}
				return String.valueOf(result);
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return s;
	}

	public static String getPlus(String s) {
		if (s == null) {
			return null;
		}
		//		System.out.println("getPlus " + s);
		try {
			if (s.contains("++")) {
				String[] nums = s.split("\\+\\+");
				if (nums.length == 1) {
					return null;
				}
				for (int i = 0; i < nums.length; i++) {
					if (nums[i].contains("--")) {
						nums[i] = getPlus(nums[i]);
						if (nums[i] == null) {
							return null;
						}
					}
				}
				int result = 0;
				for (String num : nums) {
					result += Integer.parseInt(num);
				}
				return String.valueOf(result);
			} else if (s.contains("--")) {
				String[] nums = s.split("--");
				if (nums.length == 1) {
					return null;
				}
				for (int i = 0; i < nums.length; i++) {
					if (nums[i].contains("++")) {
						nums[i] = getPlus(nums[i]);
						if (nums[i] == null) {
							return null;
						}
					}
				}
				int result = 0;
				for (int i = 0; i < nums.length; i++) {
					if (i == 0) {
						result = Integer.parseInt(nums[i]);
						continue;
					}
					result -= Integer.parseInt(nums[i]);
				}
				return String.valueOf(result);
			}
		} catch (NumberFormatException e) {
			//			System.out.println(e);
			return null;
		}
		return s;
	}
}
