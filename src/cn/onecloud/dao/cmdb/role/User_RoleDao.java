package cn.onecloud.dao.cmdb.role;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.menu.User_Role;

@Repository("userRoleDao")
public class User_RoleDao extends UtilDao {

//c
//r
	@SuppressWarnings("unchecked")
	public List<User_Role> getU_R_ByUId(int id) {
		return ht.find("from User_Role ur where ur.user.id=?", id);
	}
	@SuppressWarnings("unchecked")
	public List<User_Role> get_U_RByUId(int id) {
		return ht.find("from User_Role ur join fetch ur.role where ur.user.id=?", id);
	}
//d
	public void delete(int userId) {
		ht.bulkUpdate("delete from User_Role ur where ur.user.id=?", userId);
	}
	public void deleteU_R_ByRid(int roleId) {
		ht.bulkUpdate("delete from User_Role ur where ur.role.id=?", roleId);
	}
	public void delete(int userId, int roleId) {
		ht.bulkUpdate("delete from User_Role ur where ur.user.id=" + userId +
			" and ur.role.id=" + roleId);
	}
}
