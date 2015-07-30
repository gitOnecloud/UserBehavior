package cn.onecloud.model.cmdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 客户端RSA密钥
 */
@Entity
@Table(name = "hessian_client")
public class HessianClient {
	private int id;
	private String name;
	private String domain;
	private String pubkey;
	private String prikey;
	private String modkey;
	private String ip;

// set get
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=63, nullable=false)
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@Column(nullable=false, updatable=false)
	public String getPubkey() {
		return pubkey;
	}
	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}
	//@Column(length=256, nullable=false)
	@Column(columnDefinition="varchar(256) not null", updatable=false)
	public String getPrikey() {
		return prikey;
	}
	public void setPrikey(String prikey) {
		this.prikey = prikey;
	}
	@Column(columnDefinition="varchar(256) not null", updatable=false)
	public String getModkey() {
		return modkey;
	}
	public void setModkey(String modkey) {
		this.modkey = modkey;
	}
	@Column(nullable=false)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

}
