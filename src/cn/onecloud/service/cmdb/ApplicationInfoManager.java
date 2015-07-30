package cn.onecloud.service.cmdb;

import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.cmdb.ApplicationInfoDao;
import cn.onecloud.exception.InvokeShellException;
import cn.onecloud.model.cmdb.ApplicationInfo;
import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.util.LocalShell;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.ApplicationInfoPage;

@Service("applicationInfoManager")
public class ApplicationInfoManager
{
	@Resource(name="applicationInfoDao")
	private ApplicationInfoDao dao;
	
	@SuppressWarnings("unchecked")
	public String findByPage(ApplicationInfoPage page)
	{
		List<ApplicationInfo> infos = dao.getAllPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for(ApplicationInfo info :infos)
		{
			JSONObject appJson = new JSONObject();
			appJson.put("id", info.getId());
			appJson.put("oaid", info.getOaid());
			appJson.put("description", info.getDescription());
			appJson.put("isMonitor", info.getIsMonitor() == 1?"是":"否");
			appJson.put("satelliteName", info.getArenaSatellite().getName());
			appJson.put("satelliteId", info.getArenaSatellite().getId());
			array.add(appJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String findAllSatellite()
	{
		List<Arena> arenas = dao.getAllSatellite();
		JSONArray array = new JSONArray();
		for(Arena  arena : arenas)
		{
			JSONObject object = new JSONObject();
			object.put("arenaId", arena.getId());
			object.put("arenaName", arena.getName());
			array.add(object);
		}
		return array.toString();
	}
	
	public String addApplicationInfo(ApplicationInfo info)
	{
		dao.ht.saveOrUpdate(info);
		return StaticMethod.addSucc;
	}
	
	public String remove(String appInfoId)
	{
		dao.delete(appInfoId, "ApplicationInfo");
		return StaticMethod.removeSucc;
	}
	
	@SuppressWarnings("unchecked")
	public String freshNagiosConfig(String arenaName)
	{
		List<Object[]> ips = dao.findSatelliteIP(arenaName);
		JSONObject json = new JSONObject();
		if (ips.size() == 0)
		{
			json.put("status", 1);
			json.put("mess", "找到不到对应Nagios服务器Ip");
			return json.toString();
		}
		String shell = "/home/clouder/vs/program/nagios/libexec/check_nrpe -c configure_app -H "
				+ (String)(ips.get(0)[0]);
		String result;
		try
		{
			result = LocalShell.invokeShell(shell);
			json.put("status", 0);
			json.put("mess", result);
			return json.toString();
		} catch (InvokeShellException e)
		{
			json.put("status", 1);
			json.put("mess", e.getMessage());
			return json.toString();
		}
	}
}
