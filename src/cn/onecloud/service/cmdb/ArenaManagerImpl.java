package cn.onecloud.service.cmdb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.ArenaDao;
import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.arena.Basket;
import cn.onecloud.model.cmdb.arena.Rouyer;
import cn.onecloud.model.cmdb.arena.Satellite;
import cn.onecloud.util.StaticMethod;

@Component("arenaManager")
public class ArenaManagerImpl implements ArenaManager {
	private ArenaDao pdao;
	@Resource(name="arenaDao")
	public void setPdao(ArenaDao pdao) {
		this.pdao = pdao;
	}

//c
	public String save(Arena arena, Satellite satellite, String json) {
		pdao.save(arena);
		if(arena.getType().equals("satellite")) {
			satellite.setSlArena(arena);
			pdao.save(satellite);
		} else if(arena.getType().equals("basket")) {
			Set<Integer> ids = new HashSet<Integer>();
			int id = 0;
			for(String s : json.split("-")) {
				try {
					id = Integer.parseInt(s);
					if(ids.add(id)) {
						pdao.save(new Basket(arena, id));
					}
				} catch(NumberFormatException e) {}
			}
		} else {
			pdao.save(new Rouyer(arena));
		}
		return StaticMethod.addSucc;
	}
//r
	/*@SuppressWarnings("unchecked")
	public String findByPage(ArenaPage page) {
		List<Arena> arenas = pdao.getAllByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Arena arena : arenas) {
			JSONObject arenaJson = new JSONObject();
			arenaJson.put("id", arena.getId());
			arenaJson.put("name", arena.getName());
			//arenaJson.put("domain", arena.getDomain());
			arenaJson.put("type", arena.getType());
			array.add(arenaJson);
		}
		json.put("rows", array);
		return json.toString();
	}*/
	/**
	 * 按树形获取环境
	 */
	@SuppressWarnings("unchecked")
	public String findArena() {
		pdao.getList();//读取出来供Rouyer使用，hibernate会自动调用
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		List<Rouyer> rouyers = pdao.getRouyer();
		for(Rouyer r : rouyers) {
			JSONObject arenaJson = new JSONObject();
			arenaJson.put("id", Integer.toString(r.getId()));
			arenaJson.put("rId", r.getId());
			arenaJson.put("aId", r.getRyArena().getId());
			arenaJson.put("name", r.getRyArena().getName());
			arenaJson.put("type", "rouyer");
			array.add(arenaJson);
		}
		List<Satellite> satellites = pdao.getSatellite();
		for(Satellite s : satellites) {
			JSONObject arenaJson = new JSONObject();
			arenaJson.put("id", s.getSlRouyer().getId() +
					"-" + s.getId());
			arenaJson.put("sId", s.getId());
			arenaJson.put("aId", s.getSlArena().getId());
			arenaJson.put("name", s.getSlArena().getName());
			arenaJson.put("rId", s.getSlRouyer().getId());
			arenaJson.put("rName", s.getSlRouyer().getRyArena().getName());
			arenaJson.put("_parentId", Integer.toString(s.getSlRouyer().getId()));
			arenaJson.put("domain", s.getDomain());
			arenaJson.put("primary", s.getIsPrimary());
			arenaJson.put("type", "satellite");
			array.add(arenaJson);
		}
		List<Basket> baskets = pdao.getBasket();
		for(Basket b : baskets) {
			JSONObject arenaJson = new JSONObject();
			String pId = b.getBkSatellite().getSlRouyer().getId() + "-" +
					b.getBkSatellite().getId();
			arenaJson.put("id", pId + "-" + b.getId());
			arenaJson.put("bId", b.getId());
			arenaJson.put("aId", b.getBkArena().getId());
			arenaJson.put("name", b.getBkArena().getName());
			arenaJson.put("sId", b.getBkSatellite().getId());
			arenaJson.put("sName", b.getBkSatellite().getSlArena().getName());
			arenaJson.put("_parentId", pId);
			arenaJson.put("type", "basket");
			array.add(arenaJson);
		}
		json.put("total", array.size());
		json.put("rows", array);
		return json.toString();
	}
	/**
	 * 读取环境信息
	 */
	@SuppressWarnings("unchecked")
	public String findAll() {
		List<Arena> partys = pdao.getList();
		JSONArray array = new JSONArray();
		for(Arena party : partys) {
			JSONObject vipJson = new JSONObject();
			vipJson.put("value", party.getId());
			vipJson.put("text", party.getName());
			array.add(vipJson);
		}
		return array.toString();
	}
	/**
	 * 读取Satellite
	 */
	@SuppressWarnings("unchecked")
	public String findSatellite() {
		List<Arena> partys = pdao.getSatelliteName();
		JSONArray array = new JSONArray();
		for(Arena party : partys) {
			JSONObject vipJson = new JSONObject();
			vipJson.put("satelliteId", party.getId());
			vipJson.put("satelliteName", party.getName());
			array.add(vipJson);
		}
		return array.toString();
	}
//u
	public String change(Arena arena, Satellite satellite, String json) {
		pdao.update(arena);
		if(arena.getType().equals("satellite")) {
			pdao.update(satellite);
		} else if(arena.getType().equals("basket")) {
			pdao.delete(arena.getId());
			Set<Integer> ids = new HashSet<Integer>();
			int id = 0;
			for(String s : json.split("-")) {
				try {
					id = Integer.parseInt(s);
					if(ids.add(id)) {
						pdao.save(new Basket(arena, id));
					}
				} catch(NumberFormatException e) {}
			}
		}
		return StaticMethod.changeSucc;
	}
//d
	/**
	 * @param json 保存rouyer等id
	 */
	public String remove(Arena arena, String json) {
		if(arena==null || arena.getType()==null || json==null || json.isEmpty()) {
			return StaticMethod.inputError;
		}
		int id = 0;
		try {
			id = Integer.parseInt(json);
		} catch(NumberFormatException e) {
			return StaticMethod.inputError;
		}
		long count = pdao.countServer(arena.getId());
		if(count != 0) {
			return StaticMethod.FailMess("此环境存在" + count + "台服务器，不能删除。");
		}
		count = pdao.countVip(arena.getId());
		if(count != 0) {
			return StaticMethod.FailMess("此环境存在" + count + "个VIP，不能删除。");
		}
		if(arena.getType().equals("satellite")) {
			count = pdao.countBasket(id);
			if(count != 0) {
				return StaticMethod.FailMess("此Satellite存在" + count + "个Basket，不能删除。");
			}
			pdao.delete(json, "Satellite");
		} else if(arena.getType().equals("basket")) {
			pdao.delete(arena.getId());
		} else {//rouyer
			count = pdao.countSatellite(id);
			if(count != 0) {
				return StaticMethod.FailMess("此Rouyer存在" + count + "个Satellite，不能删除。");
			}
			pdao.delete(json, "Rouyer");
		}
		pdao.delete(Integer.toString(arena.getId()), "Arena");
		return StaticMethod.removeSucc;
	}
}
