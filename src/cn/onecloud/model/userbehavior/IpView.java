package cn.onecloud.model.userbehavior;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ip_view")
/**
 * pispower ip 访问记录
 * @author LF_eng
 *
 */
public class IpView {
	private long id;
	private Date date;
	private int hour;
	private String ip;
	private Isp isp;//ip信息
	private long amount;
	private AppInfo appinfo;
	
	public IpView() {}

	//1.使用ip暂存province， amount暂存ipnum
	//2.使用ip暂存isp， amount暂存ipnum
	public IpView(String province, long amount) {
		this.ip = province;
		this.amount = amount;
	}
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	@Column
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="isp_id")
	public Isp getIsp() {
		return isp;
	}
	public void setIsp(Isp isp) {
		this.isp = isp;
	}
	@Column
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="backend_id")
	public AppInfo getAppinfo() {
		return appinfo;
	}
	public void setAppinfo(AppInfo appinfo) {
		this.appinfo = appinfo;
	}
	
}
