package cn.onecloud.service.cmdb.role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.role.MenuDao;
import cn.onecloud.dao.cmdb.role.RoleDao;
import cn.onecloud.dao.cmdb.role.Role_MenuDao;
import cn.onecloud.model.cmdb.menu.Menu;
import cn.onecloud.model.cmdb.menu.Role;
import cn.onecloud.model.cmdb.menu.Role_Menu;

@Component("menuManager")
public class MenuManager {
	private MenuDao menuDao;
	@Resource(name="menuDao")
	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	private Role_MenuDao role_MenuDao;
	@Resource(name="roleMenuDao")
	public void setRole_MenuDao(Role_MenuDao role_MenuDao) {
		this.role_MenuDao = role_MenuDao;
	}
	private RoleDao roleDao;
	@Resource(name="roleDao")
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

//c
	public void save(Menu menu) {
		this.menuDao.save(menu);
	}
//r
	/**
	 * 排序读取菜单
	 * @return [{id,name,url,sort,open,pId,checked}]
	 */
	@SuppressWarnings("unchecked")
	public String findAsOnsort() {
		List<Menu> menus = menuDao.getAsOnsort();
		if(menus.size() == 0) {
			return "[]";
		}
		JSONArray array = new JSONArray();
		for(Menu a : menus) {
			JSONObject menuJson = new JSONObject();
			menuJson.put("id", a.getId());
			menuJson.put("name", a.getName());
			menuJson.put("url", a.getUrl());
			menuJson.put("sort", a.getSort());
			menuJson.put("open", true);
			if(a.getParent() != null)
				menuJson.put("pId", a.getParent().getId());
			menuJson.put("checked", a.getDisplay()==1? true: false);
			array.add(menuJson);
		}
		return array.toJSONString();
	}
	/**
	 * 结合角色的权限，组合全部权限
	 * @param role 为null时表示只取全部权限
	 * @return [{id,name,open,pId}]
	 */
	@SuppressWarnings("unchecked")
	public String findAsOnsort(Role role) {
		List<Menu> menus = menuDao.getAsOnsort();
		JSONArray array = new JSONArray();
		for(Menu a : menus) {
			JSONObject menuJson = new JSONObject();
			menuJson.put("id", a.getId());
			menuJson.put("name", a.getName());
			menuJson.put("sort", a.getSort());
			menuJson.put("open", "true");
			if(a.getParent() != null)
				menuJson.put("pId", a.getParent().getId());
			if(role != null)
				for(Role_Menu ra : role.getRoleMenus()) {
					if(ra.getMenu().getId() == a.getId()) {
						menuJson.put("checked", true);
						break;
					}
				}
			array.add(menuJson);
		}
		return array.toJSONString();
	}
	/**
	 * 排序读取显示的菜单
	 * @return [{id,name,action,,pId}]
	 */
	@SuppressWarnings("unchecked")
	public String findAsOnsd() {
		List<Menu> menus = menuDao.getAsOnsd();
		if(menus.size() == 0) {
			return "[]";
		}
		JSONArray array = new JSONArray();
		for(Menu a : menus) {
			JSONObject menuJson = new JSONObject();
			menuJson.put("id", a.getId());
			menuJson.put("name", a.getName());
			menuJson.put("action", a.getUrl());
			if(a.getParent() != null)
				menuJson.put("pId", a.getParent().getId());
			array.add(menuJson);
		}
		return array.toJSONString();
	}
	
	public Menu findA_RById(int id) {
		Menu auth = this.menuDao.getMenuById(id);
		return auth;
	}
	/**
	 * 获取全部权限，角色，以及关系
	 * @return {auths:findAsOnsort(null), roles:[], au_ros:[]}
	 */
	public String findRoAus() {
		return "{auths:" + findAsOnsort(null) +
				",roles:" + roleDao.getRoles() +
				",ro_aus:" + role_MenuDao.getRoAus() + "}";
	}
//u
	/**
	 * 更新菜单顺序
	 */
	public void changeMenus(String[] menus) {
		for(String auth : menus) {
			String[] ids = auth.split("_");
			menuDao.updateSort(Integer.parseInt(ids[0]),
					Integer.parseInt(ids[1]), Integer.parseInt(ids[2]),
					Integer.parseInt(ids[3]));
		}
	}
	public void changeA_nu(Menu menu) {
		menuDao.updateA_nu(menu);
	}
	public String changeRole(String json) {
		if(json==null || json.length()==0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		String[] ids = json.split("A");
		int aid = 0;
		try {
			aid = Integer.parseInt(ids[0]);
		} catch(NumberFormatException e) {}
		if(! menuDao.chkExit(aid, "Menu")) {
			return "{\"status\":1,\"mess\":\"权限不存在！\"}";
		}
		role_MenuDao.deleteR_A_ByAid(aid);
		if(ids.length > 1) {
			int rid = 0;
			Set<Integer> rids = new HashSet<Integer>();
			for(String role : ids[1].split("a")) {
				try {
					rid = Integer.parseInt(role);
				} catch(NumberFormatException e) {}
				if(rids.add(rid) && roleDao.chkExit(rid)) {
					role_MenuDao.save(new Role_Menu(rid, aid));
				}
			}
		}
		return "{\"status\":0}";
	}
//d
	public String remove(Menu menu) {
		int num = menuDao.getChildrenNum(menu.getId());
		if(num > 0) {
			return "{\"status\":1, \"mess\": \"存在" + num + "个子权限！\"}";
		}
		this.role_MenuDao.deleteR_A_ByAid(menu.getId());
		this.menuDao.delete(menu);
		return "{\"status\":0}";
	}

}
