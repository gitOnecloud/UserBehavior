package cn.onecloud.model.cmdb.arena;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="arena")
public class Arena implements Serializable {

	private static final long serialVersionUID = -8797344766343332979L;
	
	private int id;
	private String name;
	private String type;
	
	public Arena(){}
	public Arena(int id) {
		this.id = id;
	}
	public Arena(int id,String name){
		this.id = id;
		this.name = name;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=50, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=50, nullable=false)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
