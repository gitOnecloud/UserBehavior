package cn.onecloud.util.page.userbehavior;

public class TrafficAllPage extends Page {
	
	private String aliases;//一级域名
	private String domain;//二级域名
	private String oaid;
	
	public TrafficAllPage() {}
	public TrafficAllPage(String aliases) {
		this.aliases = aliases;
	}
//set get
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getOaid() {
		return oaid;
	}
	public void setOaid(String oaid) {
		this.oaid = oaid;
	}

}
