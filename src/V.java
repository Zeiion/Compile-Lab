import java.util.Scanner;

public class V {
	//VN
	public static final String CompUnit = "CompUnit";
	public static final String FuncDef = "FuncDef";
	public static final String FuncType = "FuncType";
	public static final String Ident = "Ident";
	public static final String Block = "Block";
	public static final String Stmt = "Stmt";
	public static final String Exp = "Exp";
	public static final String AddExp = "AddExp";
	public static final String AddExpPlus = "AddExpPlus";
	public static final String MulExp = "MulExp";
	public static final String MulExpPlus = "MulExpPlus";
	public static final String UnaryExp = "UnaryExp";
	public static final String PrimaryExp = "PrimaryExp";
	public static final String UnaryOp = "UnaryOp";
	public static final String Number = "Number";
	public static final String DecConst = "DecConst";
	public static final String OctConst = "OctConst";
	public static final String HexConst = "HexConst";
	public static final String NoneZeroDigit = "NoneZeroDigit";
	public static final String Digit = "Digit";
	public static final String OctDigit = "OctDigit";
	public static final String HexDigit = "HexDigit";
	public static final String HexPre = "HexPre";
	//	VT
	public static final String EPS = "Eps";
	public static final String INT = "int";
	public static final String MAIN = "main";
	public static final String RETURN = "return";
	public static final String SBL = "(";
	public static final String SBR = ")";
	public static final String LBL = "{";
	public static final String LBR = "}";
	public static final String SEMICOLON = ";";
	public static final String PLUS = "+";
	public static final String PLUSPLUS = "++";
	public static final String MINUS = "-";
	public static final String MINUSMINUS = "--";

	public static final String MUL = "*";
	public static final String DIV = "/";
	public static final String MOD = "%";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String s = scanner.nextLine();
		String[] ss = s.split(", ");
		for (String sss : ss) {
			System.out.println("public static final String " + sss + " = \"" + sss + "\";");
		}
	}
}
