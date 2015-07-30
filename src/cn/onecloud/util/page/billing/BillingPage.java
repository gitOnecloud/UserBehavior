package cn.onecloud.util.page.billing;

import cn.onecloud.util.page.userbehavior.Page;

public class BillingPage extends Page {
	private String oaid;
	private String appname;
	public String getOaid() {
		return oaid;
	}
	public void setOaid(String oaid) {
		this.oaid = oaid;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
}
