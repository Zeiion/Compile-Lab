import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Recur {
	private static int index = 0;

	private static HashMap<String, Integer> integerHashMap = new HashMap<>();
	private static int regIndex = 1;

	/**
	 * 文法识别->执行对应函数
	 */
	public static String executeV(String s, List<String> list, int layer) {
		try {
			switch (s) {
				case V.CompUnit:
					return isCompUnit(list, layer);
				case V.FuncDef:
					return isFuncDef(list, layer);
				case V.FuncType:
					return isFuncType(list, layer);
				case V.INT:
					return isVT(list, V.INT);
				case V.Ident:
					return isIdent(list, layer);
				case V.IdentDef:
					return isIdentDef(list, layer);
				case V.IdentPlus:
					return isIdentPlus(list, layer);
				case V.MAIN:
					return isVT(list, V.MAIN);
				case V.SBL:
					return isVT(list, V.SBL);
				case V.SBR:
					return isVT(list, V.SBR);
				case V.Block:
					return isBlock(list, layer);
				case V.LBL:
					return isVT(list, V.LBL);
				case V.LBR:
					return isVT(list, V.LBR);
				case V.Stmt:
					return isStmt(list, layer);
				case V.RETURN:
					return isVT(list, V.RETURN);
				case V.Number:
					return isNumber(list, layer);
				case V.SEMICOLON:
					return isVT(list, V.SEMICOLON);
				case V.DecConst:
					return isDecConst(list, layer);
				case V.OctConst:
					return isOctConst(list, layer);
				case V.HexConst:
					return isHexConst(list, layer);
				case V.NoneZeroDigit:
					return isNoneZeroDigit(list, layer);
				case V.NoneDigit:
					return isNoneDigit(list, layer);
				case V.Digit:
					return isDigit(list, layer);
				case V.OctDigit:
					return isOctDigit(list, layer);
				case V.HexPre:
					return isHexPre(list, layer);
				case V.HexDigit:
					return isHexDigit(list, layer);
				//
				//
				//
				case V.EPS:
					//					sout(layer,"index"+index+"epseps");
					return "";
				case V.Exp:
					return isExp(list, layer);
				case V.AddExp:
					return isAddExp(list, layer);
				case V.AddExpPlus:
					return isAddExpPlus(list, layer);
				case V.MulExp:
					return isMulExp(list, layer);
				case V.MulExpPlus:
					return isMulExpPlus(list, layer);
				case V.UnaryExp:
					return isUnaryExp(list, layer);
				case V.PrimaryExp:
					return isPrimaryExp(list, layer);
				case V.UnaryOp:
					return isUnaryOp(list, layer);
				case V.PLUS:
					return isVT(list, V.PLUS);
				case V.PLUSPLUS:
					//					sout(layer,"index"+index+"plusplus");
					return isVT(list, V.PLUSPLUS);
				case V.MINUS:
					return isVT(list, V.MINUS);
				case V.MINUSMINUS:
					//					sout(layer,"index"+index+"minusminus");
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
					return isDecl(list, layer);
				case V.ConstDecl:
					return isConstDecl(list, layer);
				case V.ConstDefs:
					return isConstDefs(list, layer);
				case V.BType:
					return isBType(list, layer);
				case V.ConstDef:
					return isConstDef(list, layer);
				case V.ConstInitVal:
					return isConstInitVal(list, layer);
				case V.ConstExp:
					return isConstExp(list, layer);
				case V.VarDecl:
					return isVarDecl(list, layer);
				case V.VarDefs:
					return isVarDefs(list, layer);
				case V.VarDef:
					return isVarDef(list, layer);
				case V.InitVal:
					return isInitVal(list, layer);
				case V.BlockItem:
					return isBlockItem(list, layer);
				case V.BlockItems:
					return isBlockItems(list, layer);
				case V.LVal:
					return isLVal(list, layer);
				case V.FuncRParams:
					return isFuncRParams(list, layer);
				case V.Exps:
					return isExps(list, layer);
				case V.CONST:
					return isVT(list, V.CONST);
				case V.COMMA:
					return isVT(list, V.COMMA);
				case V.EQUAL:
					return isVT(list, V.EQUAL);

			}
		} catch (Exception e) {
			//			sout(layer,"Exception " + e);
			return null;
		}
		//		sout(layer,"No match");
		System.exit(-1);
		return "";
	}

	public static boolean isVN(String type) {
		switch (type) {
			//vt
			case V.Number:
			case V.INT:
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
			case V.CONST:
			case V.COMMA:
			case V.EQUAL:
			case V.Ident:
				return false;
			default:
				return true;
		}
	}

	public static String isVT(List<String> list, String string) {
		if (!list.get(index).equals(string)) {
			if (string.equals("++")) {
				if (list.get(index).equals("+")) {
					return "++";
				}
			} else if (string.equals("--")) {
				if (list.get(index).equals("-")) {
					return "--";
				}
			}
			return null;
		} else {
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
		String ret = "[ ";
		for (String s : g) {
			ret += s;
			ret += " ";
		}
		return ret + " ]";
	}

	public static String executeG(String[][] grammar, List<String> list, int layer) {
		String ret = null;
		for (String[] g : grammar) {
			sout(layer, "<p>try " + showG(g) + " ing");
			sout(layer, "now index is " + index);
			ret = catString(g, list, layer + 1);
			if (ret == null) {
				sout(layer, "</p>" + showG(g) + " fail");
				sout(layer, "now index is " + index);
				continue;
			}
			sout(layer, "</p>!!! " + ret);
			sout(layer, "now index is " + index);
			return ret;
		}
		return null;
	}

	public static void recurIndex(String[] grammar, int ii) {
		for (int i = 0; i < ii; i++) {
			if (!isVN(grammar[i])) {
				//				sout(layer,"RRRecur"+grammar[i]+index);
				index--;
			}
		}
	}

	public static String catString(String[] grammar, List<String> list, int layer) {
		String ret = "";
		int backup = index;
		for (int i = 0; i < grammar.length; i++) {
			try {
				String s = executeV(grammar[i], list, layer);
				if (s == null) {
					index = backup;
					return null;
				} else {
					if (!isVN(grammar[i])) {
						index++;
					}
				}
				ret += s;
			} catch (Exception e) {
				index = backup;
				return null;
			}
		}
		return ret;
	}

	public static String isCompUnit(List<String> list, int layer) {
		String[] grammar = {V.FuncDef};
		String s = catString(grammar, list, 0);
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

	public static String isFuncDef(List<String> list, int layer) {
		String[][] grammar = {{V.FuncType, V.Ident, V.SBL, V.SBR, V.Block}};
		return executeG(grammar, list, 0);
	}

	public static String isFuncType(List<String> list, int layer) {
		String[][] grammar = {{V.INT}};
		return executeG(grammar, list, layer);
	}

	public static String isIdent(List<String> list, int layer) {
		//		String[][] grammar = {{V.NoneDigit, V.IdentPlus}};
		//		return executeG(grammar, list, layer);
		String s = list.get(index);
		if (!isVal(s)) {
			sout(layer, s);
			return null;
		}

		return s;
	}

	public static String isIdentDef(List<String> list, int layer) {
		String s = list.get(index);
		if (!isVal(s)) {
			sout(layer, s);
			return null;
		}
		return s;
	}

	public static String DefIdent(List<String> list, List<String> def, int layer) {
		index--;
		String type = isBType(list, layer);
		index++;
		if (type == null) {
			return null;
		}
		switch (type) {
			case V.INT:
				for (int i = 1; i < def.size() - 1; i++) {
					try {
						if(def.get(i).equals(",")){
							continue;
						}
						ArrayList<String> tmp = new ArrayList<>();
						while(i<def.size()-1 && !def.get(i).equals(",")){
							tmp.add(def.get(i));
							i++;
						}
						if(tmp.size()==1){
							integerHashMap.put(tmp.get(0), regIndex);
							sout(layer, "%x" + regIndex + " = alloca i32\n");
							regIndex++;
						}else{
							int value = Integer.parseInt(tmp.get(2));
							sout(layer, "%x" + regIndex + " = alloca i32\n");
							sout(layer, "store i32 " + value + ", i32* %x" + regIndex);
							integerHashMap.put(tmp.get(0), regIndex);
							regIndex++;
						}
					} catch (Exception e) {
						return null;
					}
				}
				return "";
			default:
				return null;
		}
	}

	public static String DefConstIdent(List<String> list, List<String> def, int layer) {
		index--;
		String type = isBType(list, layer);
		index++;
		if (type == null) {
			return null;
		}
		switch (type) {
			case V.INT:
				for (int i = 1; i < def.size() - 1; i += 4) {
					try {
						String tmp = def.get(i);
						int value = Integer.parseInt(def.get(i + 2));
						sout(layer, "%x" + regIndex + " = alloca i32\n");
						sout(layer, "store i32 " + value + ", i32* %x" + regIndex);
						integerHashMap.put(tmp, regIndex);
						regIndex++;
					} catch (Exception e) {
						return null;
					}
				}
				return "";
			default:
				return null;
		}
	}

	public static String isIdentPlus(List<String> list, int layer) {
		sout(layer, "isIdentPlus");
		String[][] grammar = {{V.NoneDigit, V.IdentPlus}, {V.Digit, V.IdentPlus}, {V.EPS}};
		return executeG(grammar, list, layer);
	}

	public static String isBlock(List<String> list, int layer) {
		String[][] grammar = {{V.LBL, V.BlockItems, V.LBR}};
		String s = executeG(grammar, list, layer);
		sout(layer, "isBlock" + s);
		return s;
	}

	public static String isBlockItems(List<String> list, int layer) {
		String[][] grammar = {{V.Decl, V.BlockItems}, {V.Stmt, V.BlockItems}, {V.BlockItem}, {V.EPS}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isBlockItem(List<String> list, int layer) {
		String[][] grammar = {{V.Decl}, {V.Stmt}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isDecl(List<String> list, int layer) {
		String[][] grammar = {{V.ConstDecl}, {V.VarDecl}};
		String s = executeG(grammar, list, layer);
		if (s != null) {
			ArrayList<String> def = new ArrayList<>(Arrays.asList(s.split("\\s+")));
			switch (def.get(0)) {
				case V.CONST:
					DefConstIdent(list, def.subList(1, def.size()), layer);
					break;
				default:
					DefIdent(list, def.subList(0, def.size()), layer);
					break;
			}
		}
		return s;
	}

	public static String isConstDecl(List<String> list, int layer) {
		String[][] grammar = {{V.CONST, V.BType, V.ConstDef, V.ConstDefs, V.SEMICOLON}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isConstDefs(List<String> list, int layer) {
		String[][] grammar = {{V.COMMA, V.ConstDef, V.ConstDefs}, {V.COMMA, V.ConstDef}, {V.EPS}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isConstDef(List<String> list, int layer) {
		String[][] grammar = {{V.IdentDef, V.EQUAL, V.ConstInitVal}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isBType(List<String> list, int layer) {
		String[][] grammar = {{V.INT}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isConstInitVal(List<String> list, int layer) {
		String[][] grammar = {{V.ConstExp}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isConstExp(List<String> list, int layer) {
		String[][] grammar = {{V.AddExp}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isVarDecl(List<String> list, int layer) {
		String[][] grammar = {{V.BType, V.VarDef, V.VarDefs, V.SEMICOLON}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isVarDefs(List<String> list, int layer) {
		String[][] grammar = {{V.COMMA, V.VarDef, V.VarDefs}, {V.COMMA, V.VarDef}, {V.EPS}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isVarDef(List<String> list, int layer) {
		String[][] grammar = {{V.IdentDef, V.EQUAL, V.InitVal}, {V.Ident}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isInitVal(List<String> list, int layer) {
		String[][] grammar = {{V.Exp}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isStmt(List<String> list, int layer) {
		String[][] grammar = {{V.LVal, V.EQUAL, V.Exp, V.SEMICOLON}, {V.SEMICOLON}, {V.Exp, V.SEMICOLON},
			{V.RETURN, V.Exp, V.SEMICOLON}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isLVal(List<String> list, int layer) {
		String[][] grammar = {{V.Ident}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isExp(List<String> list, int layer) {
		String[][] grammar = {{V.AddExp}};
		String s = executeG(grammar, list, layer);
		return s;
	}

	public static String isAddExp(List<String> list, int layer) {
		String[][] grammar = {{V.MulExp, V.AddExpPlus}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isAddExp " + s);
		return getPlus(s, layer);
	}

	public static String isAddExpPlus(List<String> list, int layer) {
		String[][] grammar = {{V.PLUSPLUS, V.MulExp, V.AddExpPlus}, {V.MINUSMINUS, V.MulExp, V.AddExpPlus}, {V.EPS}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"index" + index);
		//		sout(layer,"isAddExpPlus " + s);
		//		if (s == null) {
		//			index--;
		//			return null;
		//		}
		return s;
	}

	public static String isMulExp(List<String> list, int layer) {
		String[][] grammar = {{V.UnaryExp, V.MulExpPlus}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isME " + s);
		return getMul(s);
	}

	public static String isMulExpPlus(List<String> list, int layer) {
		String[][] grammar =
			{{V.MUL, V.UnaryExp, V.MulExpPlus}, {V.DIV, V.UnaryExp, V.MulExpPlus}, {V.MOD, V.UnaryExp, V.MulExpPlus},
				{V.EPS}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isMEP " + s);
		return s;
	}

	public static String isUnaryExp(List<String> list, int layer) {
		String[][] grammar =
			{{V.PrimaryExp}, {V.Ident, V.SBL, V.SBR}, {V.Ident, V.SBL, V.FuncRParams, V.SBR}, {V.UnaryOp, V.UnaryExp}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isUnaryExp " + s);
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

	public static String isFuncRParams(List<String> list, int layer) {
		String[][] grammar = {{V.Exp, V.Exps}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isUnaryExp " + s);
		return s;
	}

	public static String isExps(List<String> list, int layer) {
		String[][] grammar = {{V.SEMICOLON, V.Exp, V.Exps}, {V.SEMICOLON, V.Exp}, {V.EPS}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isUnaryExp " + s);
		return s;
	}

	public static String isPrimaryExp(List<String> list, int layer) {
		String[][] grammar = {{V.SBL, V.Exp, V.SBR}, {V.LVal}, {V.Number}};
		String s = executeG(grammar, list, layer);
		if (s == null) {
			return null;
		}
		if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
			return s.substring(1, s.length() - 1);
		}
		//		sout(layer,"isPrimaryExp " + s);
		return s;
	}

	public static String isUnaryOp(List<String> list, int layer) {
		String[][] grammar = {{V.PLUS}, {V.MINUS}};
		String s = executeG(grammar, list, layer);
		//		sout(layer,"isUnaryOp " + s);
		return s;
	}

	public static String isNumber(List<String> list, int layer) {
		//		String tmp = list.get(index++);
		String tmp = list.get(index);
		//		sout(layer,"isNumber "+tmp);
		try {
			Integer.parseInt(tmp);
		} catch (NumberFormatException e) {
			//			index--;
			return null;
		}
		//		return "i32 " + tmp;
		return tmp;
	}

	public static String isDecConst(List<String> list, int layer) {
		return "";
	}

	public static String isOctConst(List<String> list, int layer) {
		return "";
	}

	public static String isHexConst(List<String> list, int layer) {
		return "";
	}

	public static String isNoneZeroDigit(List<String> list, int layer) {
		return "";
	}

	public static String isDigit(List<String> list, int layer) {
		String s = list.get(index);
		if (s.length() == 1 && s.charAt(0) <= '9' && s.charAt(0) >= '0') {
			return s;
		} else {
			return null;
		}
	}

	public static String isNoneDigit(List<String> list, int layer) {
		sout(layer, "isNoneDigit");
		String s = list.get(index);
		sout(layer, s);
		if (s.length() == 1 && ((s.charAt(0) <= 'z' && s.charAt(0) >= 'a') || (s.charAt(0) <= 'Z' && s.charAt(0) >= 'A')
			|| (s.equals("_")))) {
			return s;
		} else {
			return null;
		}
	}

	public static String isOctDigit(List<String> list, int layer) {
		return "";
	}

	public static String isHexPre(List<String> list, int layer) {
		return "";
	}

	public static String isHexDigit(List<String> list, int layer) {
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

	public static String getPlus(String s, int layer) {
		sout(layer, s);
		return s;
	}

	public static String getPlusBack(String s, int layer) {
		if (s == null) {
			return null;
		}
		sout(layer, "getPlus " + s);
		try {
			if (s.contains("++")) {
				String[] nums = s.split("\\+\\+");
				if (nums.length == 1) {
					return null;
				}
				for (int i = 0; i < nums.length; i++) {
					if (nums[i].contains("--")) {
						nums[i] = getPlus(nums[i], layer + 1);
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
						nums[i] = getPlus(nums[i], layer + 1);
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
			//			sout(layer,e);
			return null;
		}
		return s;
	}

	public static void sout(int layer, String out) {
		for (int i = 0; i < layer; i++) {
			System.out.print("\t");
		}
		System.out.println(out);
	}

	public static boolean isVal(String s) {
		if (Lab1.isKey(s) || !Lab1.isLetter(s.charAt(0))) {
			return false;
		}
		int len = s.length();
		for (int i = 0; i < len; i++) {
			if (!Lab1.isLetter(s.charAt(i)) && !Lab1.isDigit(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		System.out.println(isVal("putint"));
	}
}
