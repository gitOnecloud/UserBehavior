package cn.onecloud.util.page.cmdb;

public class ApplicationInfoPage extends Page
{
	private String oaid;
	
	private String description;
	
	private String isMonitor;
	
	private String satellite;

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

	public String getIsMonitor()
	{
		return isMonitor;
	}

	public void setIsMonitor(String isMonitor)
	{
		this.isMonitor = isMonitor;
	}

	public String getSatellite()
	{
		return satellite;
	}

	public void setSatellite(String satellite)
	{
		this.satellite = satellite;
	}
}