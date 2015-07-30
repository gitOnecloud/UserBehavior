package cn.onecloud.model.cmdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="nagioscheck")
public class NagiosCheck
{
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	private String checkName;
	
	@Column
	private String description;
	
	@Column
	private Integer stoptime;
	
	@Column
	private String checkCommand;

	public NagiosCheck()
	{
	}

	public NagiosCheck(int id, String checkName,String description,int stoptime,String checkCommand)
	{
		this.id = id;
		this.checkName=checkName;
		this.checkCommand=checkCommand;
		this.description=description;
		this.stoptime = stoptime;
	}
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getCheckName()
	{
		return checkName;
	}

	public void setCheckName(String checkName)
	{
		this.checkName = checkName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getStoptime()
	{
		return stoptime;
	}

	public void setStoptime(Integer stoptime)
	{
		this.stoptime = stoptime;
	}

	public String getCheckCommand()
	{
		return checkCommand;
	}

	public void setCheckCommand(String checkCommand)
	{
		this.checkCommand = checkCommand;
	}
}
