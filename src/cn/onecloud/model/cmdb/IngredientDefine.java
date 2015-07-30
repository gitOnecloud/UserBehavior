package cn.onecloud.model.cmdb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 定义组件的基本属性
 */
@Entity
@Table(name="ingredientdefine")
public class IngredientDefine implements Serializable {

	private static final long serialVersionUID = -5498386418052760249L;
	
	private int id;
	private String name;
	private String port;
	private IngredientDefine parent;
	private String type;
	private int register;
	private String color;
	
	public IngredientDefine(){}
	public IngredientDefine(int id) {
		this.id = id;
	}
	//IngredientDefineDao.getNameByIds
	public IngredientDefine(int id, String name) {
		this.id = id;
		this.name = name;
	}
	//IngredientDefineDao.getChildren
	public IngredientDefine(int id, String name, int pid) {
		this.id = id;
		this.name = name;
		this.parent = new IngredientDefine(pid);
	}
	//IngredientDefineDao.getByPage
	public IngredientDefine(int id, String name, String port, Object pid,
			String type, int register, String color) {
		this.id = id;
		this.name = name;
		this.port = port;
		if(pid !=null) {
			this.parent = new IngredientDefine((Integer) pid);
		}
		this.type = type;
		this.register = register;
		this.color = color;
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
	@Column
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public IngredientDefine getParent() {
		return parent;
	}
	public void setParent(IngredientDefine parent) {
		this.parent = parent;
	}
	@Column(length=50, nullable=false)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getRegister() {
		return register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	@Column(columnDefinition="char(6) not null default'ffffff'")
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
