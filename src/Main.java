import static java.lang.System.exit;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		ArrayList<String> out = new ArrayList<>();
		boolean find = false;
		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (find) {
				if (line.contains("*/")) {
					line = line.substring(line.indexOf("*/") + 2);
					find = false;
				} else {
					continue;
				}
			}
			int flag = Lab1.analyze(line, out);
			if (flag == -1) {
				exit(-1);
			} else if (flag == 1) {
				find = true;
			}
		}
		if (find) {
			//没匹配上注释
			exit(-1);
		}

//		System.out.println("----");
//		for (String s : out) {
//			System.out.println(s);
//		}
//		System.out.println("----");

		//		String res1 = Lab1.getIntMain(out);
		//		if(res1.equals("err")){
		//			exit(-1);
		//		}else{
		//			System.out.println(res1);
		//			exit(0);
		//		}
		System.out.println(Recur.isCompUnit(out));
		exit(0);
	}
}
