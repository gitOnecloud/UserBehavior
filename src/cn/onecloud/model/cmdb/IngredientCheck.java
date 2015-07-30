package cn.onecloud.model.cmdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ingredientcheck")
public class IngredientCheck
{
	@Id
	@GeneratedValue
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="checkId")
	private NagiosCheck nagiosCheck;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ingredientDefineId")
	private IngredientDefine igDefine;
	
	@Column(columnDefinition="int default '1'")
	private Integer isActive;
	
	public IngredientCheck ()
	{
		
	}
	
	public IngredientCheck(int ncheckId,int idgId,int isActive)
	{
		this.nagiosCheck = new NagiosCheck();
		nagiosCheck.setId(ncheckId);
		this.igDefine = new IngredientDefine();
		igDefine.setId(idgId);
		this.isActive = isActive;
	}
	
	public IngredientCheck (int id, int igdId,String igdName,int ngcId,String ngcCheckName,String ngcDesc,int ngcInterval,String ngcCommand ,int isActive)
	{
		this.id = id;
		this.isActive = isActive;
		this.nagiosCheck=new NagiosCheck(ngcId, ngcCheckName, ngcDesc, ngcInterval, ngcCommand);
		this.igDefine=new IngredientDefine(igdId, igdName);
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public NagiosCheck getNagiosCheck()
	{
		return nagiosCheck;
	}

	public void setNagiosCheck(NagiosCheck nagiosCheck)
	{
		this.nagiosCheck = nagiosCheck;
	}

	public IngredientDefine getIgDefine()
	{
		return igDefine;
	}

	public void setIgDefine(IngredientDefine igDefine)
	{
		this.igDefine = igDefine;
	}

	public Integer getIsActive()
	{
		return isActive;
	}

	public void setIsActive(Integer isActive)
	{
		this.isActive = isActive;
	}
}
