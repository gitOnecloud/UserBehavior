package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.onecloud.action.CommonAction;
import cn.onecloud.model.cmdb.ApplicationInfo;
import cn.onecloud.service.cmdb.ApplicationInfoManager;
import cn.onecloud.util.page.cmdb.ApplicationInfoPage;

@Controller("applicationInfo")
@Scope("prototype")
public class ApplicationInfoAction extends CommonAction
{
	private static final long serialVersionUID = 3905211161702445342L;

	private ApplicationInfoPage page;
	
	private String removeAppInfoId;
	
	private String arenaName;
	
	private ApplicationInfo applicationInfo;
	
	@Resource(name="applicationInfoManager")
	private ApplicationInfoManager manager;
	public String findSatellite()
	{
		json = manager.findAllSatellite();
		return JSON;
	}
	
	public String list()
	{
		return SUCCESS;
	}

	public String list_js()
	{
		if (page == null)
		{
			page = new ApplicationInfoPage();
		}
		json = manager.findByPage(page);
		return JSON;
	}
	
	public String freshNagiosConfig()
	{
		json = manager.freshNagiosConfig(arenaName);
		return JSON;
	}
	
	public String save()
	{
		json = manager.addApplicationInfo(applicationInfo);
		return JSON;
	}
	
	public String remove()
	{
		json = manager.remove(removeAppInfoId);
		return JSON;
	}
	
	public ApplicationInfoPage getPage()
	{
		return page;
	}

	public void setPage(ApplicationInfoPage page)
	{
		this.page = page;
	}

	public ApplicationInfo getApplicationInfo()
	{
		return applicationInfo;
	}

	public void setApplicationInfo(ApplicationInfo applicationInfo)
	{
		this.applicationInfo = applicationInfo;
	}

	public String getRemoveAppInfoId()
	{
		return removeAppInfoId;
	}

	public void setRemoveAppInfoId(String removeAppInfoId)
	{
		this.removeAppInfoId = removeAppInfoId;
	}

	public String getArenaName()
	{
		return arenaName;
	}

	public void setArenaName(String arenaName)
	{
		this.arenaName = arenaName;
	}
}