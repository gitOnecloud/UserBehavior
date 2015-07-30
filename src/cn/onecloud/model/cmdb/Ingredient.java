package cn.onecloud.model.cmdb;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.server.Server;

/**
 * 组件实例表
 */
@Entity
@Table(name="ingredient")
public class Ingredient {
	private int id;
	private IngredientDefine igDefine;
	private Server igServer;
	private int isActive;
	private Date createTime;
	private Date updateTime;
	private Arena igParty;
	
	public Ingredient(){}
	public Ingredient(int id) {
		this.id = id;
	}
	
	
	public Ingredient(int id, IngredientDefine igDefine,Server igServer, int isActive,
			 Date createTime,Date updateTime,Arena igParty) {
		this.id = id;
		this.igDefine = igDefine;
		this.igServer = igServer;
		this.isActive = isActive;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.igParty = igParty;
	}
	
	public Ingredient(int id,Server igServer, int isActive,
			 Date createTime,Date updateTime,Arena igParty) {
		this.id = id;
		this.igServer = igServer;
		this.isActive = isActive;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.igParty = igParty;
	}
	
	
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Server getIgServer() {
		return igServer;
	}
	public void setIgServer(Server igServer) {
		this.igServer = igServer;
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
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Arena getIgParty() {
		return igParty;
	}
	public void setIgParty(Arena igParty) {
		this.igParty = igParty;
	}
	
	
}
