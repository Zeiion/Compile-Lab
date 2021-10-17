import static java.lang.System.exit;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		ArrayList<String> out = new ArrayList<>();
		while (input.hasNextLine()) {
			String line = input.nextLine();
//			if(line.isBlank()) break;
			if (Lab1.analyze(line,out) == -1) {
				exit(-1);
			}
		}
//		for(String o : out){
//			System.out.println(o);
//		}
		String res1 = Lab1.getIntMain(out);
		if(res1.equals("ERR")){
			exit(-1);
		}else{
			System.out.println(res1);
			exit(0);
		}
	}
}
