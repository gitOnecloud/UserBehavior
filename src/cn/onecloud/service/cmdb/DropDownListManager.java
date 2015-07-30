package cn.onecloud.service.cmdb;

import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import cn.onecloud.dao.cmdb.DropDownListDao;
import cn.onecloud.model.cmdb.DropdownListField;
import cn.onecloud.model.cmdb.DropdownListValue;
import cn.onecloud.util.page.cmdb.DropDownListFieldPage;

/**
 * 数据字典：下拉列表维护
 *
 */
@Component("ddlManager")
public class DropDownListManager {
	
	private DropDownListDao ddlDao;
	@Resource(name="ddlDao")
	public void setDdlDao(DropDownListDao ddlDao) {
		this.ddlDao = ddlDao;
	}
	
	/**
	 * Get combobox data
	 * 获取下拉列表值
	 */
	public String getComboboxData_js(String field_name){
		List<DropdownListValue> list = ddlDao.getValueByFieldName(field_name);
		JSONArray rows = new JSONArray();
		for(DropdownListValue v : list){
			JSONObject row = new JSONObject();
			row.put("value", v.getName());
			row.put("text", v.getName());
			rows.add(row);
		}
		return rows.toJSONString();
	}
	
	public String getAllField(DropDownListFieldPage page){
		List<DropdownListField> list = ddlDao.getField(page);
		JSONObject obj = new JSONObject();
		obj.put("total", page.getCountNums());
		JSONArray rows = new JSONArray();
		for(DropdownListField f : list){
			JSONObject row = new JSONObject();
			row.put("field_id", f.getId());
			row.put("field_name", f.getName());
			row.put("field_comment", f.getComment());
			rows.add(row);
		}
		obj.put("rows", rows);
		
		return obj.toJSONString();
	}
	
	public String getValues(int field_id){
		List<DropdownListValue> list = ddlDao.getValues(field_id);
		JSONArray rows = new JSONArray();
		for(DropdownListValue v : list){
			JSONObject row = new JSONObject();
			row.put("value_id", v.getId());
			row.put("value_name", v.getName());
			row.put("value_valid", v.isValid());
			row.put("seq", v.getSeq());
			rows.add(row);
		}
		
		return rows.toJSONString();
	}
	
	public String addField(String field_name, String field_comment){
		DropdownListField ddlf = new DropdownListField();
		ddlf.setName(field_name);
		ddlf.setComment(field_comment);
		ddlDao.addField(ddlf);
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}
	
	public String updateField(int field_id, String field_name, String field_comment){
		ddlDao.updateField(field_id, field_name, field_comment);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
	
	public String deleteField(int field_id){
		if(field_id != 0){
			ddlDao.deleteValueByeFieldId(field_id);
			ddlDao.delete(String.valueOf(field_id), "DropdownListField");
			return "{\"status\":0,\"mess\":\"删除成功！\"}";
		}else{
			return "{\"status\":1,\"mess\":\"删除失败！\"}";
		}
	}
	
	public String addValue(int field_id, String value_name, boolean vaild, int seq){
		DropdownListField ddlf = new DropdownListField();
		ddlf.setId(field_id);
		DropdownListValue ddlv = new DropdownListValue();
		ddlv.setField(ddlf);
		ddlv.setName(value_name);
		ddlv.setValid(vaild);
		ddlv.setSeq(seq);
		ddlDao.addValue(ddlv);
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}
	
	public String updateValue(int value_id, String value_name, boolean valid, int seq){
		DropdownListValue value = new DropdownListValue();
		value.setId(value_id);
		value.setName(value_name);
		value.setValid(valid);
		value.setSeq(seq);
		ddlDao.updateValue(value);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
	
	public String deleteValue(int value_id){
		ddlDao.delete(String.valueOf(value_id), "DropdownListValue");
		return "{\"status\":0,\"mess\":\"删除成功！\"}";
	}
	
}
