package cn.onecloud.action.userbehavior;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.onecloud.service.userbehavior.QualityStatusManager;
import cn.onecloud.util.page.userbehavior.TrafficAllPage;

import com.opensymphony.xwork2.ActionSupport;


@Controller("qualityStatus")
@Scope("prototype")
public class QualityStatusAction extends ActionSupport
{
	private static final long serialVersionUID = -1352675325303717579L;
	
	private TrafficAllPage page;
	
	private String json;
	@Resource
	private QualityStatusManager qualityStatusManager;
	
	public String list()
	{
		page = new TrafficAllPage("AllApp");
		json = qualityStatusManager.findApp(page);
		return 	SUCCESS;
	}

	public String list_js() 
	{
		if(StringUtils.isBlank(page.getAliases()) && StringUtils.isBlank(page.getDomain()) && StringUtils.isBlank(page.getOaid())) 
		{
			String date = page.getDate();
			page = new TrafficAllPage("AllApp");
			page.setDate(date);
		}
		json = qualityStatusManager.findApp(page);
		return "json";
	}
	
	public TrafficAllPage getPage() {
		return page;
	}

	public void setPage(TrafficAllPage page) {
		this.page = page;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}