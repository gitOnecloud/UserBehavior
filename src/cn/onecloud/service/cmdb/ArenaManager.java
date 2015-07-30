package cn.onecloud.service.cmdb;

import cn.onecloud.model.cmdb.arena.Arena;
import cn.onecloud.model.cmdb.arena.Satellite;

public interface ArenaManager {

//c
	public String save(Arena arena, Satellite satellite, String json);
//r
	/**
	 * 按树形获取环境
	 */
	public String findArena();
	/**
	 * 读取环境信息
	 */
	public String findAll();
	/**
	 * 读取全部Satellite
	 */
	public String findSatellite();
//u
	public String change(Arena arena, Satellite satellite, String json);
//d
	/**
	 * @param json 保存rouyer等id
	 */
	public String remove(Arena arena, String json);
}
