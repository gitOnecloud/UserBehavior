package cn.onecloud.service.userbehavior;

import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.userbehavior.SourceUrlDao;
import cn.onecloud.model.userbehavior.PispowerSourceUrl;
import cn.onecloud.util.page.userbehavior.Page;

@Service("sourceUrlManager")
public class SourceUrlManager {

	private SourceUrlDao sdao;
	@Resource(name="sourceUrlDao")
	public void setSdao(SourceUrlDao sdao) {
		this.sdao = sdao;
	}
//r
	@SuppressWarnings("unchecked")
	public String findAll(Page page) {
		List<PispowerSourceUrl> ivs = sdao.getAll(page);
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		for(PispowerSourceUrl iv : ivs) {
			JSONObject ivJson = new JSONObject();
			ivJson.put("url", iv.getSourceURL());
			ivJson.put("amount", iv.getAmount());
			array.add(ivJson);
		}
		result.put("sourceurl", array);
		result.put("date", page.getDate());
		return result.toString();
	}

}
