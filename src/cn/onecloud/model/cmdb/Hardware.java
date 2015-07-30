package cn.onecloud.model.cmdb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cn.onecloud.model.cmdb.server.Server;

/**
 * 服务器的硬件信息表，此表主要由nrpe更新
 */
@Entity
@Table(name="hardware")
public class Hardware implements Serializable {

	private static final long serialVersionUID = 6771554205687723810L;
	
	private int id;
	private Server hdServer;
	private String mac;
	private String cpu;
	private String memory;
	private String storage;
	private String raid;
	private String motherboard;
	
	public Hardware() {}
	public Hardware(int id, String mac, String motherboard, String cpu, String memory,
			String storage, String raid) {
		this.id = id;
		this.mac = mac;
		this.motherboard = motherboard;
		this.cpu = cpu;
		this.memory = memory;
		this.storage = storage;
		this.raid = raid;
	}
//set get
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Server getHdServer() {
		return hdServer;
	}
	public void setHdServer(Server hdServer) {
		this.hdServer = hdServer;
	}
	@Column
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	@Column
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	@Column
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	@Column
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	@Column
	public String getRaid() {
		return raid;
	}
	public void setRaid(String raid) {
		this.raid = raid;
	}
	@Column
	public String getMotherboard() {
		return motherboard;
	}
	public void setMotherboard(String motherboard) {
		this.motherboard = motherboard;
	}
}
