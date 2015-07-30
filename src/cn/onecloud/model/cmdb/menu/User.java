package cn.onecloud.model.cmdb.menu;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
//linux系统的mysql区分大小写，因此写上创建表的名称
@Table(name="user")
public class User {
	private int id;
	private String name;
	private String account;
	private String password;
	private List<User_Role> user_roles;
	
	public User() {}
	public User(int id) {
		this.id = id;
	}
	//UserDaoImpl.getU_ByDid 2.GoodsRecord.init
	public User(int aId, String aName) {
		this.id = aId;
		this.name = aName;
	}
	public User(int aId, String aName, String account) {
		this.id = aId;
		this.name = aName;
		this.account = account;
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
	@Column(length=20, nullable=false, unique=true)
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	@Column(columnDefinition="char(32)", nullable=false)//自定义类型
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@OneToMany(mappedBy="user")//,fetch=FetchType.LAZY)
	public List<User_Role> getUser_roles() {
		return user_roles;
	}
	public void setUser_roles(List<User_Role> user_roles) {
		this.user_roles = user_roles;
	}
}
