package cn.onecloud.action.cmdb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.onecloud.action.CommonAction;
import cn.onecloud.service.cmdb.DropDownListManager;
import cn.onecloud.util.page.cmdb.DropDownListFieldPage;

@SuppressWarnings("serial")
@Component("dropDownList")
@Scope("prototype")//必须注解为多态
public class dropDownListAction extends CommonAction {
//依赖注入	
	@Resource(name="ddlManager")
	private DropDownListManager ddlManager;
	
	private DropDownListFieldPage page;
	
	private int field_id;
	private String field_name;
	private String field_comment;
	
	private int value_id;
	private String value_name;
	private boolean value_valid;
	private int seq;
	
	private String type;
	
	public String list() {
		return SUCCESS;
	}
	
	public String listTable(){  
		json = ddlManager.getAllField(page);
		return JSON;
	}
	
	public String valuesList(){
		json = ddlManager.getValues(field_id);
		return JSON;
	}
	
	public String addField(){
		json = ddlManager.addField(field_name,field_comment);
		return JSON;
	}
	
	public String updateField(){
		json = ddlManager.updateField(field_id,field_name,field_comment);
		return JSON;
	}
	
	public String deleteField(){
		json = ddlManager.deleteField(field_id);
		return JSON;
	}
	
	public String addValue(){
		json = ddlManager.addValue(field_id,value_name,value_valid,seq);
		return JSON;
	}
	
	public String updateValue(){
		json = ddlManager.updateValue(value_id, value_name, value_valid, seq);
		return JSON;
	}
	
	public String deleteValue(){
		json = ddlManager.deleteValue(value_id);
		return JSON;
	}
	
	/**
	 * Get combobox data
	 * 获取下拉列表值
	 * @param field_name --下拉列表名称，例如：log_type
	 * @return [{"text":"","value":""}]
	 * 返回的json里面value和text是相同的，都为下拉列表值的名称
	 */
	public String getComboboxData(){
		json = ddlManager.getComboboxData_js(type);
		return JSON;
	}

	public DropDownListFieldPage getPage() {
		return page;
	}

	public void setPage(DropDownListFieldPage page) {
		this.page = page;
	}

	public int getField_id() {
		return field_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getField_comment() {
		return field_comment;
	}

	public void setField_comment(String field_comment) {
		this.field_comment = field_comment;
	}

	public int getValue_id() {
		return value_id;
	}

	public void setValue_id(int value_id) {
		this.value_id = value_id;
	}

	public String getValue_name() {
		return value_name;
	}

	public void setValue_name(String value_name) {
		this.value_name = value_name;
	}

	public boolean isValue_valid() {
		return value_valid;
	}

	public void setValue_valid(boolean value_valid) {
		this.value_valid = value_valid;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
