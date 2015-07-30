package cn.onecloud.model.userbehavior;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="app_info")
/**
 * 用户云架构信息
 * @author LF_eng
 *
 */
public class AppInfo {
	private int id;
	private String user;//PISPOWER帐号
	private String appname;//云架构名称
	private String oaid;
	private String domain;
	private String aliases;
	
	public AppInfo() {}
	public AppInfo(String user, String appname, String oaid, String domain) {
		this.user = user;
		this.appname = appname;
		this.oaid = oaid;
		this.domain = domain;
	}
	public AppInfo(int id, String user, String appname, String oaid,
			String domain, String aliases) {
		this.id = id;
		this.user = user;
		this.appname = appname;
		this.oaid = oaid;
		this.domain = domain;
		this.aliases = aliases;
	}
	@Id
	@GeneratedValue
	@Column(name="backend_id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Column
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	@Column
	public String getOaid() {
		return oaid;
	}
	public void setOaid(String oaid) {
		this.oaid = oaid;
	}
	@Column
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@Column
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
}
