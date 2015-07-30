package cn.onecloud.service.userbehavior;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.userbehavior.AppInfoDao;
import cn.onecloud.dao.userbehavior.QualityStatusDao;
import cn.onecloud.model.userbehavior.AppInfo;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

@Service
public class QualityStatusManager 
{
	@Resource(name="appInfoDao")
	private AppInfoDao appInfoDao;
	
	@Resource
	private QualityStatusDao qualityStatusDao;
	
	@SuppressWarnings("unchecked")
	public String findApp (TrafficAllPage page)
	{
		AppInfo ai = appInfoDao.getAppInfo(page);
		List<Object[]> infos = null;
		JSONObject result = new JSONObject();
		if("AllApp".equals(page.getAliases()))
		{
			infos = this.qualityStatusDao.getAllQualityStatusInfo(page);
			result.put("user", "全部");
			result.put("appname", "全部");
			result.put("oaid", "全部");
			result.put("domain", "全部");
			result.put("aliases", "全部");
		}
		else
		{
			if(ai == null) 
			{
				return "{\"status\":1,\"mess\":\"未找到！\"}";
			}
			result.put("user", ai.getUser());
			result.put("appname", ai.getAppname());
			result.put("oaid", ai.getOaid());
			result.put("domain", ai.getDomain());
			result.put("aliases", ai.getAliases());
			infos = this.qualityStatusDao.getQualityStatusInfo(page, ai.getId());
		}
		List<Integer> status = this.qualityStatusDao.getStatus();
		
		
		Map<Integer, BigDecimal> statusMap = new HashMap<Integer, BigDecimal>();
		
		
		for (Object[] info : infos) 
		{
			statusMap.put((Integer) info[0], (BigDecimal) info[1]);
		}
		
		JSONArray array = new  JSONArray();
		for(int statusNumber :status)
		{
			JSONObject object = new JSONObject();
			if(statusMap.containsKey(statusNumber))
			{
				object.put("status", statusNumber);
				object.put("amount", statusMap.get(statusNumber));
			}
			else
			{
				object.put("status", statusNumber);
				object.put("amount", 0);
			}
			array.add(object);
		}
		result.put("info", array);
		result.put("date", page.getDate());
		return result.toString();
	}
}
