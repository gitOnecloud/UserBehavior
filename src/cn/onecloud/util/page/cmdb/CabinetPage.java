package cn.onecloud.util.page.cmdb;

import java.io.Serializable;

public class CabinetPage extends Page implements Serializable {

	private static final long serialVersionUID = 5496576975957595603L;
	
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
