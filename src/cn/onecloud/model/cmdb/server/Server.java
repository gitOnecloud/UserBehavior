package cn.onecloud.model.cmdb.server;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.onecloud.model.cmdb.Hardware;
import cn.onecloud.model.cmdb.Software;

/**
 * 服务器表，包括物理服务器和虚拟机服务器
 */
@Entity
@Table(name="server")
public class Server {
	private int id;
	private String ip;
	private int isActive;
	private Date createTime;
	private Date updateTime;
	private String remark;
	
	private Server host;
	private Hardware serverHW;
	private Software serverSW;
	
	private int sort;//服务器在机柜的位置
	private Cabinet svCabinet;//所属机柜
	
	public Server(){}
	public Server(int id) {
		this.id = id;
	}
	public Server(int id, String ip,int isActive, Date createTime,
			Date updateTime,Server host,String remark) {
		this.id = id;
		this.ip = ip;
		this.isActive = isActive;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.host = host;
		this.remark = remark;
	}
	//ServerDao.getSvsByCId
	public Server(int id, String ip, int sort, int isActive, String remark) {
		this.id = id;
		this.ip = ip;
		this.sort = sort;
		this.isActive = isActive;
		this.remark = remark;
	}
	//ServerDao.getByPage
	public Server(int id, String ip, Object sId, Object cabId,
			int hwId, String mac, String motherboard, String cpu, String memory, String storage, String raid,
			int swId, String operationName, String defaultGateway, String openfile, String hostname) {
		this.id = id;
		this.ip = ip;
		if(sId != null) {
			this.host = new Server((Integer) sId);
		}
		this.serverHW = new Hardware(hwId, mac, motherboard, cpu, memory, storage, raid);
		this.serverSW = new Software(swId, operationName, defaultGateway, openfile, hostname);
		if(cabId != null) {
			this.svCabinet = new Cabinet((Integer) cabId);
		}
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=50, nullable=false)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	@Column
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 如果hostID=id即是物理服务器，否则为虚拟服务器
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	//@JoinColumn(nullable=false)
	public Server getHost() {
		return host;
	}
	public void setHost(Server host) {
		this.host = host;
	}
	@Transient
	public Hardware getServerHW() {
		return serverHW;
	}
	public void setServerHW(Hardware serverHW) {
		this.serverHW = serverHW;
	}
	@Transient
	public Software getServerSW() {
		return serverSW;
	}
	public void setServerSW(Software serverSW) {
		this.serverSW = serverSW;
	}
	@Column(columnDefinition="tinyint(1) not null default'0'", updatable=false)
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	//@JoinColumn(nullable=false)
	public Cabinet getSvCabinet() {
		return svCabinet;
	}
	public void setSvCabinet(Cabinet svCabinet) {
		this.svCabinet = svCabinet;
	}
}
