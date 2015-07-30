package cn.onecloud.model.cmdb.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 服务器机柜
 */
@Entity
@Table(name="server_cabinet")
public class Cabinet {
	private int id;
	private String name;
	private int sort;
	private Style cabStyle;
	private Room cabRoom;
	private String remark;
	
	public Cabinet() {}
	//Server.init
	public Cabinet(int id) {
		this.id = id;
	}
	//CabinetDao.getCabients
	public Cabinet(int id, String name, int rId, String rName) {
		this.id = id;
		this.name = name;
		this.cabRoom = new Room(rId, rName);
	}
	//CabinetDao.getCabsByRId
	public Cabinet(int id, String name, int sort) {
		this.id = id;
		this.name = name;
		this.sort = sort;
	}
	//CabinetDao.getAllByPage
	public Cabinet(int id, String name, String remark, int sId, String sName,
			int rId, String rName) {
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.cabStyle = new Style(sId, sName);
		this.cabRoom = new Room(rId, rName);
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
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="tinyint(1) not null default'0'", updatable=false)
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne//(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Style getCabStyle() {
		return cabStyle;
	}
	public void setCabStyle(Style cabStyle) {
		this.cabStyle = cabStyle;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Room getCabRoom() {
		return cabRoom;
	}
	public void setCabRoom(Room cabRoom) {
		this.cabRoom = cabRoom;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
