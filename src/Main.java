import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (Lex.analyze(line) == -1) {
				return;
			}
		}
	}
}
