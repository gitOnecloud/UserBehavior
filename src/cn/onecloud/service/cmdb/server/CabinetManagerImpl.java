package cn.onecloud.service.cmdb.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.server.CabinetDao;
import cn.onecloud.dao.cmdb.server.ServerDao;
import cn.onecloud.model.cmdb.server.Cabinet;
import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.CabinetPage;

@Component("cabinetManager")
public class CabinetManagerImpl implements CabinetManager {
	@Resource(name="cabinetDao")
	private CabinetDao cdao;
	@Resource(name="serverDao")
	private ServerDao sdao;
//c
	public String save(Cabinet cabinet) {
		if(cabinet==null || StaticMethod.StrSize(cabinet.getName())<1 ||
				cabinet.getCabStyle()==null || cabinet.getCabRoom()==null) {
			return StaticMethod.inputError;
		}
		cdao.save(cabinet);
		return StaticMethod.addSucc;
	}
//r
	/**
	 * 分页查找
	 */
	public JSONObject findByPage(CabinetPage page) {
		List<Cabinet> cabinets = cdao.getAllByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Cabinet cabinet : cabinets) {
			JSONObject roomJson = new JSONObject();
			roomJson.put("id", cabinet.getId());
			roomJson.put("name", cabinet.getName());
			roomJson.put("remark", cabinet.getRemark());
			roomJson.put("sId", cabinet.getCabStyle().getId());
			roomJson.put("sName", cabinet.getCabStyle().getName());
			roomJson.put("rId", cabinet.getCabRoom().getId());
			roomJson.put("rName", cabinet.getCabRoom().getName());
			array.add(roomJson);
		}
		json.put("rows", array);
		return json;
	}
	/**
	 * 根据ID查找机房信息以及机柜信息
	 */
	public String findById(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		try {
			Integer.parseInt(json);
		} catch(NumberFormatException e) {
			return StaticMethod.inputError;
		}
		Style style = cdao.getCabStyle(json);
		if(style == null) {
			return StaticMethod.inputError;
		}
		JSONObject room = new JSONObject();
		JSONObject styleJson = new JSONObject();
		styleJson.put("horInterval", style.getHorInterval());
		styleJson.put("horNum", style.getHorNum());
		styleJson.put("verInterval", style.getVerInterval());
		styleJson.put("verNum", style.getVerNum());
		styleJson.put("insideHeight", style.getInsideHeight());
		styleJson.put("insideWidth", style.getInsideWidth());
		room.put("style", styleJson);
		List<Server> servers = sdao.getSvsByCId(json);
		JSONArray array = new JSONArray();
		for(Server server : servers) {
			JSONObject roomJson = new JSONObject();
			roomJson.put("id", server.getId());
			roomJson.put("ip", server.getIp());
			roomJson.put("sort", server.getSort());
			array.add(roomJson);
		}
		room.put("servers", array);
		return room.toString();
	}
//u
	public String change(Cabinet cabinet) {
		if(cabinet==null || cabinet.getId()==0 || StaticMethod.StrSize(cabinet.getName())<1 ||
				cabinet.getCabStyle()==null || cabinet.getCabRoom()==null) {
			return StaticMethod.inputError;
		}
		cdao.update(cabinet);
		return StaticMethod.changeSucc;
	}
	/**
	 * 更改顺序
	 * @param json 格式为id1_sort A id2_sort
	 */
	public String changeSort(String json) {
		if(StaticMethod.StrSize(json) < 3) {
			return StaticMethod.inputError;
		}
		Set<Integer> ids = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("A")) {
			String[] str = s.split("_");
			try {
				id = Integer.parseInt(str[0]);
				Integer.parseInt(str[1]);
				if(ids.add(id)) {
					cdao.updateSort(str[0], str[1]);
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.changeSucc;
	}
	/**
	 * 为机柜更改机房
	 * @param json roomId A cId a cId
	 */
	public String changeRoom(String json) {
		if(StaticMethod.StrSize(json) < 3) {
			return StaticMethod.inputError;
		}
		String[] ids = json.split("A");
		int rId = 0;
		try {
			rId = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		if(! cdao.chkExit(rId, "Room")) {
			return StaticMethod.FailMess("机房不存在！");
		}
		if(ids.length > 1) {
			int cId = 0;
			Set<Integer> uids = new HashSet<Integer>();
			for(String cablinet : ids[1].split("a")) {
				try {
					cId = Integer.parseInt(cablinet);
				} catch(NumberFormatException e) {}
				if(uids.add(cId)) {
					cdao.updateRoom(cId, rId);
				}
			}
		}
		return StaticMethod.changeSucc;
	}
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		Set<Integer> ids = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("a")) {
			try {
				id = Integer.parseInt(s);
				if(ids.add(id)) {
					cdao.delete(s, "Cabinet");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
}
