package cn.onecloud.action.cmdb.server;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.service.cmdb.server.StyleManager;
import cn.onecloud.util.page.cmdb.StylePage;

@SuppressWarnings("serial")
@Component("style")
@Scope("prototype")//必须注解为多态
public class StyleAction extends CommonAction {
	
	@Resource(name="styleManager")
	private StyleManager sm;
	private Style style;
	private StylePage page;
//c
	public String add_js() {
		json = sm.save(style);
		return JSON;
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new StylePage();
		}
		json = sm.findByPage(page).toString();
		return JSON;
	}
//u
	public String change_js() {
		json = sm.change(style);
		return JSON;
	}
//d
	public String remove_js() {
		json = sm.remove(json);
		return JSON;
	}
//set get
	public Style getStyle() {
		return style;
	}
	public void setStyle(Style style) {
		this.style = style;
	}
	public StylePage getPage() {
		return page;
	}
	public void setPage(StylePage page) {
		this.page = page;
	}
}
