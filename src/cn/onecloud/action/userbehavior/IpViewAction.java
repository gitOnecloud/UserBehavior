package cn.onecloud.action.userbehavior;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.service.userbehavior.IpViewManager;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("ipview")
@Scope("prototype")//必须注解为多态
public class IpViewAction extends ActionSupport{
	private String json;
	private TrafficAllPage page;
	
	@Resource(name="ipViewManager")
	private IpViewManager im;
//r
	public String list() {
		page = new TrafficAllPage();
		json = im.findAll(page);
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new TrafficAllPage();
		}
		json = im.findAll(page);
		return "json";
	}
//setget
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public TrafficAllPage getPage() {
		return page;
	}
	public void setPage(TrafficAllPage page) {
		this.page = page;
	}
}
