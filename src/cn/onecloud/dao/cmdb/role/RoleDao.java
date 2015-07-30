package cn.onecloud.dao.cmdb.role;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.menu.Role;


@Repository("roleDao")
public class RoleDao extends UtilDao {
//c
//r
	/**
	 * 读取全部角色信息
	 * @return [{id,name,sort,open,pId}]
	 */
	@SuppressWarnings("unchecked")
	public String getRoles() {
		List<Role> roles = ht.find("from Role r order by r.sort");
		if(roles.size() == 0) {
			return "[]";
		}
		JSONArray array = new JSONArray();
		for(Role r : roles) {
			JSONObject roleJson = new JSONObject();
			roleJson.put("id",  r.getId());
			roleJson.put("name", r.getName());
			roleJson.put("sort", r.getSort());
			roleJson.put("open", true);
			if(r.getParent() != null) {
				roleJson.put("pId",  r.getParent().getId());
			}
			array.add(roleJson);
		}
		return array.toString();
	}
	public Role getRoleById(int id) {
		return ht.get(Role.class, id);
	}
	@SuppressWarnings("unchecked")
	public boolean chkExit(int rid) {
		List<Integer> rids = ht.find("select r.id from Role r where r.id=" + rid);
		return rids.size()==0? false: true;
	}
//u
	public void update(Role role) {
		ht.update(role);
	}
	public void updateName(Role role) {
		ht.bulkUpdate("update Role r set r.name=? where r.id=?", role.getName(), role.getId());
	}
	public void updateSort(int id, int pid, int sort) {
		StringBuilder sql = new StringBuilder();
		if(pid != -1) {
			if(pid == 0)
				sql.append(",r.parent.id=null");
			else
				sql.append(",r.parent.id=" + pid);
		}
		if(sort != -1)
			sql.append(",r.sort=" + sort);
		if(sql.length() != 0) {
			ht.bulkUpdate("update Role r set " + sql.substring(1, sql.length()) +
					" where r.id=" + id);
		}
	}
//d
	public void delete(Role role) {
		ht.delete(role);
	}
}
