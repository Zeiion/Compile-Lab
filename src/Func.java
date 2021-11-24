import java.util.ArrayList;

public class Func {

	// 变量名
	public String name;
	// type void or int
	public String type = "int";
	// params
	public ArrayList<Var> params = new ArrayList<>();

	public Func() {
	}

	public Func(String name, String type, ArrayList<Var> params) {
		this.name = name;
		this.type = type;
		this.params = params;
	}
}
