package cn.onecloud.action.userbehavior;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import cn.onecloud.service.userbehavior.TrafficallManager;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("trafficall")
@Scope("prototype")//必须注解为多态
public class TrafficAllAction extends ActionSupport {
	private String json;
	private TrafficAllPage page;
	
	@Resource(name="trafficallManager")
	private TrafficallManager tam;
//r
	/**
	 * 流量与浏览数排行
	 * @return
	 */
	public String list() {
		page = new TrafficAllPage();
		json = tam.findAll(page);
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new TrafficAllPage();
			//page = new TrafficAllPage("2013-08-11");
		}
		json = tam.findAll(page);
		return "json";
	}
	/**
	 * 各应用流量与浏览数
	 */
	public String search() {
		page = new TrafficAllPage();
		page.setAliases("www.pispower.com");
		json = tam.findApp(page);
		return SUCCESS;
	}
	public String search_js() {
		if(page == null) {
			page = new TrafficAllPage();
			page.setAliases("www.pispower.com");
		}
		json = tam.findApp(page);
		return "json";
	}
	/**
	 * 总流量和浏览数
	 */
	public String sum() {
		page = new TrafficAllPage();
		json = tam.findSum(page);
		return SUCCESS;
	}
	public String sum_js() {
		if(page == null) {
			page = new TrafficAllPage();
		}
		json = tam.findSum(page);
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
