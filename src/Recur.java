import java.util.List;

public class Recur {
	private static int index = 0;

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
			}
		} catch (Exception e) {
			System.exit(-1);
		}
		System.exit(-1);
		return "";
	}

	public static String isVT(List<String> list, String string) {
		if (!list.get(index).equals(string)) {
			System.exit(-1);
			return "";
		} else {
			index++;
			switch (string) {
				case V.INT:
					return "define dso_local i32 ";
				case V.MAIN:
					return "@main";
				case V.RETURN:
					return "\tret ";
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
				default:
					index--;
					System.exit(-1);
			}
		}
		return "";
	}

	public static String catString(String[] grammar, List<String> list) {
		String ret = "";
		for (int i = 0; i < grammar.length; i++) {
			try {
				ret += executeV(grammar[i], list, list.size());
			} catch (Exception e) {
				System.exit(-1);
			}
		}
		return ret;
	}

	public static String isCompUnit(List<String> list) {
		String[] grammar = {V.FuncDef};
		String s = catString(grammar, list);
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
		String[] grammar = {V.LBL, V.Stmt, V.LBR};
		return catString(grammar, list);
	}

	public static String isStmt(List<String> list) {
		String[] grammar = {V.RETURN, V.Number, V.SEMICOLON};
		return catString(grammar, list);
	}

	public static String isNumber(List<String> list) {
		String tmp = list.get(index++);
		return "i32 " + tmp;
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
}
