package cn.onecloud.action.role;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.menu.Menu;
import cn.onecloud.service.cmdb.role.MenuManager;

@SuppressWarnings("serial")
@Component("menu")
@Scope("prototype")//必须注解为多态
public class MenuAction extends CommonAction {
//依赖注入	
	@Resource(name="menuManager")
	private MenuManager menuManager;
//页面属性
	private String[] menus;
	private Menu menu;
//访问方法
//c
	public String add_js() {
		int length = menu.getName().length();
		if(length>0 && length<20) {
			menuManager.save(menu);
			json = "{\"status\":0,\"id\":\"" + menu.getId() + "\"}";
		} else {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		return JSON;
	}
//r
	public String list() {
		json = menuManager.findAsOnsort();
		return SUCCESS;
	}
	public String role() {
		json = menuManager.findRoAus();
		return "success";
	}
//u
	public String changeMenu_js() {
		if(menus.length > 0) {
			menuManager.changeMenus(menus);
		}
		json = "{\"status\":0, \"mess\":\"更新成功！\"}";
		return JSON;
	}
	public String change_js() {
		if(menu.getName() == null)
			menu.setName("");
		int length = menu.getName().length();
		if(length < 20) {
			if(menu.getUrl() == null)
				menu.setUrl("");
			length = menu.getUrl().length();
			if(length < 50) {
				menuManager.changeA_nu(menu);
				json = "{\"status\":0}";
			} else {
				json = "{\"status\":1,\"mess\":\"菜单url长度超出50个！\"}";
			}
		} else {
			json = "{\"status\":1,\"mess\":\"菜单名字长度超出20个！\"}";
		}
		return JSON;
	}
	public String changeRole_js() {
		json = menuManager.changeRole(json);
		return "json";
	}
//d
	public String remove_js() {
		json = menuManager.remove(menu);
		return JSON;
	}
//set get
	public String[] getMenus() {
		return menus;
	}
	public void setMenus(String[] menus) {
		this.menus = menus;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
}
