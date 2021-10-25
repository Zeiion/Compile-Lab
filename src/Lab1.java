import static java.lang.System.exit;

import java.util.ArrayList;
import java.util.Scanner;

public class Lab1 {

	public static String ERR = "err";
	public static String[] keyWords = {"int", "main"};
	private static char[] ops = {';', '(', ')', '{', '}', '+', '-', '*', '/', '%'};

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

	public static boolean isBlank(char c) {
		if (Character.isWhitespace(c)) {
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

	public static boolean isOp(char c) {
		for (char ch : ops) {
			if (c == ch) {
				return true;
			}
		}
		return false;
	}

	public static boolean isHexLetter(char c) {
		return isLetter(c) && ((c <= 'f' && c >= 'a') || (c <= 'F' && c >= 'A'));
	}

	public static boolean isNumber(String s) {
		char[] ss = s.toCharArray();
		for (char c : ss) {
			if (!isDigit((c))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return -1 出错
	 * @return 1 继续寻找注释
	 */
	public static int analyze(String line, ArrayList<String> out) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
			if (isBlank(line.charAt(i))) {
				continue;
			} else if (isDigit(line.charAt(i))) {
				String tmpDigit = "";
				char start = line.charAt(i);
				if (start == '0') {
					// 十六进制
					if (i + 1 < len && (line.charAt(i + 1) == 'x' || line.charAt(i + 1) == 'X')) {
						tmpDigit += "0x";
						i += 2;
						if (i >= len || (!isDigit(line.charAt(i)) && !isHexLetter(line.charAt(i)))) {
							System.exit(-1);
						}
						while (isDigit(line.charAt(i)) || isHexLetter(line.charAt(i))) {
							tmpDigit += line.charAt(i);
							if (i + 1 < len) {
								i++;
							} else {
								// number done
								out.add(Integer.valueOf(tmpDigit.substring(2), 16).toString());
								return 0;
							}
						}
						out.add(Integer.valueOf(tmpDigit.substring(2), 16).toString());
					}
					// 八进制
					else if (i + 1 < len && isDigit(line.charAt(i + 1))) {
						tmpDigit += "0";
						i++;
						if (i >= len) {
							continue;
						}
						while (isDigit(line.charAt(i))) {
							tmpDigit += line.charAt(i);
							if (i + 1 < len) {
								i++;
							} else {
								// number done
								out.add(Integer.valueOf(tmpDigit, 8).toString());
								return 0;
							}
						}
						//
						out.add(Integer.valueOf(tmpDigit, 8).toString());
					} else {
						System.exit(-1);
					}
				} else {
					while (isDigit(line.charAt(i))) {
						tmpDigit += line.charAt(i);
						if (i + 1 < len) {
							i++;
						} else {
							// number done
							out.add(tmpDigit);
							return 0;
						}
					}
					// number done
					out.add(tmpDigit);
				}
				i--;
				continue;
			} else if (isLetter(line.charAt(i))) {
				StringBuilder tmpLetter = new StringBuilder();
				while (isLetter(line.charAt(i))) {
					tmpLetter.append(line.charAt(i));
					if (i + 1 < len) {
						i++;
					} else {
						// letter done
						out.add(tmpLetter.toString());
						return 0;
					}
				}
				i--;
				// letter done
				out.add(tmpLetter.toString());
				continue;
			} else if (isOp(line.charAt(i))) {
				if (i + 1 < len && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
					return 0;
				} else if (i + 1 < len && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
					if (!line.contains("*/")) {
						return 1;
					} else {
						i = line.indexOf("*/") + 1;
						continue;
					}
				} else {
					// op done
					out.add(Character.toString(line.charAt(i)));
				}
			} else {
				System.exit(-1);
			}
		}
		return 0;
	}

	public static String getString(String s) {
		switch (s) {
			case "int":
				return "define dso_local i32 ";
			case "main":
				return "@main";
			case "(":
				return "(";
			case ")":
				return ")";
			case "{":
				return "{\n\r";
			case "}":
				return "}";
			case "return":
				return "\tret ";
			case ";":
				return "";
		}
		return "-1";
	}

	public static String getIntMain(ArrayList<String> input) {
		String output = "";
		String[] testList = {"int", "main", "(", ")", "{", "任意", "}"};
		for (int i = 0; i < testList.length; i++) {
			if (testList[i].equals(input.get(i))) {
				output += getString(testList[i]);
				continue;
			} else {
				if (testList[i].equals("任意")) {
					String res = getRet(new ArrayList<>(input.subList(i, input.size() - 1)));
					if (res.equals(ERR)) {
						return ERR;
					} else {
						output += res;
						output += "}\n\r";
						break;
					}
				} else {
					return ERR;
				}
			}
		}
		return output;
	}

	public static String getRet(ArrayList<String> input) {
		String output = "";
		String[] testList = {"return", "任意", ";"};
		for (int i = 0; i < testList.length; i++) {
			if (testList[i].equals(input.get(i))) {
				output += getString(testList[i]);
				continue;
			} else {
				if (testList[i].equals("任意")) {
					if (isNumber(input.get(i))) {
						output += "i32 " + input.get(i) + "\n\r";
					} else {
						return ERR;
					}
				}
			}
		}
		return output;
	}
}
