import java.util.ArrayList;

public class Var {

	// 变量名
	public String name;
	// 地址索引
	public int index;
	public int fakeIndex;
	// 是否是const
	public boolean isConst = false;
	// 是否是global
	public boolean isGlobal = false;
	// 数据值
	public int value = 0;
	// 数据类型
	public String type = "i32";

	// 是否是array
	public boolean isArray = false;
	public int dimension = 1;
	// 数组中所有的值
	public ArrayList<Integer> arrValues;
	// 维度列表
	public ArrayList<Integer> dimensionList;

	public Var() {
	}

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

	public void setArray(int dimension, ArrayList<Integer> arrValues, String arrType, ArrayList<Integer> dArr) {
		isArray = true;
		this.dimension = dimension;
		this.arrValues = arrValues;
		this.type = arrType;
		dimensionList = dArr;
	}

	public Var copyVar(Var v) {
		Var vv = new Var();
		vv.name = v.name;
		vv.index = v.index;
		vv.isConst = v.isConst;
		vv.isGlobal = v.isGlobal;
		vv.value = v.value;
		vv.type = v.type;
		vv.isArray = v.isArray;
		vv.dimension = v.dimension;
		vv.arrValues = v.arrValues;
		vv.dimensionList = v.dimensionList;
		return vv;
	}
}
