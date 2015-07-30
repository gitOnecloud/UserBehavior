package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.arena.Satellite;
import cn.onecloud.service.cmdb.ArenaManager;
import cn.onecloud.util.page.cmdb.ArenaPage;

@SuppressWarnings("serial")
@Component("arena")
@Scope("prototype")//必须注解为多态
public class ArenaAction extends CommonAction {
	
	@Resource(name="arenaManager")
	private ArenaManager pm;
	private Arena arena;
	private Satellite satellite;
	private ArenaPage page;
//c
	public String add_js() {
		json = pm.save(arena, satellite, json);
		return JSON;
	}
//r
	public String list() {
		return SUCCESS;
	}
	/*public String list_js() {
		if(page == null) {
			page = new ArenaPage();
		}
		json = pm.findByPage(page);
		return JSON;
	}*/
	public String list_js() {
		json = pm.findArena();
		return JSON;
	}
	public String all_js() {
		json = pm.findAll();
		return JSON;
	}
	public String satellite_js() {
		json = pm.findSatellite();
		return JSON;
	}
//u
	public String change_js() {
		json = pm.change(arena, satellite, json);
		return JSON;
	}
//d
	public String remove_js() {
		json = pm.remove(arena, json);
		return JSON;
	}
//set get 
	public ArenaPage getPage() {
		return page;
	}
	public void setPage(ArenaPage page) {
		this.page = page;
	}
	public Arena getArena() {
		return arena;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
	}
	public Satellite getSatellite() {
		return satellite;
	}
	public void setSatellite(Satellite satellite) {
		this.satellite = satellite;
	}
}
