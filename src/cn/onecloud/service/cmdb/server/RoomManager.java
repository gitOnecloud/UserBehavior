package cn.onecloud.service.cmdb.server;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.onecloud.model.cmdb.server.Room;
import cn.onecloud.util.page.cmdb.RoomPage;

public interface RoomManager {

//c
	public String save(Room room);
//r
	/**
	 * 根据ID查找机房信息以及机柜信息
	 */
	public String findById(String json);
	/**
	 * 分页查找
	 */
	public JSONObject findByPage(RoomPage page);
	/**
	 * 查找全部样式
	 */
	public JSONArray findAll();
	/**
	 * 
	 * @param json
	 * @return 0-机房名称 1-excel输出流
	 * @throws IOException
	 */
	public List<Object> download(String json) throws IOException;
//u
	public String change(Room room);
//d
	/**
	 * @param json ids，由字母a隔开
	 */
	public String remove(String json);
}
