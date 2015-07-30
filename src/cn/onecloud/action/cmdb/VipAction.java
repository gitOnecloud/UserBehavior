package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.Vip;
import cn.onecloud.service.cmdb.VipManager;
import cn.onecloud.util.page.cmdb.VipPage;

@SuppressWarnings("serial")
@Component("vip")
@Scope("prototype")//必须注解为多态
public class VipAction extends CommonAction {
	
	@Resource(name="vipManager")
	private VipManager vm;
	private Vip vip;
	private VipPage page;
//c
	public String add_js() {
		json = vm.save(vip);
		return JSON;
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new VipPage();
		}
		json = vm.findByPage(page);
		return JSON;
	}
//u
	public String change_js() {
		json = vm.change(vip);
		return JSON;
	}
//d
	public String remove_js() {
		json = vm.remove(json);
		return JSON;
	}
//set get 
	public VipPage getPage() {
		return page;
	}
	public void setPage(VipPage page) {
		this.page = page;
	}
	public Vip getVip() {
		return vip;
	}
	public void setVip(Vip vip) {
		this.vip = vip;
	}
}
