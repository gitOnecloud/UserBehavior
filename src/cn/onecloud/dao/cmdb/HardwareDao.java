package cn.onecloud.dao.cmdb;


import java.util.List;

import org.springframework.stereotype.Repository;

import cn.onecloud.model.cmdb.Hardware;

@Repository("hardwareDao")
public class HardwareDao extends UtilDao{
	
	@SuppressWarnings("unchecked")
	public Hardware getHardware(int server_id){
		List<Hardware> hw_list =  super.ht.find("from Hardware obj where obj.hdServer='"+server_id+"'");
		if(hw_list.size()==0||hw_list==null){
			return null;
		}else{
			return hw_list.get(0);
		}
	}

	public void add(Hardware hw){
		super.ht.save(hw);
	}
	
	public void delete(Hardware hw){
		super.ht.delete(hw);
	}
}
