package cn.onecloud.action.cmdb.server;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.server.Cabinet;
import cn.onecloud.service.cmdb.server.CabinetManager;
import cn.onecloud.service.cmdb.server.RoomManager;
import cn.onecloud.service.cmdb.server.StyleManager;
import cn.onecloud.util.page.cmdb.CabinetPage;

@SuppressWarnings("serial")
@Component("cabinet")
@Scope("prototype")//必须注解为多态
public class CabinetAction extends CommonAction {

	@Resource(name="cabinetManager")
	private CabinetManager cm;
	@Resource(name="styleManager")
	private StyleManager sm;
	@Resource(name="roomManager")
	private RoomManager rm;
	private Cabinet cabinet;
	private CabinetPage page;
//c
	public String add_js() {
		json = cm.save(cabinet);
		return JSON;
	}
//r
	public String list() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("styles", sm.findAll());
		jsonObj.put("rooms", rm.findAll());
		json = jsonObj.toString();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new CabinetPage();
		}
		json = cm.findByPage(page).toString();
		return JSON;
	}
	public String detail_js() {
		json = cm.findById(json);
		return JSON;
	}
//u
	public String change_js() {
		json = cm.change(cabinet);
		return JSON;
	}
	public String changeSort_js() {
		json = cm.changeSort(json);
		return JSON;
	}
	public String changeRoom_js() {
		json = cm.changeRoom(json);
		return JSON;
	}
//d
	public String remove_js() {
		json = cm.remove(json);
		return JSON;
	}
//set get
	public Cabinet getCabinet() {
		return cabinet;
	}
	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}
	public CabinetPage getPage() {
		return page;
	}
	public void setPage(CabinetPage page) {
		this.page = page;
	}
}
