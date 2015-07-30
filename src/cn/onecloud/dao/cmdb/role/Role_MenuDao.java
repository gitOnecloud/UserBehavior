package cn.onecloud.dao.cmdb.role;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.menu.Role_Menu;

@Repository("roleMenuDao")
public class Role_MenuDao extends UtilDao {

//c

//r
	@SuppressWarnings("unchecked")
	public List<Role_Menu> getRole_Menus() {
		return ht.find("from Role_Menu");
	}
	@SuppressWarnings("unchecked")
	public List<Role_Menu> getAIdsById(int roleId) {
		return ht.find("from Role_Menu ra where ra.role.id=?", roleId);
	}
	/**
	 * 通过角色id获取权限id
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getAIdsByRIds(String rids) {
		return ht.find("select a.id from Menu a where a.display=1 and " +
				"a.id in(select distinct ra.menu.id from Role_Menu ra " +
				"where ra.role.id in(" + rids + "))");
	}
	
	@SuppressWarnings("unchecked")
	public String getRoAus() {
		List<Role_Menu> roAus = ht.find("from Role_Menu");
		if(roAus.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(Role_Menu ra : roAus) {
			json.append("{\"rid\":" + ra.getRole().getId() + 
					",\"aid\":" + ra.getMenu().getId() + "},");
		}
		return json.substring(0, json.length()-1) + ']';
	}
//d
	/**
	 * 通过角色id删除角色与权限的关系
	 */
	public void deleteR_A_ByRid(int id) {
		ht.bulkUpdate("delete from Role_Menu r where r.role.id=?", id);
	}
	/**
	 * 通过权限id删除角色与权限的关系
	 */
	public void deleteR_A_ByAid(int id) {
		ht.bulkUpdate("delete from Role_Menu r where r.menu.id=?", id);
	}
}
