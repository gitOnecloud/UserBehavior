package cn.onecloud.service.userbehavior;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.userbehavior.AppInfoDao;
import cn.onecloud.dao.userbehavior.IspDao;
import cn.onecloud.model.userbehavior.AppInfo;
import cn.onecloud.model.userbehavior.IpView;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

@Service("ispManager")
public class IspManager {

	private IspDao idao;
	@Resource(name="ispDao")
	public void setIdao(IspDao idao) {
		this.idao = idao;
	}
	private AppInfoDao aid;
	@Resource(name="appInfoDao")
	public void setAid(AppInfoDao aid) {
		this.aid = aid;
	}
	
	@SuppressWarnings("unchecked")
	public String findAll(TrafficAllPage page) {
		AppInfo ai = aid.getAppInfo(page);
		if(ai == null) {
			return "{\"status\":1,\"mess\":\"未找到！\"}";
		}
		JSONObject result = new JSONObject();
		result.put("user", ai.getUser());
		result.put("appname", ai.getAppname());
		result.put("oaid", ai.getOaid());
		result.put("domain", ai.getDomain());
		result.put("aliases", ai.getAliases());
		List<Object[]> ipsum = idao.getIpSum(page, ai.getId());
		Map<String, BigInteger> ipMap = new HashMap<String, BigInteger>();
		for(Object[] iv : ipsum) {
			ipMap.put((String) iv[0], (BigInteger) iv[1]);
		}
		List<IpView> ivs = idao.getAll(page, ai.getId());
		JSONArray array = new JSONArray();
		for(IpView iv : ivs) {
			JSONObject ivJson = new JSONObject();
			ivJson.put("isp", iv.getIp());
			//ivJson.put("ipsum", iv.getId());
			ivJson.put("ipsum", ipMap.get(iv.getIp()));
			ivJson.put("amount", iv.getAmount());
			array.add(ivJson);
		}
		result.put("ipview", array);
		result.put("date", page.getDate());
		return result.toString();
	}

}
