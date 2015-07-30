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
@Table(name="page_view_all")
/**
 * 域名浏览数
 * @author LF_eng
 *
 */
public class PageViewAll {
	private long id;
	private Date date;
	private int hour;
	private String url;
	private long amount;
	private Integer status;
	private AppInfo appinfo;
	
	public PageViewAll(){}
	
	//PageViewAllDao.getAll
	public PageViewAll(String user, String appname, String oaid, String domain,
			long amount){
		this.amount = amount;
		this.appinfo = new AppInfo(user, appname, oaid, domain);
	}
	//PageViewAllDao.getAllByBId
	public PageViewAll(Date date, int hour, long amount){
		this.date = date;
		this.hour = hour;
		this.amount = amount;
	}
	//PageViewAllDao.getAllByMonth
	public PageViewAll(Object amount){
		if(amount != null) {
			this.amount = (Long) amount;
		}
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	@Column
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
