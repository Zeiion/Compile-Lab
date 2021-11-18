public class Var {
	// 地址索引
	public int index;
	// 是否是const
	public boolean isConst = false;
	// 数据类型
	public String type = "i32";

	public Var(int index) {
		this.index = index;
	}

	public Var(int index, boolean isConst) {
		this.index = index;
		this.isConst = isConst;
	}
}
