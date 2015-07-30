package cn.onecloud.model.userbehavior;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sourceURL")
/**
 * 访问pispower的来源
 * @author LF_eng
 *
 */
public class PispowerSourceUrl {
	private long id;
	private Date date;
	private int hour;
	private String sourceURL;
	private long amount;
	
	public PispowerSourceUrl() {}
	public PispowerSourceUrl(String url, long amount) {
		this.sourceURL = url;
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
	public String getSourceURL() {
		return sourceURL;
	}
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}
	
	@Column
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
}
