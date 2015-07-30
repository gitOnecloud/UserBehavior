package cn.onecloud.util.page.cmdb;

import java.io.Serializable;

public class StylePage extends Page implements Serializable {

	private static final long serialVersionUID = 6001943247793693852L;
	
	private String name;
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
}
