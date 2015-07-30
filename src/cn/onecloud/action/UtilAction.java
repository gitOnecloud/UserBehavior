package cn.onecloud.action;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.service.cmdb.role.SecuManager;
import cn.onecloud.util.LocalShell;

@SuppressWarnings("serial")
@Component("util")
@Scope("prototype")//必须注解为多态
public class UtilAction extends CommonAction {
//依赖注入
	@Resource(name="secuManager")
	private SecuManager secuManager;
	/*@Resource(name="initData")
	private InitDataManager idata;*/
//页面属性
//访问方法
	public String index() {
		json = SecuManager.findMenu();
		return "index";
	}
	//登录用户
	public String lguser() {
		json = secuManager.findlgUser();
		return SUCCESS;
	}
	public String exec() {
		String info = LocalShell.exec("ipconfig");
		//SshShell.exec("192.168.75.128", "clouder", "engine", 22,
			//	"sh /home/clouder/liubf/script/nagios.sh all")
		JSONObject jo = JSONObject.fromObject(info);
		//json = jo.getString("harddisk");
		json = jo.toString();
		return JSON;
	}
	//刷新内存
	public String rfall_rf() {
		this.secuManager.refreshAll_RA_();
		this.secuManager.refreshMenu();
		//this.idata.reload();
		json = "{\"status\":0}";
		return "json";
	}
	//权限角色
	public String refresh_rf() {
		this.secuManager.refreshAll_RA_();
		json = "{\"status\":0}";
		return "json";
	}
	//权限菜单
	public String rfmenu_rf() {
		this.secuManager.refreshMenu();
		json = "{\"status\":0}";
		return "json";
	}
	//配置文件
	public String rfdata_rf() {
		//this.idata.reload();
		//idata.output();
		json = "{\"status\":0}";
		return "json";
	}
//set get
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManager.currentUser();
	}
}
