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
 * 服务器的软件信息表，此表主要由nrpe更新
 */
@Entity
@Table(name="software")
public class Software implements Serializable {

	private static final long serialVersionUID = 1905003953187431357L;
	
	private int id;
	private Server swServer;
	private String operationName;
	private String operationVersion;
	private String defaultGateway;
	private String openfile;
	private String hostname;
	
	public Software() {}
	public Software(int id, String operationName, String defaultGateway,
			String openfile, String hostname) {
		this.id = id;
		this.operationName = operationName;
		this.defaultGateway = defaultGateway;
		this.openfile = openfile;
		this.hostname = hostname;
	}
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
	public Server getSwServer() {
		return swServer;
	}
	public void setSwServer(Server swServer) {
		this.swServer = swServer;
	}
	@Column
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	@Column
	public String getOperationVersion() {
		return operationVersion;
	}
	public void setOperationVersion(String operationVersion) {
		this.operationVersion = operationVersion;
	}
	@Column
	public String getDefaultGateway() {
		return defaultGateway;
	}
	public void setDefaultGateway(String defaultGateway) {
		this.defaultGateway = defaultGateway;
	}
	@Column
	public String getOpenfile() {
		return openfile;
	}
	public void setOpenfile(String openfile) {
		this.openfile = openfile;
	}
	@Column
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
}
