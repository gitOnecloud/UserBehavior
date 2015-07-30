package cn.onecloud.dao.cmdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.DropdownListField;
import cn.onecloud.model.cmdb.DropdownListValue;
import cn.onecloud.util.page.cmdb.DropDownListFieldPage;

@Repository("ddlDao")
public class DropDownListDao extends UtilDao {
	
	public List<DropdownListField> getField(final DropDownListFieldPage page){
		String countHql = "select count(id) from DropdownListField d where 1=1 ";
		String hql = "select new DropdownListField(d.id,d.name,d.comment) " +
						   "from DropdownListField d where 1=1 ";
		StringBuffer conditionHql = new StringBuffer();
		List<String> params = new ArrayList<String>();
		if(page.getField_name() != null && !page.getField_name().equals("")){
			conditionHql.append("and d.name like (?) ");
			params.add("%" + page.getField_name() + "%");
		}
		return getObjsByPage(countHql + conditionHql, 
				   			 hql + conditionHql,
				             page, 
				             params); 
	}
	
	public List<DropdownListValue> getValues(int field_id){
		String hql = "select new DropdownListValue(d.id,d.name,d.valid,d.seq,d.field.id,d.field.name) " +
						   "from DropdownListValue d where d.field.id=? order by d.seq asc";
		List<DropdownListValue> list = ht.find(hql, field_id);
		return list;
	}
	
	public List<DropdownListValue> getValueByFieldName(String field_name){
		String hql = "select new DropdownListValue(d.id,d.name,d.valid,d.seq,d.field.id,d.field.name) " +
						   "from DropdownListValue d where d.field.name=? and d.valid=true order by d.seq asc";
		List<DropdownListValue> list = ht.find(hql, field_name);
		return list;
	}
	
	public void addField(DropdownListField field){
		ht.save(field);
	}
	
	public void updateField(int field_id, String field_name, String field_comment){
		String hql = "update DropdownListField set name='" + field_name + "',comment='" + field_comment + "'" +
					 "where id=" + field_id;
		ht.bulkUpdate(hql);
	}
	
	public void addValue(DropdownListValue value){
		ht.save(value);
	}
	
	public void updateValue(DropdownListValue value){
		String hql = "update DropdownListValue set name='" + value.getName() + "',valid=" + value.isValid() +
				",seq=" + value.getSeq() + " where id=?";
		ht.bulkUpdate(hql, value.getId());
	}
	
	public void deleteValueByeFieldId(int field_id){
		String hql = "delete from DropdownListValue d where d.field.id=?";
		ht.bulkUpdate(hql, field_id);
	}
}
