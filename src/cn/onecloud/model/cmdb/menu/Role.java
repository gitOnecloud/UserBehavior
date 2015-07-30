package cn.onecloud.model.cmdb.menu;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	private int id;
	private String name;
	private int sort;
	private Role parent;
	private List<Role_Menu> roleMenus;
	
	public Role() {}
	public Role(int id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=20, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public Role getParent() {
		return parent;
	}
	public void setParent(Role parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy="role")//,fetch=FetchType.LAZY)
	public List<Role_Menu> getRoleMenus() {
		return roleMenus;
	}
	public void setRoleMenus(List<Role_Menu> roleMenus) {
		this.roleMenus = roleMenus;
	}
	/**
	 * check name
	 * @return 空:-1, 超出20:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 20) {
			return -2;
		}
		return name.length();
	}
}
