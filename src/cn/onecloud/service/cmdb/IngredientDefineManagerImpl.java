package cn.onecloud.service.cmdb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;


import cn.onecloud.dao.cmdb.IngredientDefineDao;
import cn.onecloud.model.cmdb.IngredientDefine;
import cn.onecloud.util.StaticMethod;
import cn.onecloud.util.page.cmdb.IngredientDefinePage;

@Component("ingredientDefineManager")
public class IngredientDefineManagerImpl implements IngredientDefineManager {
	private IngredientDefineDao idDao;
	@Resource(name="ingredientDefineDao")
	public void setIdDao(IngredientDefineDao idDao) {
		this.idDao = idDao;
	}
//c
	public String save(IngredientDefine igdefine) {
		idDao.save(igdefine);
		return StaticMethod.addSucc;
	}
//r
	/**
	 * 分页获取组件信息
	 */
	@SuppressWarnings("unchecked")
	public String findByPage(IngredientDefinePage page) {
		List<IngredientDefine> ids = idDao.getByPage(page);
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		Set<Integer> pids = new HashSet<Integer>();
		for(IngredientDefine id : ids) {
			if(id.getParent() != null) {
				pids.add(id.getParent().getId());
			}
		}
		Map<Integer, String> parentMap = new HashMap<Integer, String>();
		if(pids.size() != 0) {
			List<IngredientDefine> igDefines = idDao.getNameByIds(
					StaticMethod.Array2Str(pids, ","));
			for(IngredientDefine id : igDefines) {
				parentMap.put(id.getId(), id.getName());
			}
		}
		for(IngredientDefine id : ids) {
			JSONObject idJson = new JSONObject();
			idJson.put("id", id.getId());
			idJson.put("name", id.getName());
			idJson.put("port", id.getPort());
			idJson.put("type", id.getType());
			idJson.put("register", id.getRegister());
			idJson.put("color", id.getColor());
			if(id.getParent() != null) {
				idJson.put("pid", id.getParent().getId());
				idJson.put("pname", parentMap.get(id.getParent().getId()));
			}
			array.add(idJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	
	/**
	 * 输入前半部分名字获取完整组件名
	 */
	@SuppressWarnings("unchecked")
	public String findNames(String name) {
		if(name==null || name.length()==0) {
			name = "";
			//return StaticMethod.JsonMess(0, "data", "[]");
		}
		if(name.length() > 50) {
			name = name.substring(0, 50);
		}
		List<IngredientDefine> igDefines = idDao.getNameByPart(name);
		if(igDefines.size() == 0) {
			return "[]";
			//return StaticMethod.JsonMess(0, "data", "[]");
		}
		//JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		for(IngredientDefine id : igDefines) {
			JSONObject idJson = new JSONObject();
			idJson.put("id", id.getId());
			idJson.put("value", id.getName());
			idJson.put("text", id.getName());
			array.add(idJson);
		}
		//json.put("data", array);
		//json.put("status", 0);
		return array.toString();
	}
	/**
	 * 查找组件之间父子关系
	 */
	public String findChildren(String id) {
		int igId;
		try {
			igId = Integer.parseInt(id);
		} catch(Exception e) {
			return StaticMethod.inputError;
		}
		Set<Integer> checkId = new HashSet<Integer>();
		id = Integer.toString(findParent(igId, checkId));//先找到根节点
		JSONArray array = new JSONArray();
		checkId.clear();
		List<IngredientDefine> children = idDao.getNameByIds(id);
		findChildren(array, children, checkId);//再往下找全部孩子节点
		return array.toString();
	}
	/**
	 * 递归查找根节点，根节点保存在checkId最后一个位置
	 */
	private int findParent(int id, Set<Integer> checkId) {
		if(checkId.add(id)) {
			int pId = idDao.getParentId(id);
			if(pId != 0) {
				return findParent(pId, checkId);
			}
		}
		return id;
	}
	/**
	 * 递归查找孩子节点
	 */
	@SuppressWarnings("unchecked")
	private void findChildren(JSONArray array, List<IngredientDefine> children, Set<Integer> checkId) {
		Set<Integer> ids = new HashSet<Integer>();
		for(IngredientDefine igd : children) {
			JSONObject idJson = new JSONObject();
			if(checkId.add(igd.getId())) {
				idJson.put("id", igd.getId());
				idJson.put("name", igd.getName());
				ids.add(igd.getId());
			} else {
				idJson.put("id", - igd.getId());
				idJson.put("name", igd.getName() + "--出现循环");
			}
			if(igd.getParent() != null) {
				idJson.put("pId", igd.getParent().getId());
			}
			array.add(idJson);
		}
		if(ids.size() != 0) {
			children = idDao.getChildren(StaticMethod.Array2Str(ids, ","));
			if(children.size() != 0) {
				findChildren(array, children, checkId);
			}
		}
	}
	/**
	 * 通过ids获取组件
	 * @param json 格式：id1-id2
	 */
	@SuppressWarnings("unchecked")
	public String findIdfByIds(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return "[]";
		}
		Set<Integer> ipSet = new HashSet<Integer>();
		for(String s : json.split("-")) {
			try {
				ipSet.add(Integer.parseInt(s));
			} catch(NumberFormatException e) {}
		}
		List<IngredientDefine> ids = idDao.getIdfByIds(StaticMethod.Set2Str(ipSet, ","));
		if(ids.size() == 0) {
			return "[]";
		}
		//查找父节点
		Set<Integer> pids = new HashSet<Integer>();
		for(IngredientDefine id : ids) {
			if(id.getParent() != null) {
				pids.add(id.getParent().getId());
			}
		}
		Map<Integer, String> parentMap = new HashMap<Integer, String>();
		if(pids.size() != 0) {
			List<IngredientDefine> igDefines = idDao.getNameByIds(
					StaticMethod.Array2Str(pids, ","));
			for(IngredientDefine id : igDefines) {
				parentMap.put(id.getId(), id.getName());
			}
		}
		JSONArray array = new JSONArray();
		for(IngredientDefine id : ids) {
			JSONObject idJson = new JSONObject();
			idJson.put("id", id.getId());
			idJson.put("name", id.getName());
			idJson.put("port", id.getPort());
			idJson.put("type", id.getType());
			idJson.put("register", id.getRegister());
			idJson.put("color", id.getColor());
			if(id.getParent() != null) {
				idJson.put("pid", id.getParent().getId());
				idJson.put("pname", parentMap.get(id.getParent().getId()));
			}
			array.add(idJson);
		}
		return array.toString();
	}
//u
	public String change(IngredientDefine igdefine) {
		idDao.update(igdefine);
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
		Set<Integer> npIds = new HashSet<Integer>();
		int id = 0;
		int count = 0;
		int count1 = 0;
		for(String s : json.split("a")) {
			try {
				id = Integer.parseInt(s);
				if(npIds.add(id)) {
					if(idDao.countChildren(id) == 0) {
						if(idDao.countServer(id) == 0) {
							idDao.delete(s, "IngredientDefine");
						} else {
							count1 ++;
						}
					} else {
						count ++;
					}
				}
			} catch(NumberFormatException e) {}
		}
		if(count==0 && count1==0) {
			return StaticMethod.removeSucc;
		} else {
			StringBuilder str = new StringBuilder();
			str.append("有" + (count+count1) + "个组件未删除(");
			if(count != 0) {
				str.append(" " + count + "个父组件 ");
			}
			if(count1 != 0) {
				str.append(" " + count1 + "个组件存在服务器 ");
			}
			return StaticMethod.SuccMess(str.toString() + ')');
		}
	}
}
