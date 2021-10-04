public class Lex {
	private static String[] keyWords = {"if", "else", "while", "break", "continue", "return"};
	private static char[] ops = {'=', ';', '(', ')', '{', '}', '+', '*', '/', '<', '>'};

	public static boolean isDigit(char c) {
		if (c >= '0' && c <= '9') {
			return true;
		}
		return false;
	}

	public static boolean isLetter(char c) {
		if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_') {
			return true;
		}
		return false;
	}

	public static boolean isKey(String s) {
		for (String k : keyWords) {
			if (k.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBlank(char c) {
		if (Character.isWhitespace(c)) {
			return true;
		}
		return false;
	}

	public static boolean isOp(char c) {
		for (char ch : ops) {
			if (c == ch) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return -1 直接退出
	 */
	public static int analyze(String line) {
		int len = line.length();
		String tmp = "";
		for (int i = 0; i < len; i++) {
			if (isBlank(line.charAt(i))) {
				continue;
			} else if (isDigit(line.charAt(i))) {
				String tmpDigit = "";
				while (isDigit(line.charAt(i))) {
					tmpDigit += line.charAt(i);
					if (i + 1 < len) {
						i++;
					} else {
						System.out.println("Number(" + tmpDigit + ")");
						return 0;
					}
				}
				i--;
				System.out.println("Number(" + tmpDigit + ")");
				continue;
			} else if (isLetter(line.charAt(i))) {
				String tmpLetter = "";
				while (isLetter(line.charAt(i))) {
					tmpLetter += line.charAt(i);
					if (i + 1 < len) {
						i++;
					} else {
						if (isKey(tmpLetter)) {
							switch (tmpLetter) {
								case "if":
									System.out.println("If");
									break;
								case "else":
									System.out.println("Else");
									break;
								case "while":
									System.out.println("While");
									break;
								case "break":
									System.out.println("Break");
									break;
								case "continue":
									System.out.println("Continue");
									break;
								case "return":
									System.out.println("Return");
									break;
							}
						} else {
							System.out.println("Ident(" + tmpLetter + ")");
						}
						return 0;
					}
				}
				i--;
				if (isKey(tmpLetter)) {
					switch (tmpLetter) {
						case "if":
							System.out.println("If");
							break;
						case "else":
							System.out.println("Else");
							break;
						case "while":
							System.out.println("While");
							break;
						case "break":
							System.out.println("Break");
							break;
						case "continue":
							System.out.println("Continue");
							break;
						case "return":
							System.out.println("Return");
							break;
					}
				} else {
					System.out.println("Ident(" + tmpLetter + ")");
				}
				continue;
			} else if (isOp(line.charAt(i))) {
				switch (line.charAt(i)) {
					case '=':
						if (i + 1 < len && line.charAt(i + 1) == '=') {
							System.out.println("Eq");
							i++;
							continue;
						} else {
							System.out.println("Assign");
							continue;
						}
					case ';':
						System.out.println("Semicolon");
						continue;
					case '(':
						System.out.println("LPar");
						continue;
					case ')':
						System.out.println("RPar");
						continue;
					case '{':
						System.out.println("LBrace");
						continue;
					case '}':
						System.out.println("RBrace");
						continue;
					case '+':
						System.out.println("Plus");
						continue;
					case '*':
						System.out.println("Mult");
						continue;
					case '/':
						System.out.println("Div");
						continue;
					case '<':
						System.out.println("Lt");
						continue;
					case '>':
						System.out.println("Gt");
						continue;
				}
			} else {
				System.out.println("Err");
				return -1;
			}
		}
		return 0;
	}
}
