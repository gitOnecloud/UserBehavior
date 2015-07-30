package cn.onecloud.service.cmdb.server;

import java.io.InputStream;
import java.util.List;

import cn.onecloud.model.cmdb.server.Server;
import cn.onecloud.util.page.cmdb.IngredientPage;
import cn.onecloud.util.page.cmdb.ServerPage;

public interface ServerManager {

	public List<List<String>> get_current_server_list(IngredientPage page);
	
	/**
	 * 查找服务器软硬件信息
	 * @param server_id
	 * @return
	 */
	public String findHwSw(int server_id);
	
	/**
	 * 更新软硬件信息
	 * @param server_id
	 * @return
	 */
	public String updateHwSw(int server_id);
	
	/**
	 * 添加 server handware software ingredient
	 * @param ip_list
	 * @param isActive
	 * @param isVm
	 * @param party_id
	 * @param define_id
	 * @return
	 */
	public String addServer(List<String> ip_list,String isActive, String isVm,String party_id,String define_id,String remark);
	
	/**
	 * 修改 server  ingredient
	 * @param server_id
	 * @param isActive
	 * @param party_id
	 * @param define_id
	 * @return
	 */
	public String updateServer(String server_id,String isActive,String party_id,String define_id,String remark);
	
	/**
	 * 删除 server handware software ingredient
	 * @param array_server_id
	 * @return
	 */
	public String deleteServer(String[] array_server_id,String[] array_party_id);
	
	/**
	 * 查看服务器软硬件信息
	 */
	public String findServerByPage(ServerPage page);
	/**
	 * 更新手动输入的软硬件信息
	 */
	public String changeHSW(Server server);
	/**
	 * 导出excel
	 * @param spage 
	 * @throws Exception 
	 */
	public InputStream download(ServerPage page) throws Exception;
	/**
	 * 更改服务器在机柜的顺序
	 * @param json 格式为id1_sort A id2_sort
	 */
	public String changeSort(String json);
	/**
	 * 为服务器更改机柜
	 * @param json cabinetId A sId a sId
	 */
	public String changeCabinet(String json);
	/**
	 * 导出全部service列表
	 */
	public InputStream currentexport(IngredientPage page) throws Exception;
}
