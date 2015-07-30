package cn.onecloud.service.cmdb.server;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.onecloud.model.cmdb.server.Style;
import cn.onecloud.util.page.cmdb.StylePage;

public interface StyleManager {
//c
	public String save(Style style);
//r
	/**
	 * 查找全部样式
	 */
	public JSONArray findAll();
	/**
	 * 分页查找
	 */
	public JSONObject findByPage(StylePage page);
//u
	public String change(Style style);
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json);
	
}
