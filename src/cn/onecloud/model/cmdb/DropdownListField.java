package cn.onecloud.model.cmdb;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="dropDownListField")
public class DropdownListField {
	private int id;
	private String name;	//名称
	private String comment;	//备注
	private List<DropdownListValue> values;
	
	public DropdownListField(){}
	
	public DropdownListField(int id, String name, String comment){
		this.id = id;
		this.name = name;
		this.comment = comment;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=50)
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	@OneToMany(mappedBy="field")
	public List<DropdownListValue> getValues() {
		return values;
	}
	public void setValues(List<DropdownListValue> values) {
		this.values = values;
	}
}
