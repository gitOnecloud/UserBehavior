package cn.onecloud.util.pojo.cmdb;

/**
 * 为输出excel，保存服务器ip或者机柜名，状态，备注等
 * @author LF_eng
 * 2014-6-9 15:54:36
 */
public class ServerName {
	private String name;
	private int type;
	private String remark;
	
	public ServerName() {}
	public ServerName(String name) {
		this.name = name;
	}
	public ServerName(String name, int type, String remark) {
		this.name = name;
		this.type = type;
		this.remark = remark;
	}
//set get
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
