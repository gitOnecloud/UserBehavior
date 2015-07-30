package cn.onecloud.service.cmdb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.VipDao;
import cn.onecloud.model.cmdb.Vip;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.VipPage;

@Component("vipManager")
public class VipManager {
	private VipDao vdao;
	@Resource(name="vipDao")
	public void setVdao(VipDao vdao) {
		this.vdao = vdao;
	}

//c
	public String save(Vip vip) {
		vdao.save(vip);
		return StaticMethod.addSucc;
	}
//r
	@SuppressWarnings("unchecked")
	public String findByPage(VipPage page) {
		List<Vip> vips = vdao.getAllByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Vip vip : vips) {
			JSONObject vipJson = new JSONObject();
			vipJson.put("id", vip.getId());
			vipJson.put("ip", vip.getIp());
			vipJson.put("igdId", vip.getIgDefine().getId());
			vipJson.put("igdName", vip.getIgDefine().getName());
			vipJson.put("ptId", vip.getViParty().getId());
			vipJson.put("ptName", vip.getViParty().getName());
			vipJson.put("description", vip.getDescription());
			array.add(vipJson);
		}
		json.put("rows", array);
		return json.toString();
	}
//u
	public String change(Vip vip) {
		vdao.update(vip);
		return StaticMethod.changeSucc;
	}
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json) {
		if(json==null || json.isEmpty()) {
			return StaticMethod.inputError;
		}
		Set<Integer> ids = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("a")) {
			try {
				id = Integer.parseInt(s);
				if(ids.add(id)) {
					vdao.delete(s, "Vip");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}

}
