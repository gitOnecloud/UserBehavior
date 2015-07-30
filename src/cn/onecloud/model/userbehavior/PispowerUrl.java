package cn.onecloud.model.userbehavior;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="url_pispower")
/**
 * pispower的url说明
 * @author LF_eng
 *
 */
public class PispowerUrl {
	private long id;
	private String url;
	private String descrip;
	
	public PispowerUrl() {}
	public PispowerUrl(String descrip) {
		this.descrip = descrip;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column
	public String getDescrip() {
		return descrip;
	}
	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}
}
