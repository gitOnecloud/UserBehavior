package cn.onecloud.model.cmdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.onecloud.model.cmdb.arena.Arena;

/**
 * 虚拟IP记录表
 */
@Entity
@Table(name="vip")
public class Vip {
	private int id;
	private String ip;
	private IngredientDefine igDefine;
	private Arena viParty;
	private String description;
	
	public Vip(){}
	//VipDao.getAllByPage
	public Vip(int id, String ip, String description, int igdId, String igdName,
			int ptId, String ptName) {
		this.id = id;
		this.ip = ip;
		this.description = description;
		this.igDefine = new IngredientDefine(igdId, igdName);
		//this.viParty = new Party(ptId, ptName);
		this.viParty = new Arena();
		viParty.setId(ptId);
		viParty.setName(ptName);
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=50, nullable=false)//unique=true
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public IngredientDefine getIgDefine() {
		return igDefine;
	}
	public void setIgDefine(IngredientDefine igDefine) {
		this.igDefine = igDefine;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Arena getViParty() {
		return viParty;
	}
	public void setViParty(Arena viParty) {
		this.viParty = viParty;
	}
	@Column
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
