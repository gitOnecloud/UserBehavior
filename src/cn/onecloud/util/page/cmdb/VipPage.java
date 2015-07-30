package cn.onecloud.util.page.cmdb;

public class VipPage extends Page{
	
	private String igdefine;
	private String party;
	private String vip;
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
	public String getIgdefine() {
		return igdefine;
	}
	public void setIgdefine(String igdefine) {
		this.igdefine = igdefine;
	}
	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
	public String getVip() {
		return vip;
	}
	public void setVip(String vip) {
		this.vip = vip;
	}

}
