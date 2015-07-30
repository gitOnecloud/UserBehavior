package cn.onecloud.model.cmdb.menu;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Role_MenuPK implements Serializable {
	private Menu menu;
	private Role role;
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof Role_MenuPK) {
			Role_MenuPK pk = (Role_MenuPK)o;
			if(this.role.getId() == pk.role.getId() && this.menu.getId() == pk.menu.getId()) {
			  return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.role.hashCode();
	}
}
