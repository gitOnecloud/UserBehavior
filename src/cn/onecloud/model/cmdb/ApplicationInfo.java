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

@Entity
@Table(name="applicationinfo")
public class ApplicationInfo
{
	@Column
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	private String oaid;
	
	@Column
	private String description;
	
	@JoinColumn(name="arena_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private Arena arenaSatellite;
	
	@Column(columnDefinition="tinyint")
	private Integer isMonitor;

	public ApplicationInfo()
	{
		
	}
	
	public ApplicationInfo(String oaid,String description,int arenaId,int isMonitor,String arenaName)
	{
		this.oaid = oaid;
		this.description = description;
		this.isMonitor = isMonitor;
		this.arenaSatellite = new Arena(arenaId,arenaName);
	}
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getOaid()
	{
		return oaid;
	}

	public void setOaid(String oaid)
	{
		this.oaid = oaid;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Arena getArenaSatellite()
	{
		return arenaSatellite;
	}

	public void setArenaSatellite(Arena arenaSatellite)
	{
		this.arenaSatellite = arenaSatellite;
	}

	public Integer getIsMonitor()
	{
		return isMonitor;
	}

	public void setIsMonitor(Integer isMonitor)
	{
		this.isMonitor = isMonitor;
	}
}