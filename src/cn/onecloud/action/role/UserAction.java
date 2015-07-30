package cn.onecloud.action.role;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.model.cmdb.menu.User;
import cn.onecloud.service.cmdb.role.UserManager;
import cn.onecloud.util.page.cmdb.UserPage;

import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("user")
@Scope("prototype")//必须注解为多态
public class UserAction extends ActionSupport {
//依赖注入
	@Resource(name="userManager")
	private UserManager userManager;
//页面属性
	private User user;
	private UserPage page;
	private int[] roleId;
	private String json;
	private String date;
//访问方法
//c
	public String save_js() {
		if(user.getAccount().isEmpty()) {
			json = "{\"status\":1,\"mess\":\"账号为空！\"}";
		} else if(user.getName().isEmpty()) {
			json = "{\"status\":1,\"mess\":\"名字为空！\"}";
		} else {
			if (user.getPassword().isEmpty()) {
				user.setPassword("123456");
			}
			json = userManager.save(user, roleId);
		}
		return "json";
	}
//r
	public String list() {
		json = userManager.findDRs();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null)
			page = new UserPage();
		json = userManager.findU_inadOnPage(page);
		return "json";
	}
//u
	public String change_js() {
		if(user.getId() == 0) {
			json = "{\"status\":1,\"mess\":\"未选择用户！\"}";
		} else {
			if(user.getPassword().isEmpty())
				user.setPassword("123456");
			json = userManager.change(user, roleId);
		}
		return "json";
	}
	public String changePW_js() {
		if(user.getPassword().isEmpty())
			user.setPassword("123456");
		json = userManager.changePW(user);
		return "json";
	}
	public String changeRole_js() {
		json = userManager.changeRole(json);
		return "json";
	}
//d
	public String remove_js() {
		if(user.getId() == 0) {
			json = "{\"status\":1,\"mess\":\"未选择用户！\"}";
		} else {
			json = userManager.remove(user);
		}
		return "json";
	}
//o
	/*public String makePassword() {
		if(user == null) user = new User(0, "admin", "admin",0, "dpmName");
		this.userManager.makePassword(user.getAccount(), user.getPassword());
		return "jhtml";
	}*/
	
	public String getAllUserName(){
		json = userManager.getAllUserName();
		//System.out.println("json="+json);
		return "json";
	}
	
//set get
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public UserPage getPage() {
		return page;
	}
	public void setPage(UserPage page) {
		this.page = page;
	}
	public int[] getRoleId() {
		return roleId;
	}
	public void setRoleId(int[] roleId) {
		this.roleId = roleId;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
