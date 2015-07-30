package cn.onecloud.service.cmdb.role;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.onecloud.dao.cmdb.role.RoleDao;
import cn.onecloud.dao.cmdb.role.Role_MenuDao;
import cn.onecloud.dao.cmdb.role.User_RoleDao;
import cn.onecloud.model.cmdb.menu.Role;
import cn.onecloud.model.cmdb.menu.Role_Menu;

@Service("roleManager")
public class RoleManager {
	private RoleDao roleDao;
	@Resource(name="roleDao")
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	private Role_MenuDao role_MenuDao;
	@Resource(name="roleMenuDao")
	public void setRole_MenuDao(Role_MenuDao role_MenuDao) {
		this.role_MenuDao = role_MenuDao;
	}
	private User_RoleDao user_RoleDao;
	@Resource(name="userRoleDao")
	public void setUser_RoleDao(User_RoleDao user_RoleDao) {
		this.user_RoleDao = user_RoleDao;
	}
//c
	public void save(Role role, int[] ids) {
		roleDao.save(role);
		if(ids != null)
			for(int aId : ids)
				role_MenuDao.save(new Role_Menu(role.getId(), aId));
	}
//r
	public String findRoles() {
		return roleDao.getRoles();
	}
	public Role findRA_ByR_id(int id) {
		Role role = roleDao.getRoleById(id);
		if(role != null)
			role.setRoleMenus(role_MenuDao.getAIdsById(id));
		return role;
	}
//u
	public void changeRA(int id, int[] ids) {
		role_MenuDao.deleteR_A_ByRid(id);
		if(ids != null)
			for(int aId : ids)
				role_MenuDao.save(new Role_Menu(id, aId));
	}
	public void changeName(Role role) {
		roleDao.updateName(role);
	}
	public void changeSort(String[] roles) {
		for(String role : roles) {
			String[] ids = role.split("_");
			roleDao.updateSort(Integer.parseInt(ids[0]),
					Integer.parseInt(ids[1]), Integer.parseInt(ids[2]));
		}
	}
//d
	public void remove(Role role) {
		user_RoleDao.deleteU_R_ByRid(role.getId());
		role_MenuDao.deleteR_A_ByRid(role.getId());
		roleDao.delete(role);
	}
}
