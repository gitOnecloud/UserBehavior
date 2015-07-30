package cn.onecloud.model.cmdb.arena;

import java.io.Serializable;

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
@Table(name="basket")
public class Basket implements Serializable {
	
	private static final long serialVersionUID = 5709955842367956876L;
	
	private int id;
	private Arena bkArena;
	private Satellite bkSatellite;
	
	public Basket() {}
	public Basket(Arena arena, int sId) {
		this.bkArena = arena;
		this.bkSatellite = new Satellite(sId);
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
	public Arena getBkArena() {
		return bkArena;
	}
	public void setBkArena(Arena bkArena) {
		this.bkArena = bkArena;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="satellite_id", nullable=false)
	public Satellite getBkSatellite() {
		return bkSatellite;
	}
	public void setBkSatellite(Satellite bkSatellite) {
		this.bkSatellite = bkSatellite;
	}
}
