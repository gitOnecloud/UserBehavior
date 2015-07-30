package cn.onecloud.util.page.cmdb;

import java.io.Serializable;

public class ArenaPage extends Page implements Serializable {

	private static final long serialVersionUID = -5262318322858263773L;
	
	private String name;
	private String domain;
	private String type;
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
