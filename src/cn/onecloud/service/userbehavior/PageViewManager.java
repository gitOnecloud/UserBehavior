package cn.onecloud.service.userbehavior;

import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.userbehavior.PageViewDao;
import cn.onecloud.model.userbehavior.PispowerPageView;
import cn.onecloud.util.page.userbehavior.Page;

@Service("pageViewManager")
public class PageViewManager {

	private PageViewDao pdao;
	@Resource(name="pageViewDao")
	public void setPdao(PageViewDao pdao) {
		this.pdao = pdao;
	}

//r
	@SuppressWarnings("unchecked")
	public String findAll(Page page) {
		List<PispowerPageView> ivs = pdao.getAll(page);
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		for(PispowerPageView iv : ivs) {
			JSONObject ivJson = new JSONObject();
			ivJson.put("url", iv.getUrl().getDescrip());
			ivJson.put("amount", iv.getAmount());
			array.add(ivJson);
		}
		result.put("pageview", array);
		result.put("date", page.getDate());
		return result.toString();
	}

}
