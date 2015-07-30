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
@Table(name="menu")
public class Menu {
	private int id;
	private String name;
	private String url;
	private int sort;
	private Menu parent;
	private List<Menu> children;
	private int display;
	private List<Role_Menu> roleMenus;
	
	public Menu() {}
	public Menu(int id) {
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
	@Column(columnDefinition="default'#'", length=50, nullable=false)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public Menu getParent() {
		return parent;
	}
	public void setParent(Menu parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy="parent")
	public List<Menu> getChildren() {
		return children;
	}
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'",  insertable=false)
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	@OneToMany(mappedBy="menu")
	public List<Role_Menu> getRoleMenus() {
		return roleMenus;
	}
	public void setRoleMenus(List<Role_Menu> roleMenus) {
		this.roleMenus = roleMenus;
	}
	
}
