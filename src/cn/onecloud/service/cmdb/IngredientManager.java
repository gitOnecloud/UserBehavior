package cn.onecloud.service.cmdb;

import cn.onecloud.util.page.cmdb.IngredientPage;

public interface IngredientManager {
	
	public String get_list(IngredientPage page);
	
	/**
	 * 环境列表
	 * @return
	 */
	public String party_list();
	
	/**
	 * 组件列表
	 * @return
	 */
	public String idf_list();
	
//u
	/**
	 * nagios组件监控更新
	 * @param json serverId A igdId_igdId A status_status => 5A3A0
	 * @return
	 */
	public String changeStatus(String json);
}
