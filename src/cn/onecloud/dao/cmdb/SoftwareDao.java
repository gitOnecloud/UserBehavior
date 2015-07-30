package cn.onecloud.dao.cmdb;


import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.Software;

@Repository("softwareDao")
public class SoftwareDao extends UtilDao{

	@SuppressWarnings("unchecked")
	public Software getSoftware(int server_id){
		List<Software> sw_list =  super.ht.find("from Software obj where obj.swServer='"+server_id+"'");
		if(sw_list.size()==0||sw_list==null){
			return null;
		}else{
			return sw_list.get(0);
		}
	}

	public void add(Software sw){
		super.ht.save(sw);
	}
	
	public void delete(Software sw){
		super.ht.delete(sw);
	}
}
