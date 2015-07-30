package cn.onecloud.service.cmdb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import cn.onecloud.dao.cmdb.IngredientCheckDao;
import cn.onecloud.exception.InvokeShellException;
import cn.onecloud.model.cmdb.IngredientCheck;
import cn.onecloud.model.cmdb.NagiosCheck;
import cn.onecloud.util.LocalShell;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.NagiosCheckPage;

@Service
public class IngredientCheckManager
{
	@Resource
	private IngredientCheckDao checkDao;
	
	@SuppressWarnings("unchecked")
	public String findByPage (NagiosCheckPage page)
	{
		List<IngredientCheck> ingredientChecks = checkDao.getAllPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for (IngredientCheck check : ingredientChecks)
		{
			JSONObject checkJson = new JSONObject();
			checkJson.put("id", check.getId());
			checkJson.put("ncheckId", check.getNagiosCheck().getId());
			checkJson.put("isActive", check.getIsActive()== 0?"否":"是");
			checkJson.put("checkName", check.getNagiosCheck().getCheckName());
			checkJson.put("description", check.getNagiosCheck().getDescription());
			checkJson.put("stoptime", check.getNagiosCheck().getStoptime());
			checkJson.put("command", check.getNagiosCheck().getCheckCommand());
			checkJson.put("igdId", check.getIgDefine().getId());
			checkJson.put("igdName", check.getIgDefine().getName());
			array.add(checkJson);
		}
		json.put("rows", array);
		return json.toString();
	}

	/**
	 * @param json ids，由字母a隔开
	 */

	public String remove(String json)
	{
		if(json==null || json.isEmpty()) {
			return StaticMethod.inputError;
		}
		Set<Integer> ingredientCheckId = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("a")) {
			try {
				id = Integer.parseInt(s);
				if(ingredientCheckId.add(id)) {
					checkDao.delete(s, "IngredientCheck");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
	/**
	 *   Verify the checkName is not exist.
	 * @param checkName
	 * @return
	 */
	public String verifyCheckName(String checkName)
	{
		checkName = checkName.trim();
		List<NagiosCheck> checks = checkDao.verifyCheckName(checkName);
		if (checks.size()!= 0)
		{
			return StaticMethod.verifyError;
		}
		return StaticMethod.verifySucc;
	}
	
	public String addNagios(NagiosCheck check)
	{
		try
		{
			checkDao.ht.saveOrUpdate(check);
		}catch(Exception e){
			e.printStackTrace();
		}
		return StaticMethod.addSucc;
	}
	
	@SuppressWarnings("unchecked")
	public String findNagiosAllPage (NagiosCheckPage page)
	{
		List<NagiosCheck> nagiosChecks = checkDao.getNagiosAllPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		for (NagiosCheck check : nagiosChecks)
		{
			JSONObject checkJson = new JSONObject();
			checkJson.put("id", check.getId());
			checkJson.put("checkName", check.getCheckName());
			checkJson.put("description", check.getDescription());
			checkJson.put("stoptime", check.getStoptime());
			checkJson.put("command", check.getCheckCommand());
			array.add(checkJson);
		}
		json.put("rows", array);
		return json.toString();
	}

	public String addNagiosCheck(IngredientCheck icheck)
	{
		checkDao.save(icheck);
		return StaticMethod.addSucc;
	}
	
	public String updateNagiosCheck(IngredientCheck check)
	{
		checkDao.update(check);
		return StaticMethod.changeSucc;
	}
	
	@SuppressWarnings("unchecked")
	public String getNagiosMornitor()
	{
		List<Object []> mornitors = checkDao.getNagiosMornitor();
		JSONArray array = new  JSONArray();
		for(Object [] mornitor : mornitors)
		{
			JSONObject object = new JSONObject();
			object.put("arenaName", mornitor[0]);
			object.put("ingredientName", mornitor[1]);
			object.put("ip", mornitor[2]);
			object.put("parentName", mornitor[3]);
			array.add(object);
		}
		return array.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String getNagiosSatellite(String arenaName)
	{
		List<Object []> satellites = checkDao.getNagiosSatellite(arenaName);
		JSONArray array = new JSONArray();
		for(Object [] satellite : satellites)
		{
			JSONObject object = new JSONObject();
			object.put("satelliteName", satellite[0]);
			object.put("satelliteId", satellite[1]);
			array.add(object);
		}
		return array.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String getNagiosBasket(int arenaId)
	{
		List<Object []> baskets = checkDao.getNagiosBasket(arenaId);
		JSONArray array = new JSONArray();
		for(Object [] basket : baskets)
		{
			JSONObject object = new JSONObject();
			object.put("basketName", basket[0]);
			object.put("basketId", basket[1]);
			array.add(object);
		}
		return array.toString();
	}
	
	public String verifyBasketUnique(int arenaId)
	{
		List<Object[]> baskets = checkDao.verifyBasketUnique(arenaId);
		if (baskets.size() != 1)
		{
			return StaticMethod.verifyUniqueError;
		}
		return StaticMethod.verifyUniqueSucc;
	}
	
	@SuppressWarnings("unchecked")
	public String updateNagios(String shellParams)
	{
		String shell="/home/clouder/vs/program/nagios/libexec/check_nrpe"+shellParams;
		String result;
		JSONObject json = new JSONObject();
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
		/*if (StringUtils.isBlank(result)) 
		{
			json.put("status", 1);
			json.put("mess", "找不到脚本");
			return json.toString();
		}
		switch (result.trim())
		{
		case "0":
			json.put("status", 0);
			json.put("mess", "更新成功");
			break;
		case "1":
			json.put("status", 1);
			json.put("mess", "更新失败，回滚成功");
			break;
		case "2":
			json.put("status", 1);
			json.put("mess", "更新失败，回滚失败");
			break;
		default:
			break;
		}
		return json.toString();*/
	}
}
