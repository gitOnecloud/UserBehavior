package cn.onecloud.model.cmdb.menu;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(Role_MenuPK.class)
@Table(name="role_menu")
public class Role_Menu {
	private Menu menu;
	private Role role;
	public Role_Menu() {};
	public Role_Menu(int roleId, int menuId) {
		this.role = new Role(roleId);
		this.menu = new Menu(menuId);
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
}
