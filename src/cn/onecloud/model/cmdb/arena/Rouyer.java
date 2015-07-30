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
@Table(name="rouyer")
public class Rouyer implements Serializable {

	private static final long serialVersionUID = -3035515810783100501L;
	
	private int id;
	private Arena ryArena;
	
	public Rouyer() {}
	public Rouyer(Arena arena) {
		this.ryArena = arena;
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
	public Arena getRyArena() {
		return ryArena;
	}
	public void setRyArena(Arena ryArena) {
		this.ryArena = ryArena;
	}
}
