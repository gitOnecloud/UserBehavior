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
import javax.persistence.Transient;

@Entity
@Table(name="traffic_all")
/**
 * 域名上行和下行的流量
 * @author LF_eng
 *
 */
public class TrafficAll {
	private long id;
	private Date date;
	private int hour;
	private String domain;
	private long request_traffic;
	private long response_traffic;
	
	private Double uploadBandWidth;
	private Double downBandWidth;
	
	private AppInfo appinfo;
	
	public TrafficAll(){}

	
	//TrafficallDao.getAll
	public TrafficAll(String user, String appname, String oaid, String domain,
			long reqRraffic, long rspRraffic){
		this.request_traffic =  reqRraffic;
		this.response_traffic =  rspRraffic;
		this.appinfo = new AppInfo(user, appname, oaid, domain);
	}
	//TrafficallDao.getAllByBId
	public TrafficAll(Date date, int hour, long reqRraffic, long rspRraffic){
		this.date = date;
		this.hour = hour;
		this.request_traffic =  reqRraffic;
		this.response_traffic =  rspRraffic;
	}
	//TrafficallManager.findApp/findSum
	public TrafficAll(String date, long req, long res, long amount) {
		this.domain = date;
		this.request_traffic = req;
		this.response_traffic = res;
		this.id = amount;
	}
	//TrafficallManager.findApp/findSum
	public TrafficAll(String date, long req, long res, long amount, double uploadBandWidth, double downBandWidth)
	{
		this.domain = date;
		this.request_traffic = req;
		this.response_traffic = res;
		this.id = amount;
		this.uploadBandWidth = uploadBandWidth;
		this.downBandWidth = downBandWidth;
	}
	
	
	//TrafficallManager.getAllByMonth
	public TrafficAll(Object req, Object res) {
		if(req != null) {
			this.request_traffic = (Long) req;
		}
		if(res != null) {
			this.response_traffic = (Long) res;
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@Column
	public long getRequest_traffic() {
		return request_traffic;
	}
	public void setRequest_traffic(long request_traffic) {
		this.request_traffic = request_traffic;
	}
	@Column
	public long getResponse_traffic() {
		return response_traffic;
	}
	public void setResponse_traffic(long response_traffic) {
		this.response_traffic = response_traffic;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="backend_id")
	public AppInfo getAppinfo() {
		return appinfo;
	}
	public void setAppinfo(AppInfo appinfo) {
		this.appinfo = appinfo;
	}
	@Transient
	public Double getUploadBandWidth()
	{
		return uploadBandWidth;
	}
	public void setUploadBandWidth(Double uploadBandWidth)
	{
		this.uploadBandWidth = uploadBandWidth;
	}
	@Transient
	public Double getDownBandWidth()
	{
		return downBandWidth;
	}
	public void setDownBandWidth(Double downBandWidth)
	{
		this.downBandWidth = downBandWidth;
	}
}
