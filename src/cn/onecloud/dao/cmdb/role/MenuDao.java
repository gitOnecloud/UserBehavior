package cn.onecloud.dao.cmdb.role;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.dao.cmdb.UtilDao;
import cn.onecloud.model.cmdb.menu.Menu;

@Repository("menuDao")
public class MenuDao extends UtilDao {

//c	

//r
	/**
	 * 排序读取菜单
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getAsOnsort() {
		return (List<Menu>)ht.find("from Menu a order by a.sort");
	}
	/**
	 * 排序读取显示的菜单
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getAsOnsd() {
		return (List<Menu>)ht.find("from Menu a where a.display=1 order by a.sort");
	}
	public Menu getMenuById(int id) {
		return ht.get(Menu.class, id);
	}
	/**
	 * 获取子菜单个数
	 */
	public int getChildrenNum(int id) {
		long nums = (Long) ht.find("select count(*) " +
				"from Menu a where a.parent.id=?", id).get(0);
		return (int) nums;
	}
	/**
	 * 获取无角色的权限id
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getAll_AR_() {
		// where a.url!='#'
		return (List<Menu>) ht.find("from Menu a join fetch a.roleMenus");
	}
	/**
	 * 获取无角色的权限id
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getA_OnNoRole() {
		return (List<Integer>)ht.find("select a.id from Menu a where a.display=1 and " +
				"a.id not in (select distinct ra.menu.id from Role_Menu ra)");
	}
//u
	/**
	 * 更新菜单的父节点 顺序 显示
	 * @param id
	 * @param pid
	 * @param display 1显示 0不显示 -1不更新
	 * @param sort -1不更新 其它为顺序
	 */
	public void updateSort(int id, int pid, int display, int sort) {
		String sql = "";
		if(pid != -1) {
			if(pid == 0)
				sql = sql.concat(",a.parent.id=null");
			else
				sql = sql.concat(",a.parent.id=" + pid);
		}
		if(display != -1)
			sql = sql.concat(",a.display=" + display);
		if(sort != -1)
			sql = sql.concat(",a.sort=" + sort);
		if(!sql.isEmpty())
			ht.bulkUpdate("update Menu a set " + sql.substring(1, sql.length()) +
					" where a.id=" + id);
	}
	/**
	 * 更改菜单的名字和地址
	 */
	public void updateA_nu(Menu a) {
		if(a.getName().isEmpty()) {
			if(!a.getUrl().isEmpty()) {
				ht.bulkUpdate("update Menu a set a.url=? where a.id=?", a.getUrl(), a.getId());
			}
		} else {
			if(a.getUrl().isEmpty()) {
				ht.bulkUpdate("update Menu a set a.name=? where a.id=?", a.getName(), a.getId());
			} else {
				ht.bulkUpdate("update Menu a set a.name=?,a.url=? where a.id=?", a.getName(), a.getUrl(), a.getId());
			}
		}
	}
//d
	public void delete(Menu menu) {
		ht.delete(menu);
	}

}
