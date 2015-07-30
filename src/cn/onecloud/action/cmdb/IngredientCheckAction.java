package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.IngredientCheck;
import cn.onecloud.model.cmdb.NagiosCheck;
import cn.onecloud.service.cmdb.IngredientCheckManager;
import cn.onecloud.util.page.cmdb.NagiosCheckPage;

@Controller("nagiosCheck")
@Scope("prototype")
public class IngredientCheckAction extends CommonAction
{
	private static final long serialVersionUID = -6348805983555357617L;

	@Resource
	private IngredientCheckManager ingredientCheckManager;
	
	private NagiosCheckPage page;
	
	private NagiosCheck nagiosCheck;
	
	private String verfyCheckName;
	
	private Integer ncheckId;
	
	private Integer igdId;
	
	private Integer icheckId;
	
	private Integer isActive;
	
	private String arenaName;
	
	private Integer arenaId;
	
	private String shellParams;
	
	public String list()
	{
		return SUCCESS;
	}
	
	public String list_js()
	{
		if (page == null)
		{
			page = new NagiosCheckPage();
		}
		json = ingredientCheckManager.findByPage(page);
		return JSON;
	}
	
	public String remove_js()
	{
		json = ingredientCheckManager.remove(json);
		return JSON;
	}
	
	public String addNagios()
	{
		json = ingredientCheckManager.addNagios(nagiosCheck);
		return JSON;
	}
	
	public String verifyCheckName()
	{
		json = ingredientCheckManager.verifyCheckName(verfyCheckName);
		return JSON;
	}
	
	public String verifyBasketUnique()
	{
		json = ingredientCheckManager.verifyBasketUnique(arenaId);
		return JSON;
	}
	
	public String nagios_list()
	{
		if(page == null)
		{
			page = new NagiosCheckPage();
		}
		json = ingredientCheckManager.findNagiosAllPage(page);
		return JSON;
	}
	
	public String addNagiosCheck()
	{
		IngredientCheck icheck =new IngredientCheck(ncheckId, igdId,isActive);
		json = ingredientCheckManager.addNagiosCheck(icheck);
		return JSON;
	}
	
	public String updateNagiosCheck()
	{
		IngredientCheck icheck =new IngredientCheck(ncheckId, igdId,isActive);
		icheck.setId(icheckId);
		json = ingredientCheckManager.updateNagiosCheck(icheck);
		return JSON;
	}
	
	public String mornitor_js()
	{
		json = ingredientCheckManager.getNagiosMornitor();
		return JSON;
	}
	
	public String satelittle_js()
	{
		json = ingredientCheckManager.getNagiosSatellite(arenaName);
		return JSON;
	}
	
	public String basket_js()
	{
		json = ingredientCheckManager.getNagiosBasket(arenaId);
		return JSON;
	}
	
	public String updateNagios()
	{
		json = ingredientCheckManager.updateNagios(shellParams);
		return JSON;
	}
	
	public NagiosCheck getNagiosCheck()
	{
		return nagiosCheck;
	}

	public void setNagiosCheck(NagiosCheck nagiosCheck)
	{
		this.nagiosCheck = nagiosCheck;
	}

	public String getVerfyCheckName()
	{
		return verfyCheckName;
	}

	public void setVerfyCheckName(String verfyCheckName)
	{
		this.verfyCheckName = verfyCheckName;
	}

	public NagiosCheckPage getPage()
	{
		return page;
	}

	public void setPage(NagiosCheckPage page)
	{
		this.page = page;
	}

	public Integer getNcheckId()
	{
		return ncheckId;
	}

	public void setNcheckId(Integer ncheckId)
	{
		this.ncheckId = ncheckId;
	}

	public Integer getIgdId()
	{
		return igdId;
	}

	public void setIgdId(Integer igdId)
	{
		this.igdId = igdId;
	}

	public Integer getIcheckId()
	{
		return icheckId;
	}

	public void setIcheckId(Integer icheckId)
	{
		this.icheckId = icheckId;
	}

	public String getArenaName()
	{
		return arenaName;
	}

	public void setArenaName(String arenaName)
	{
		this.arenaName = arenaName;
	}

	public Integer getArenaId()
	{
		return arenaId;
	}

	public void setArenaId(Integer arenaId)
	{
		this.arenaId = arenaId;
	}

	public String getShellParams()
	{
		return shellParams;
	}

	public void setShellParams(String shellParams)
	{
		this.shellParams = shellParams;
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