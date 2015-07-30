package cn.onecloud.util.page.cmdb;

import java.io.Serializable;

public class IngredientDefinePage extends Page implements Serializable {

	private static final long serialVersionUID = 1725183513073405501L;
	
	private String name;
	private String port;
	private String type;
	private int register = -1;
	
	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(10);
		super.countPage(countNums);
	}
//set get
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
