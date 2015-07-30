package cn.onecloud.util.page.cmdb;

import java.io.Serializable;

public class IngredientPage extends Page implements Serializable {

	private static final long serialVersionUID = -7448310034605135356L;
	
	private String party_id;
	private String idf_id;
	private String ip;
	private String isActive;
	private String host;
	
	private String party;
	private String idf;
	private String isActiceName;
	private String hostname;
	
	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(10);
		super.countPage(countNums);
	}
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getParty_id() {
		return party_id;
	}
	public void setParty_id(String party_id) {
		this.party_id = party_id;
	}
	public String getIdf_id() {
		return idf_id;
	}
	public void setIdf_id(String idf_id) {
		this.idf_id = idf_id;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
	public String getIdf() {
		return idf;
	}
	public void setIdf(String idf) {
		this.idf = idf;
	}
	public String getIsActiceName() {
		return isActiceName;
	}
	public void setIsActiceName(String isActiceName) {
		this.isActiceName = isActiceName;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
}
