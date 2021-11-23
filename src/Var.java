public class Var {

	// 变量名
	public String name;
	// 地址索引
	public int index;
	// 是否是const
	public boolean isConst = false;
	// 是否是global
	public boolean isGlobal = false;
	// 数据值
	public int value = 0;
	// 数据类型
	public String type = "i32";

	public Var(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public Var(String name, int index, boolean isConst) {
		this.name = name;
		this.index = index;
		this.isConst = isConst;
	}

	public Var(boolean isGlobal, int value, String name, int index, boolean isConst) {
		this.isGlobal = isGlobal;
		this.value = value;
		this.name = name;
		this.index = index;
		this.isConst = isConst;
	}
}
