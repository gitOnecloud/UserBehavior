package cn.onecloud.model.cmdb.server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 服务器机房
 */
@Entity
@Table(name="server_room")
public class Room {
	private int id;
	private String name;
	private int sort;
	private Style cabStyle;
	private String remark;

	public Room() {}
	//RoomDao.getAll
	public Room(int id, String name) {
		this.id = id;
		this.name = name;
	}
	//RoomDao.getAllByPage
	public Room(int id, String name, String remark, int sId, String sName) {
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.cabStyle = new Style(sId, sName);
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
	@Column(columnDefinition="tinyint(1) not null default'0'")
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
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
