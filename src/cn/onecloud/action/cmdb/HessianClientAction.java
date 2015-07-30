package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.HessianClient;
import cn.onecloud.service.cmdb.HessianClientManager;
import cn.onecloud.util.page.cmdb.HessianClientPage;

@SuppressWarnings("serial")
@Component("hessianClient")
@Scope("prototype")//必须注解为多态
public class HessianClientAction extends CommonAction {
	@Resource(name="hessianClientManager")
	private HessianClientManager hcm;
	private HessianClientPage page;
	private HessianClient hessianClient;
//c
	public String add_js() {
		json = hcm.save(hessianClient);
		return JSON;
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new HessianClientPage();
		}
		json = hcm.findByPage(page).toString();
		return JSON;
	}
//u
	public String change_js() {
		json = hcm.change(hessianClient);
		return JSON;
	}
	/**
	 * 更新客户端的RSA密钥
	 */
	public String changeRSA_js() {
		json = hcm.changeRSA(json);
		return JSON;
	}
//d
	public String remove_js() {
		json = hcm.remove(json);
		return JSON;
	}
//set get
	public HessianClientPage getPage() {
		return page;
	}
	public void setPage(HessianClientPage page) {
		this.page = page;
	}
	public HessianClient getHessianClient() {
		return hessianClient;
	}
	public void setHessianClient(HessianClient hessianClient) {
		this.hessianClient = hessianClient;
	}
}
