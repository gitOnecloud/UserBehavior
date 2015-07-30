package cn.onecloud.util.page.cmdb;


public class NagiosCheckPage extends Page
{
	private String checkName;
	
	private String checkCommand;
	
	private String stoptime;
	
	private String igdefine;

	/**
	 * 计算页数
	 * @param countNums
	 */
	public void countPage(int countNums) {
		if(super.getNum()<1 || super.getNum()>100)
			super.setNum(10);
		super.countPage(countNums);
	}
	
	public String getCheckName()
	{
		return checkName;
	}

	public void setCheckName(String checkName)
	{
		this.checkName = checkName;
	}

	public String getCheckCommand()
	{
		return checkCommand;
	}

	public void setCheckCommand(String checkCommand)
	{
		this.checkCommand = checkCommand;
	}

	public String getStoptime()
	{
		return stoptime;
	}

	public void setStoptime(String stoptime)
	{
		this.stoptime = stoptime;
	}

	public String getIgdefine()
	{
		return igdefine;
	}

	public void setIgdefine(String igdefine)
	{
		this.igdefine = igdefine;
	}
}
