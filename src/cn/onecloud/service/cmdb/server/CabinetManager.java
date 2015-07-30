package cn.onecloud.service.cmdb.server;

import net.sf.json.JSONObject;

import cn.onecloud.model.cmdb.server.Cabinet;
import cn.onecloud.util.page.cmdb.CabinetPage;

public interface CabinetManager {

//c
	public String save(Cabinet cabinet);
//r
	/**
	 * 分页查找
	 */
	public JSONObject findByPage(CabinetPage page);
	/**
	 * 根据ID查找机房信息以及机柜信息
	 */
	public String findById(String json);
//u
	public String change(Cabinet cabinet);
	/**
	 * 更改顺序
	 * @param json 格式为id1_sort A id2_sort
	 */
	public String changeSort(String json);
	/**
	 * 为机柜更改机房
	 * @param json roomId A cId a cId
	 */
	public String changeRoom(String json);
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json);
}
