package cn.onecloud.action.userbehavior;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.service.userbehavior.PageViewManager;
import cn.onecloud.util.page.userbehavior.Page;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("pageView")
@Scope("prototype")//必须注解为多态
public class PageViewAction extends ActionSupport {
	private String json;
	private Page page;
	
	@Resource(name="pageViewManager")
	private PageViewManager pm;
	
//r
	public String list() {
		page = new Page();
		json = pm.findAll(page);
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new Page();
		}
		json = pm.findAll(page);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
}
