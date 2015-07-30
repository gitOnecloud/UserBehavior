package cn.onecloud.util.page.cmdb;

import java.util.Date;

public class LogPage extends Page{
	
	private Date sdate;		//起始时间
	private Date edate;		//结束时间
	private String Ip;
	private String type;	//日志类型
	private String keyword;	//关键字查询
	private String sort;	//排序字段
	private String order;	//顺序：desc、asc
	
	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(20);
		super.countPage(countNums);
	}

	public Date getSdate() {
		return sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public Date getEdate() {
		return edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public String getIp() {
		return Ip;
	}

	public void setIp(String ip) {
		Ip = ip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
