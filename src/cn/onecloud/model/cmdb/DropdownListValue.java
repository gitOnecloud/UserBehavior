package cn.onecloud.model.cmdb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="dropdownListValue")
public class DropdownListValue {
	private int id;
	private String name;		//名称
	private boolean valid;		//是否有效: 1-有效 ， 0-无效
	private int seq;			//顺序号
	private DropdownListField field;
	
	public DropdownListValue(){}
	
	public DropdownListValue(int id, String name, boolean valid, int seq, int field_id, String field_name){
		this.id = id;
		this.name = name;
		this.valid = valid;
		this.seq = seq;
		DropdownListField field = new DropdownListField();
		field.setId(field_id);
		field.setName(field_name);
		this.field = field;
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
	@Column(columnDefinition="bit(1)")
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	@Column(nullable=false)
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="field_id",nullable=false)
	public DropdownListField getField() {
		return field;
	}
	public void setField(DropdownListField field) {
		this.field = field;
	}
}
