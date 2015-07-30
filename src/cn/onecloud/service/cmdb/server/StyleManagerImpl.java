package cn.onecloud.service.cmdb.server;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.server.StyleDao;
import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.StylePage;

@Component("styleManager")
public class StyleManagerImpl implements StyleManager {
	@Resource(name="styleDao")
	private StyleDao sdao;
//c
	public String save(Style style) {
		if(style==null || StaticMethod.StrSize(style.getName())<1) {
			return StaticMethod.inputError;
		}
		sdao.save(style);
		return StaticMethod.addSucc;
	}
//r
	/**
	 * 查找全部样式
	 */
	public JSONArray findAll() {
		List<Style> styles = sdao.getAll();
		JSONArray array = new JSONArray();
		for(Style style : styles) {
			JSONObject styleJson = new JSONObject();
			styleJson.put("id", style.getId());
			styleJson.put("name", style.getName());
			array.add(styleJson);
		}
		return array;
	}
	/**
	 * 分页查找
	 */
	public JSONObject findByPage(StylePage page) {
		List<Style> styles = sdao.getAllByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(Style style : styles) {
			JSONObject styleJson = new JSONObject();
			styleJson.put("id", style.getId());
			styleJson.put("name", style.getName());
			styleJson.put("horInterval", style.getHorInterval());
			styleJson.put("horNum", style.getHorNum());
			styleJson.put("verInterval", style.getVerInterval());
			styleJson.put("verNum", style.getVerNum());
			styleJson.put("insideHeight", style.getInsideHeight());
			styleJson.put("insideWidth", style.getInsideWidth());
			array.add(styleJson);
		}
		json.put("rows", array);
		return json;
	}
//u
	public String change(Style style) {
		if(style==null || style.getId()==0 || StaticMethod.StrSize(style.getName())<1) {
			return StaticMethod.inputError;
		}
		sdao.update(style);
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
					sdao.delete(s, "Style");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
	
}
