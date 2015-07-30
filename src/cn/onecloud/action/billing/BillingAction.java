package cn.onecloud.action.billing;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.service.billing.BillingManager;
import cn.onecloud.util.page.billing.BillingPage;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("billing")
@Scope("prototype")//必须注解为多态
public class BillingAction extends ActionSupport {
	private String json;
	private BillingPage page;
	
	@Resource(name="billingManager")
	private BillingManager bm;
//r
	/**
	 * 流量与浏览数排行
	 * @return
	 */
	public String list() {
		page = new BillingPage();
		json = bm.findAll(page);
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new BillingPage();
			//page = new TrafficAllPage("2013-08-11");
		}
		json = bm.findAll(page);
		return "json";
	}
//setget
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public BillingPage getPage() {
		return page;
	}
	public void setPage(BillingPage page) {
		this.page = page;
	}
	
	
}
