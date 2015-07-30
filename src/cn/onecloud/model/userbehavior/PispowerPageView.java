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
@Table(name="page_view")
/**
 * pispower的url访问
 * @author LF_eng
 *
 */
public class PispowerPageView {
	private long id;
	private Date date;
	private int hour;
	private PispowerUrl url;
	private long amount;
	
	public PispowerPageView() {}
	//PageViewDao.getAll
	public PispowerPageView(String descrip, long amount) {
		this.url = new PispowerUrl(descrip);
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
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="url_id")
	public PispowerUrl getUrl() {
		return url;
	}
	public void setUrl(PispowerUrl url) {
		this.url = url;
	}
	@Column
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
}
