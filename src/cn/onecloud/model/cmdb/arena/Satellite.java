package cn.onecloud.model.cmdb.arena;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author LF_eng 2013年12月3日
 */
@Entity
@Table(name="satellite")
public class Satellite implements Serializable {

	private static final long serialVersionUID = 4364291118110475083L;
	
	private int id;
	private Arena slArena;
	private Rouyer slRouyer;
	private String domain;
	private int isPrimary;//是否是Main IDC
	
	public Satellite() {}
	public Satellite(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="arena_id", nullable=false)
	public Arena getSlArena() {
		return slArena;
	}
	public void setSlArena(Arena slArena) {
		this.slArena = slArena;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="rouyer_id", nullable=false)
	public Rouyer getSlRouyer() {
		return slRouyer;
	}
	public void setSlRouyer(Rouyer slRouyer) {
		this.slRouyer = slRouyer;
	}
	@Column(length=50)
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(int isPrimary) {
		this.isPrimary = isPrimary;
	}
}
